/**
 * 
 */
package com.embarcados.activity;

import java.util.Set;

import com.embarcados.decibel_android.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author Mauricio L. Dau <mauricioldau@gmail.com>
 *
 */
public class AcquirementActivity extends Activity {

	public AcquirementActivity() {
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.acquirement_layout);
	}
	
	@Override
	protected void onStart() {

		super.onStart();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode) {
		
			case REQUEST_ENABLE_BT: {
			
				if(resultCode == RESULT_OK) {
					
					Toast.makeText(this, "Bluetooth ON", Toast.LENGTH_LONG).show();
					beginConnection();
					
				} else {
					
					Toast.makeText(this, "Bluetooth OFF", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
	
	private void beginConnection() {
		
		Set<BluetoothDevice> pairedDevices = this.bluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        
		    	if(device.getName().equals(DEVICE_NAME)) {

		    		device.
		    	}
		    }
		}
	}
}
