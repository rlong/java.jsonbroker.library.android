// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.android.ui;

import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.work.WorkManager;
import jsonbroker.library.server.broker.BrokerJob;
import jsonbroker.library.server.broker.JavascriptCallbackAdapter;
import jsonbroker.library.server.broker.JavascriptCallbackAdapterHelper;
import jsonbroker.library.server.broker.Service;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;

public class WebViewBridge extends WebViewClient implements JavascriptCallbackAdapter {

	private static final Log log = Log.getLog( WebViewBridge.class );

	////////////////////////////////////////////////////////////////////////////
	WebView _webView;
	
	////////////////////////////////////////////////////////////////////////////
	//
	Service _primaryService;
	
	////////////////////////////////////////////////////////////////////////////	
	//
	private boolean _active;

	
	////////////////////////////////////////////////////////////////////////////	
	//
	Handler _handler = new Handler();

	////////////////////////////////////////////////////////////////////////////	
	//
	
	
	
	@SuppressLint("SetJavaScriptEnabled")
	public WebViewBridge( WebView webView, Service primaryService ) {
		
		_webView = webView;
		_primaryService = primaryService;
		
        WebSettings webSettings = _webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setRenderPriority( RenderPriority.HIGH);

		_webView.setWebViewClient( this );

		_active = false;
		
	}
	
	
	
	@Override
	public void onLoadResource(WebView view, String url) {
		
		if( !_active ) {
			log.warn( "!_active" );
			return;
		}
		if( url.startsWith( BrokerJob.JSON_BROKER_SCHEME ) || url.startsWith( BrokerJob.JSON_BROKER_URI_ENCODED_SCHEME ) ) {
			BrokerJob job = new BrokerJob(url, true, _primaryService, this);
			WorkManager.enqueue( job );
			return;
		}
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		
		log.enteredMethod();

	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		
		log.enteredMethod();

	}


	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		
		log.enteredMethod();
		
		
		if( url.startsWith( "jsonbroker:" ) ) {
			if( _active ) {
				
				BrokerJob job = new BrokerJob(url, true, _primaryService, this);
				WorkManager.enqueue( job );
				
			} 
			// True if the host application wants to handle the key event itself, otherwise return false 
			return true;
		}
		
		return super.shouldOverrideUrlLoading(view, url);
		
	}
	
	
	
    public void onResume() {

    	log.enteredMethod();   	

    	if( _active ) {
    		return;
    	}

		_active = true;

        _webView.loadUrl("javascript:onResume();");
    }

    public void onPause() {
    	
    	log.enteredMethod();
    	
    	if( !_active ) {
    		return;
    	}
    	
		_active = false;

        _webView.loadUrl("javascript:onPause();");
    }

    
    
	class JavascriptPoster implements Runnable {
		String _javascript;
		
		JavascriptPoster( String javascript ) {
			_javascript = javascript;
		}

		@Override
		public void run() {
			
			if( _active ) {
				_webView.loadUrl(_javascript);
			}			
		}
	}

    
    
    private void postJavascript( String javascript ) {
    	
    	// vvv derived from <http://stackoverflow.com/questions/2848575/how-to-detect-gui-thread-on-android>
    	
    	if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
    		
    		_webView.loadUrl(javascript);
    		
    	} else {
    		
    		JavascriptPoster poster = new JavascriptPoster( javascript );
    		_handler.post( poster );
    		
    		
    	}
    	
    	
    	// ^^^ derived from <http://stackoverflow.com/questions/2848575/how-to-detect-gui-thread-on-android>
    	
    }

	@Override
	public void onFault(BrokerMessage request, Throwable fault) {
		
		
		if( !_active ) {
			log.warn( "!_active" );
			return;
		}
		String javascript = "javascript:" + JavascriptCallbackAdapterHelper.buildJavascriptFault(request , fault);
		postJavascript( javascript );
		
	}


	@Override
	public void onNotification(BrokerMessage notification) {
		
		if( !_active ) {
			log.warn( "!_active" );
			return;
		}

		String javascript = "javascript:" + JavascriptCallbackAdapterHelper.buildJavascriptNotification(notification);
		postJavascript( javascript );

		
	}


	@Override
	public void onResponse(BrokerMessage request, BrokerMessage response) {
		
		if( !_active ) {
			log.warn( "!_active" );
			return;
		}
		
		String javascript = "javascript:" + JavascriptCallbackAdapterHelper.buildJavascriptResponse(response);
		postJavascript( javascript );
	}
	

}
