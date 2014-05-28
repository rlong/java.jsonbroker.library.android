package jsonbroker.library.android.server.http.request_handler;

import java.io.InputStream;

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
import android.app.Service;
import android.content.Context;
import android.content.res.AssetManager;

public class AssetRequestHandler implements RequestHandler {

	private static Log log = Log.getLog(AssetRequestHandler.class);

	
	////////////////////////////////////////////////////////////////////////////	
	//
	Context _context;

	
	
	////////////////////////////////////////////////////////////////////////////
	private String _rootFolder;

	////////////////////////////////////////////////////////////////////////////

	
	public AssetRequestHandler( String rootFolder ) {		
		_rootFolder = rootFolder;
		
//		Resources.getSystem().getAssets().openFd( )
//		
//		File root = new File( rootFolder );
//		if( !root.exists() ) {
//			log.warnFormat( "!root.exists(); rootFolder = %s", rootFolder);
//		}
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
	
	
	
	
	private Entity tryReadFile( Context context, String relativePath  ) {
		
        String absoluteFilename = _rootFolder + relativePath;
        
        log.debug( absoluteFilename, "absoluteFilename");
        
        

		AssetManager assetManager = context.getResources().getAssets();

		InputStream inputStream = AssetManagerHelper.open( assetManager, absoluteFilename, this);
		
		long length = InputStreamHelper.available( inputStream, this );
		log.debug( length, "length" );
		
//		
//		long length;
//		{
//			AssetFileDescriptor fileDescriptor = AssetManagerHelper.openFd( assetManager, absoluteFilename, this );
//			length = fileDescriptor.getLength();
//			log.debug( length, "length" );
//			AssetFileDescriptorHelper.close( fileDescriptor, this);
//		}
//		
//		
//		InputStream inputStream = AssetManagerHelper.open(assetManager, absoluteFilename, AssetManager.ACCESS_RANDOM, this);
//		
		
		 
		Entity answer = new StreamEntity( inputStream, length );
		return answer;
		
	}
	
	private Entity readFile( Context context, String relativePath  ) {

        
		try {
			
			return tryReadFile( context, relativePath);
			
		} catch( BaseException e ) {
			log.warn( e.getMessage() );
			throw HttpErrorHelper.notFound404FromOriginator( this );
		}		

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

		
        try
        {
        	
        	Entity body = readFile( _context, requestUri );        	
            HttpResponse answer = new HttpResponse( HttpStatus.OK_200, body );
            String contentType = MimeTypes.getMimeTypeForPath(requestUri);
            answer.setContentType( contentType );
            return answer;
            
        } catch( BaseException e ) {
        	throw e;
        } catch( Throwable t ) {        	
        	log.error(t.getMessage() ); 
        	throw HttpErrorHelper.notFound404FromOriginator(this);        	
        }
	}
	
	
    public void onCreate( Context context ) {
    	
    	_context = context;
        
    }
    
    public void onDestroy() {
    	
    	_context = null;
    	
    }


}
