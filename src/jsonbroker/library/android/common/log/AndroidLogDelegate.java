// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.android.common.log;

import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.log.LogDelegate;
import jsonbroker.library.common.log.LogDelegateHelper;
import jsonbroker.library.common.log.Loggable;


public class AndroidLogDelegate implements LogDelegate {

	
	private boolean _isDebugEnabled; 
	
	public AndroidLogDelegate( boolean isDebugEnabled ) {
		_isDebugEnabled = isDebugEnabled;
		
	}
	
	////////////////////////////////////////////////////////////////////////////


	@Override
	public boolean isDebugEnabled() {
		return _isDebugEnabled;
	}


	////////////////////////////////////////////////////////////////////////////

	@Override
	public void debug(Log origin, String message) {
		if( _isDebugEnabled ) {
			String threadName = Thread.currentThread().getName();
			String methodName = LogDelegateHelper.getMethodName(origin);
			message = threadName + " [" + origin.getCallerClassName() + " " + methodName +"] " + message;
			
			android.util.Log.d( "jsonbroker", message );			
		}
	}
	
	

	@Override
	public void debug( Log origin, boolean value, String name ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}
	
	
	@Override
	public void debug( Log origin, int value, String name  ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}
	
	
	@Override
	public void debug( Log origin, Loggable value, String name  ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}
	
	@Override
	public void debug( Log origin, long value, String name  ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}

	@Override
	public void debug( Log origin, Object value, String name  ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}

	@Override
	public void debug( Log origin, String value, String name  ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}
	
	@Override
	public void debugIp4Address( Log origin, int value, String name ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}
	
	@Override
	public void debugFormat( Log origin, String format, Object ... args ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.formatString(format, args));
		}
	}

	////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void enteredMethod(Log origin) {
		if( _isDebugEnabled ) {
			debug( origin, "entered");
		}		
	}

	
	////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public void info(Log origin, String message) {
		if( _isDebugEnabled ) {
			String threadName = Thread.currentThread().getName();
			String methodName = LogDelegateHelper.getMethodName(origin);
			message = threadName + " [" + origin.getCallerClassName() + " " + methodName +"] " + message;
			
			android.util.Log.i( "jsonbroker", message );			
		}
	}

	////////////////////////////////////////////////////////////////////////////

	@Override
	public void warn(Log origin, String message) {
		if( _isDebugEnabled ) {
			String threadName = Thread.currentThread().getName();
			String methodName = LogDelegateHelper.getMethodName(origin);
			message = threadName + " [" + origin.getCallerClassName() + " " + methodName +"] " + message;
			
			android.util.Log.w( "jsonbroker", message );			
		}
	}
	
	////////////////////////////////////////////////////////////////////////////

	@Override
	public void error(Log origin, String message) {
		if( _isDebugEnabled ) {
			String threadName = Thread.currentThread().getName();
			String methodName = LogDelegateHelper.getMethodName(origin);
			message = threadName + " [" + origin.getCallerClassName() + " " + methodName +"] " + message;
			
			android.util.Log.e( "jsonbroker", message );			
		}
	}

	
	////////////////////////////////////////////////////////////////////////////




}
