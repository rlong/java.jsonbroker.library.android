// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.android.common;

import jsonbroker.library.android.common.net.AndroidNetUtil;
import jsonbroker.library.common.net.NetUtil;
import android.app.Application;


public class FrameworkAndroidCommon {
	
	public static void setup(Application application) {
	
		
		NetUtil.setInstance( new AndroidNetUtil( application ) );
	}

}
