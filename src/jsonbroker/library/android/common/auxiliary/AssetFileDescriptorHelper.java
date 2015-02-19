// Copyright (c) 2015 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.android.common.auxiliary;

import java.io.FileInputStream;
import java.io.IOException;

import jsonbroker.library.common.exception.BaseException;

import android.content.res.AssetFileDescriptor;

public class AssetFileDescriptorHelper {

	
	public static void close( AssetFileDescriptor assetFileDescriptor, Object caller ) {
		
		try {
			assetFileDescriptor.close();
		} catch (IOException e) {
			throw new BaseException( caller, e);
		}
		
	}
	
	
	public static FileInputStream createInputStream(AssetFileDescriptor assetFileDescriptor, Object caller ) {

		try {
			return assetFileDescriptor.createInputStream();
		} catch (IOException e) {
			throw new BaseException( caller, e);
		}

	}
	
	
}
