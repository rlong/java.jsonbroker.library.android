// Copyright (c) 2015 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.android.server.http.request_handler;

import java.io.InputStream;

import jsonbroker.library.android.BuildConfig;
import jsonbroker.library.android.common.auxiliary.AssetManagerHelper;
import jsonbroker.library.common.auxiliary.InputStreamHelper;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.HttpStatus;
import jsonbroker.library.common.http.StreamEntity;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.http.HttpErrorHelper;
import jsonbroker.library.server.http.HttpRequest;
import jsonbroker.library.server.http.HttpResponse;
import jsonbroker.library.server.http.MimeTypes;
import jsonbroker.library.server.http.RequestHandler;
import jsonbroker.library.server.http.reqest_handler.RequestHandlerHelper;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;

public class AssetRequestHandler implements RequestHandler {

	private static Log log = Log.getLog(AssetRequestHandler.class);

	
	////////////////////////////////////////////////////////////////////////////	
	//
	Context _context;

	
	
	////////////////////////////////////////////////////////////////////////////
	private String _rootFolder;

	
	////////////////////////////////////////////////////////////////////////////
	private static String _quotedETag;

	
	////////////////////////////////////////////////////////////////////////////

	
	public AssetRequestHandler( String rootFolder ) {		
		_rootFolder = rootFolder;
		
	}
	
	
	private static String getQuotedETag( Context context ) {
		
		if( null != _quotedETag ) {
			return _quotedETag;
		}
		
		if( BuildConfig.DEBUG ) {
			 
			_quotedETag = "\"" + System.currentTimeMillis() + "\"";
			log.infoFormat( "BuildConfig.DEBUG; _quotedETag = '%s'", _quotedETag );
			return _quotedETag;
		}
		
		// vvv http://stackoverflow.com/questions/4616095/how-to-get-the-build-version-number-of-your-android-application
		
		PackageManager packageManager = context.getPackageManager();
		String packageName = context.getPackageName();
		log.debug( packageName, "packageName" );
		int flags = 0;
		
		
		try {

			PackageInfo packageInfo = packageManager.getPackageInfo( packageName, flags);
			_quotedETag = "\"" + packageInfo.versionName + "\"";
			
			
		} catch (NameNotFoundException e) {
			log.error( e );
			// better than nothing ...  it will at least be valid as long as this process is running
			_quotedETag = "\"" + System.currentTimeMillis() + "\"";
			
		}
		
		// ^^^ http://stackoverflow.com/questions/4616095/how-to-get-the-build-version-number-of-your-android-application
		
		log.debug( _quotedETag, "_quotedETag" );
		return _quotedETag; 
		
	}

	
	
	public void validate( ) {
		
		if( null == _context ) {
			log.warn( "null == _service" );
			return;
		}
		
		AssetManager assetManager = _context.getResources().getAssets();
		
		try {
			
			String list[] = AssetManagerHelper.list( assetManager, _rootFolder, this);
			
			if( null == list ) {
				log.warn( "null == list" );
			} else if( 0 == list.length ) {
				log.warn( "0 == list.length" );
			} else {
				log.debug( list.length, "list.length" );
			}
			
		} catch (BaseException e) {
			log.warn( e );
		}
		
	}
	
	
	
	private Entity tryReadFile( AssetManager assetManager, String path  ) {
		
        
		InputStream inputStream = AssetManagerHelper.open( assetManager, path, this);
		
		long length = InputStreamHelper.available( inputStream, this );
		
		Entity answer = new StreamEntity( inputStream, length );
		return answer;
		
	}
	
		
	@Override
	public String getProcessorUri() {
		return "/";
	}

	@Override
	public HttpResponse processRequest(HttpRequest request) {

		
		if( null == _context ) {
			throw HttpErrorHelper.notFound404FromOriginator(this);
		}
		
		
		String requestUri = request.getRequestUri();
		
		if( requestUri.endsWith( "/" ) ) {
			requestUri = requestUri + "index.html";
		}
		
		requestUri = RequestHandlerHelper.removeUriParameters( requestUri );
		
		
		{ // some validation 
			
			RequestHandlerHelper.validateRequestUri( requestUri );
			RequestHandlerHelper.validateMimeTypeForRequestUri( requestUri );			
		}

        String path = _rootFolder + requestUri;        

		AssetManager assetManager = _context.getResources().getAssets();

        if( !AssetManagerHelper.assetExists( assetManager, path,  this) ) {
        	
			throw HttpErrorHelper.notFound404FromOriginator( this );        	
        }

        
        HttpResponse answer;
        
		
		// we know the file exists, but does the client already have a cache of it ...
    	String eTag = getQuotedETag( _context );
    	String ifNoneMatch = request.getHttpHeader( "if-none-match" );
    	if(  null != ifNoneMatch && eTag.equals(ifNoneMatch) ) {
    		answer = new HttpResponse( HttpStatus.NOT_MODIFIED_304 );
    	} else {
    		
            try
            {
            	
            	Entity body = tryReadFile( assetManager, path );        	
                answer = new HttpResponse( HttpStatus.OK_200, body );
                String contentType = MimeTypes.getMimeTypeForPath(requestUri);
                answer.setContentType( contentType );
                
            } catch( BaseException e ) {
            	throw e;
            } catch( Throwable t ) {        	
            	log.error( t.getMessage() ); 
            	throw HttpErrorHelper.internalServerError500FromOriginator( this );        	
            }
    	}
    	
		answer.putHeader( "ETag", eTag );
		return answer;

		
	}
	
	
    public void onCreate( Context context ) {
    	
    	_context = context;
        
    }
    
    public void onDestroy() {
    	
    	_context = null;
    	
    }


}
