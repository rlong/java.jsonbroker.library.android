// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.android.common.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jsonbroker.library.common.auxiliary.InputStreamHelper;

import jsonbroker.library.common.channel.Channel;
import jsonbroker.library.common.channel.ChannelHelper;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;
import android.bluetooth.BluetoothSocket;

public class AndroidBluetoothChannel implements Channel {
	
	
	private static Log log = Log.getLog( AndroidBluetoothChannel.class );

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
	public void close( boolean ignoreErrors ) {
		try {
			
			_inputStream.close();
			_outputStream.close();
			_bluetoothSocket.close();
			
		} catch (IOException e) {
			
			if( ignoreErrors ) {
				log.warn( e );
			} else {
				throw new BaseException( this, e );
			}
						
		}
		
	}

	@Override
	public void flush() {
		
		try {
			_outputStream.flush();
		} catch (IOException e) {
			throw new BaseException( this, e );
		}
		
	}

	@Override
	public String readLine() {
		return ChannelHelper.readLine(_inputStream, this);
	}

	
	@Override
	public void write( byte[] bytes ) {
		ChannelHelper.write( bytes, _outputStream );		
	}
	
	@Override
	public void write( InputStream inputStream ) {			
		InputStreamHelper.write( inputStream, _outputStream );
	}

	@Override
	public void write( String line ) {
		ChannelHelper.write( line, _outputStream);
	}

	@Override
	public void writeLine( String line ) {
		ChannelHelper.writeLine( line, _outputStream);
	}


	
	

}
