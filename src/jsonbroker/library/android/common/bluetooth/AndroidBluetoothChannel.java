package jsonbroker.library.android.common.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jsonbroker.library.common.bluetooth.BluetoothChannel;
import jsonbroker.library.common.bluetooth.BluetoothChannelHelper;
import jsonbroker.library.common.exception.BaseException;
import android.bluetooth.BluetoothSocket;

public class AndroidBluetoothChannel implements BluetoothChannel {
	
	
	BluetoothSocket _bluetoothSocket;
	InputStream _inputStream;
	OutputStream _outputStream;

	
	public AndroidBluetoothChannel( BluetoothSocket bluetoothSocket ) {
		
		
		
		_bluetoothSocket = bluetoothSocket;
		
		try {
			
			_bluetoothSocket.connect();

			_inputStream = _bluetoothSocket.getInputStream();
			_outputStream = _bluetoothSocket.getOutputStream();
		} catch (IOException e) {
			throw new BaseException( this, e );		
		}
	}
	

	@Override
	public void close() {
		try {
			
			_inputStream.close();
			_outputStream.close();
			_bluetoothSocket.close();
			
		} catch (IOException e) {
			
			throw new BaseException( this, e );			
		}
		
	}


	@Override
	public String readLine() {
		return BluetoothChannelHelper.readLine(_inputStream, this);
	}

	
	
	@Override
	public void write( String line ) {
		BluetoothChannelHelper.write( _outputStream, line);
	}

	@Override
	public void writeLine( String line ) {
		BluetoothChannelHelper.writeLine( _outputStream, line);
	}
	
	

}
