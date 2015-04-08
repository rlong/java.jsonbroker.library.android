// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.android.server.broker;

import jsonbroker.library.broker.BrokerMessage;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.broker.server.DescribedService;
import jsonbroker.library.broker.server.ServiceDescription;
import android.os.Handler;

public class MainThreadServiceDelegator implements DescribedService {
	
	private static Log log = Log.getLog( MainThreadServiceDelegator.class );
	

	////////////////////////////////////////////////////////////////////////////

	DescribedService _delegate;
	
	Handler _handler; 
	
	
	////////////////////////////////////////////////////////////////////////////
	public MainThreadServiceDelegator( DescribedService delegate ) {
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
	
//	@Override
//	public ServiceDescription getServiceDescription() {
//		return _delegate.getServiceDescription();
//	}


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

	@Override
	public ServiceDescription getServiceDescription() {
		return _delegate.getServiceDescription();
	}


}
