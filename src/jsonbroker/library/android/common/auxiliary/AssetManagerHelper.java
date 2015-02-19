// Copyright (c) 2015 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.android.common.auxiliary;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

public class AssetManagerHelper {

	private static Log log = Log.getLog(AssetManagerHelper.class);

	public static final String[] list( AssetManager assetManager, String path, Object caller) {
		
		String[] answer;
		try {
			answer = assetManager.list( path );
		} catch (IOException e) {
			throw new BaseException( caller, e );
		}
		
		return answer;
		
	}
		
	
	public static boolean assetExists( AssetManager assetManager, String path, Object caller) {
		
		// vvv http://stackoverflow.com/questions/13364793/file-exists-returns-false-on-android

		File f = new File(path);
        String parent = f.getParent();
        if (parent == null) parent = "";
        String fileName = f.getName();
        
        // now use path to list all files
        String[] assetList;
		try {
			assetList = assetManager.list(parent);
		} catch (IOException e) {
			log.warnFormat( "caught exception: '%s'; path: '%s'", e.getMessage(), path );
			return false;
		}
        if (assetList != null && assetList.length > 0) {
            for (String item : assetList) {
                if (fileName.equals(item))
                    return true;
            }
        }
        
        return false;
		
		// ^^^ http://stackoverflow.com/questions/13364793/file-exists-returns-false-on-android
	}
	
	
	public static final AssetFileDescriptor openFd (AssetManager assetManager, String fileName, Object caller) {

		AssetFileDescriptor answer;
		try {
			answer = assetManager.openFd( fileName );
		} catch (IOException e) {
			throw new BaseException( caller, e );
		}
		
		return answer;

	}

	
	
	public static final InputStream open (AssetManager assetManager, String fileName, Object caller) {
		
		InputStream answer;
		try {
			answer = assetManager.open( fileName );
		} catch (IOException e) {
			throw new BaseException( caller, e );
		}
		
		return answer;

	}

	
	public static final InputStream open (AssetManager assetManager, String fileName, int accessMode, Object caller) {
		
		InputStream answer;
		try {
			answer = assetManager.open( fileName, accessMode );
		} catch (IOException e) {
			throw new BaseException( caller, e );
		}
		
		return answer;

	}
	
	
}
