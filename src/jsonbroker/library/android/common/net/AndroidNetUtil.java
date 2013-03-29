// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.android.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.net.NetUtil;


import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class AndroidNetUtil extends NetUtil {

	private static final Log log = Log.getLog( AndroidNetUtil.class );
	
	Application _application;
	
	public AndroidNetUtil( Application application ) {
		_application = application;
	}


	@Override
	public String escapeString(String input) {
		
		return Uri.encode( input, _allowedChars );
		
	}

	@Override
	public InetAddress getWifiIpAddress() {
		
		
		WifiManager wifiManager = (WifiManager)_application.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		log.debug( ipAddress, "ipAddress");
		
		byte[] ip4Address = new byte[4];
		if( 0 == ipAddress ) {
			ip4Address[0] = 127;			
			ip4Address[1] = 0;
			ip4Address[2] = 0;
			ip4Address[3] = 1;
		} else {
			ip4Address[0] = (byte)(ipAddress & 0xFF);
			ip4Address[1] = (byte)((ipAddress >>8) & 0xFF);
			ip4Address[2] = (byte)((ipAddress >>16) & 0xFF);
			ip4Address[3] = (byte)((ipAddress >>24) & 0xFF);
		}
		
		
//		if( true ) {
//			log.warn( "true" );
//			ip4Address[0] = (byte)172;
//			ip4Address[1] = (byte)16;
//			ip4Address[2] = (byte)74;
//			ip4Address[3] = (byte)131;
//		}
		
		try {
			InetAddress answer = InetAddress.getByAddress( ip4Address );
			log.debug( answer.getHostAddress(), "answer.getHostAddress()" );
			return answer;
			
		} catch (UnknownHostException e) {
			throw new BaseException( this, e );
		}

	}
	
	
	
			
			

}
