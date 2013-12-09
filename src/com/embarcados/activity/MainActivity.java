package com.embarcados.activity;

import com.embarcados.decibel_android.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button startBtn = (Button) this.findViewById(R.id.button1);
		startBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		
			case R.id.button1: {
			
				final Intent intent = new Intent(this, AcquirementActivity.class);
				this.startActivity(intent);
			}
		}
		
	}

}
