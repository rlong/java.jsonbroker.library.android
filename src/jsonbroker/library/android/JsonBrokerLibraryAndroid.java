// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.android;

import jsonbroker.library.android.common.log.AndroidLogDelegate;
import jsonbroker.library.android.common.net.AndroidNetUtil;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.net.NetUtil;
import android.app.Application;


public class JsonBrokerLibraryAndroid {
	
	public static void setup(Application application, boolean isDebugEnabled) {
	
		String tag = application.getPackageName();
		Log.setDelegate( new AndroidLogDelegate( isDebugEnabled, tag ) ); 
		
		NetUtil.setInstance( new AndroidNetUtil( application ) );
	}

}
