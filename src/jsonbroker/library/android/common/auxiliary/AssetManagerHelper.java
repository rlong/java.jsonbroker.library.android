package jsonbroker.library.android.common.auxiliary;

import java.io.IOException;
import java.io.InputStream;

import jsonbroker.library.common.exception.BaseException;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

public class AssetManagerHelper {

	
	public static final String[] list( AssetManager assetManager, String path, Object caller) {
		
		String[] answer;
		try {
			answer = assetManager.list( path );
		} catch (IOException e) {
			throw new BaseException( caller, e );
		}
		
		return answer;
		
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
