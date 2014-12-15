package com.example.shareaview;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CompassModule implements SensorEventListener {
	private SensorManager mSensorManager;
	private float reading;
	private Context context;

	public CompassModule(Context context, Object service)
	{
		// initialize your android device sensor capabilities
		mSensorManager = (SensorManager) service;
		
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
		
		this.context = context;
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		reading = Math.round(event.values[0]);
		
		if(context instanceof ViewBrowseActivity)
		{
			((ViewBrowseActivity) context).showUSerDirection(reading);
		}
	}

	public float getSensorReading() {
		return reading;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// not in use
	}
}
