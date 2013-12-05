/**
 * 
 */
package com.embarcados.activity;

import com.embarcados.bluetooth.BluetoothReceiverFactory;
import com.embarcados.bluetooth.IBluetoothReceiveListener;
import com.embarcados.bluetooth.IBluetoothReceiver;
import com.embarcados.decibel_android.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author Mauricio L. Dau <mauricioldau@gmail.com>
 *
 */
public class AcquirementActivity extends Activity implements IBluetoothReceiveListener {

	private final IBluetoothReceiver bluetoothReceiver;
	
	private TextView valueTextView;
	
	public AcquirementActivity() {
		
		this.bluetoothReceiver = BluetoothReceiverFactory.newFakeBTReceiver();
		
		this.bluetoothReceiver.setOnBluetoothReceiveListener(this);
		this.bluetoothReceiver.setContext(this);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.acquirement_layout);
		
		this.valueTextView = (TextView) this.findViewById(R.id.textView1);
	}

	@Override
	protected void onStart() {

		super.onStart();
		
		this.bluetoothReceiver.start();
	}
	
	@Override
	protected void onStop() {
		
		super.onStop();
		
		this.bluetoothReceiver.stop();
	}
	
	@Override
	protected void onDestroy() {

		super.onDestroy();
		
		this.bluetoothReceiver.close();
	}
	
	@Override
	public void onNewValue(int newValue) {

		this.valueTextView.setText(String.valueOf(newValue));
	}
	
}
