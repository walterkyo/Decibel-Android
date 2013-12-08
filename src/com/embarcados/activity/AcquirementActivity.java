/**
 * 
 */
package com.embarcados.activity;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.embarcados.bluetooth.BluetoothReceiverFactory;
import com.embarcados.bluetooth.IBluetoothReceiveListener;
import com.embarcados.bluetooth.IBluetoothReceiver;
import com.embarcados.decibel_android.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * @author Mauricio L. Dau <mauricioldau@gmail.com>
 *
 */
public class AcquirementActivity extends Activity implements IBluetoothReceiveListener {

	private final IBluetoothReceiver bluetoothReceiver;
	
	private TextView valueTextView;
	private TimeSeries dataset;
	private XYMultipleSeriesDataset mDataset;
	private XYSeriesRenderer renderer;
	private XYMultipleSeriesRenderer mRenderer;
	private GraphicalView mChartView;
	Calendar calendar = new GregorianCalendar();
	
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

		//Grafico dinamico
		dataset = new TimeSeries("Valores");
		mDataset = new XYMultipleSeriesDataset();
		
		renderer = new XYSeriesRenderer();
		mRenderer = new XYMultipleSeriesRenderer();
		
		mDataset.addSeries(dataset);
		
		renderer.setColor(Color.GREEN);
		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setFillPoints(true);
		
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setXTitle("Segundos");
		mRenderer.setYTitle("Decibeis");
		mRenderer.setAxisTitleTextSize(20);
	    //mRenderer.setYAxisMin(30);
	    //mRenderer.setYAxisMax(80);
		mRenderer.setShowGrid(true);
		mRenderer.setGridColor(Color.DKGRAY);
		
		mRenderer.addSeriesRenderer(renderer);
		
	}

	@Override
	protected void onStart() {

		super.onStart();
		
		this.bluetoothReceiver.start();
		
		if (mChartView == null) {
		    LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
		    mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
		    layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		  } else {
		    mChartView.repaint();
		}
		
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
		
		dataset.add(calendar.get(Calendar.SECOND),newValue);
		mChartView.repaint();
		this.valueTextView.setText(String.valueOf(newValue));
	}
	
}
