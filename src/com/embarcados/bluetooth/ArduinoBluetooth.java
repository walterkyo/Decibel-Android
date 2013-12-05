/**
 * 
 */
package com.embarcados.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author Mauricio L. Dau <mauricioldau@gmail.com>
 *
 *	Class with static methods to handle the bluetooth connection with the Arduino.
 *
 */
class ArduinoBluetooth extends AbstractBluetoothReceiver {

	private static final int REQUEST_ENABLE_BT = 1;
	private static final int DEVICE_PIN = 1234;
	private static final String DEVICE_NAME = "BT UART";
	
	private static final int MESSAGE_LENGTH = 3;
	
	private static final String ANDROID_NAME = "ANDROID_SERVER";
	private static final UUID ANDROID_UUID = UUID.randomUUID();
	
	private  BluetoothAdapter bluetoothAdapter;
	
	private  Context context;
	
	private static ArduinoBluetooth INSTANCE;
	
	public boolean isRunning = false;
	
	private IBluetoothReceiveListener listener;
	
	public void setBluetoothListener(IBluetoothReceiveListener listener) {
		
		this.listener = listener;
	}
	
	public ArduinoBluetooth() {
		
	}
	
	private ArduinoBluetooth(Context context) {
		
		this.context = context;
		
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if(this.bluetoothAdapter == null) {
			
			Toast.makeText(this.context, "Device does not support bluetooth", Toast.LENGTH_LONG).show();
			System.exit(-1);
		}
	}
	
	public static ArduinoBluetooth getInstance(Context context) {
		
		if(INSTANCE == null) {
			
			INSTANCE = new ArduinoBluetooth(context);
		}
		
		return INSTANCE;
	}
	
	public static void enableDiscoverability(Activity activity) {
		
		Intent discoverableIntent = new
		Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		activity.startActivity(discoverableIntent);
	}
	
	private class AcceptThread extends Thread {
		
	    private final BluetoothServerSocket mmServerSocket;
	 
	    public AcceptThread() {
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(ANDROID_NAME, ANDROID_UUID);
	        } catch (IOException e) { }
	        mmServerSocket = tmp;
	    }
	 
	    public void run() {
	        BluetoothSocket socket = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {
	            try {
	                socket = mmServerSocket.accept();
	            } catch (IOException e) {
	                break;
	            }
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
	            	final ReceiveBluetooth receiveThread = new ReceiveBluetooth(socket);
	            	receiveThread.start();
	            	
	                cancel();
	                break;
	            }
	        }
	    }
	 
	    /** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	private class ReceiveBluetooth extends Thread {
		
		private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
		
		public ReceiveBluetooth(BluetoothSocket socket) {
			
			this.mmSocket = socket;
			InputStream temp = null;
			
			try {
				temp= this.mmSocket.getInputStream();
			} catch (IOException e) { }
			
			this.mmInStream = temp;
		}
		
		@Override
		public void run() {

			super.run();
			
			byte[] buffer = new byte[3];
			
			while(ArduinoBluetooth.this.isRunning) {
				
				try {
					
					mmInStream.read(buffer);
					
					String newValue = new String(buffer).replace("\n", "");
					
					listener.onNewValue(Integer.valueOf(newValue));
					
				} catch (IOException e) { }
			}
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
