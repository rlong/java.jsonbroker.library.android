// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.android.server.broker;

import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.broker.DescribedService;
import jsonbroker.library.server.broker.Service;
import jsonbroker.library.server.broker.ServiceDescription;
import android.os.Handler;

public class GuiService implements DescribedService {
	
	private static Log log = Log.getLog( GuiService.class );
	

	public static final String SERVICE_NAME = "UserInterfaceService";
	public static final ServiceDescription SERVICE_DESCRIPTION = new ServiceDescription( SERVICE_NAME );


	////////////////////////////////////////////////////////////////////////////

	Service _delegate;
	
	Handler _handler; 
	
	
	////////////////////////////////////////////////////////////////////////////
	public GuiService( Service delegate ) {
		_delegate = delegate;
		_handler = new Handler();
	}



	
	@Override
	public BrokerMessage process(BrokerMessage request) {

		UserInterfaceRunner runner = new UserInterfaceRunner(request);
		synchronized (runner) {
			
			_handler.post(runner);
			
			while( null == runner._exception && null == runner._response ) {			
				try {
					runner.wait();
				} catch (InterruptedException e) {
					log.warn( e);
				}			
			}
			
			if( null != runner._exception ) {
				if( runner._exception instanceof BaseException ) { 
					throw (BaseException)runner._exception;
				}
				throw new BaseException( this, runner._exception);
			}
			
			return runner._response;
			
		}
		
	}
	
	@Override
	public ServiceDescription getServiceDescription() {
		return SERVICE_DESCRIPTION;
	}


	// runs in the context of the main UI thread
	class UserInterfaceRunner implements Runnable {
		
		BrokerMessage _request;
		
		Throwable _exception;
		
		BrokerMessage _response;
		
		UserInterfaceRunner( BrokerMessage request ) {
			_request = request;
		}

		@Override
		public synchronized void run() {
			
			try {
				_response = _delegate.process( _request );
				this.notify();
			} catch( Throwable t ) {
				_exception = t;
				this.notify();
			}
		}
	}


}
