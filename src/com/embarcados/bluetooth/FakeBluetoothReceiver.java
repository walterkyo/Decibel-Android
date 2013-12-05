/**
 * 
 */
package com.embarcados.bluetooth;

import java.util.Random;

import android.os.AsyncTask;

/**
 * @author Mauricio Dau mauricio.dau@endeeper.com
 *
 */
class FakeBluetoothReceiver extends AbstractBluetoothReceiver {

	private FakeValueGenerator generatorThread;
	
	public FakeBluetoothReceiver() {
		
		this.generatorThread = new FakeValueGenerator();
	}
	
	@Override
	public void onStart() {

		this.generatorThread.execute("");
	}

	@Override
	public void onStop() {


	}
	
	private class FakeValueGenerator extends AsyncTask<String, Integer, Void> {
		
		private static final int FAKE_GENERATION_INTERVAL = 1000;

		@Override
		protected Void doInBackground(String... params) {

			final Random rand = new Random();
			
			while(isRunning) {
				
				try {
					
					Thread.sleep(FAKE_GENERATION_INTERVAL);
					
					publishProgress(rand.nextInt(MAXIMUM_DECIBEL_VALUE));
					
				} catch (InterruptedException e) { }
			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			
			listener.onNewValue(values[0]);
		}
	}

	@Override
	public void close() {

		this.generatorThread = null;
	}

}
