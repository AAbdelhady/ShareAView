package com.example.shareaview;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

public class LocationModule implements GooglePlayServicesClient.ConnectionCallbacks,OnConnectionFailedListener,LocationListener {

	private Location currentLocation;
	private LocationClient mLocationClient;
	boolean mUpdatesRequested;
	private Context context;
	
	private static final int MILLISECONDS_PER_SECOND = 1000;// Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;// Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;// The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;// A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
   
    // Define an object that holds accuracy and frequency parameters
    LocationRequest mLocationRequest;
	
	public LocationModule(Context context)
	{
		this.context = context;
		
		if(!isLocationEnabled(context))
		{
			Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			context.startActivity(myIntent);
		}
		
		int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if(resp == ConnectionResult.SUCCESS){
			mLocationClient = new LocationClient(context,this,this);
			mLocationClient.connect();
		}
		
		mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mUpdatesRequested = true;//enable updates
	}
	
	public LatLng getCurrentLocation()
	{
		if(currentLocation!=null)
		{
			return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		}
		else
		{
			Toast.makeText(context, "waiting for current location", Toast.LENGTH_SHORT).show();
			return null;
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		while(currentLocation==null)
			currentLocation = mLocationClient.getLastLocation();
		
		if(context instanceof MapActivity)
		{
			((MapActivity) context).markNearbyViews(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));
			((MapActivity) context).focusOnLocation();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		if (mUpdatesRequested) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }

		currentLocation = mLocationClient.getLastLocation();
		
		if(currentLocation==null)
		{
			Toast.makeText(context, "waiting for current location", Toast.LENGTH_SHORT).show();
		}
		else
		{
			if(context instanceof MapActivity)
			{
				((MapActivity) context).markNearbyViews(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));
				((MapActivity) context).focusOnLocation();
			}
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
	public static boolean isLocationEnabled(Context context) {
		int locationMode = 0;
		String locationProviders;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			try {
				locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}

			return locationMode != Settings.Secure.LOCATION_MODE_OFF;

		}else{
			locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			return !TextUtils.isEmpty(locationProviders);
		}
	} 
}
