package com.example.shareaview;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MapActivity extends Activity implements OnMarkerClickListener {

	LocationModule locMod;
	CommunicationModule comMod;
	ArrayList<Marker> markerList;
	ArrayList<ViewEntry> nearbyViews;
	
	private GoogleMap googleMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		locMod = new LocationModule(this);
		comMod = new CommunicationModule(this);
		markerList = new ArrayList<Marker>();
		
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMap();
		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		
		googleMap.setOnMarkerClickListener(this);
		
		if(locMod.getCurrentLocation()!=null)
			markNearbyViews(locMod.getCurrentLocation());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void markNearbyViews(LatLng currentLocation)
	{
		nearbyViews = comMod.getNearbyViews(currentLocation);
		
		googleMap.clear();
		markerList.clear();
		
		for(int i=0; i<nearbyViews.size();i++)
		{
			MarkerOptions MyLocMarker = new MarkerOptions().position(nearbyViews.get(i).getCoords());
			Marker marker = googleMap.addMarker(MyLocMarker);
			markerList.add(marker);
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		Intent openView = new Intent(this,ViewBrowseActivity.class);
		
		ViewEntry view = nearbyViews.get(markerList.indexOf(arg0));
		
		openView.putExtra("ViewDescription", view.getDescription());
		openView.putExtra("ViewLatitude", String.valueOf(view.getCoords().latitude));
		openView.putExtra("ViewLongitude", String.valueOf(view.getCoords().longitude));
		openView.putExtra("ViewDirection", String.valueOf(view.getDirection()));
		openView.putExtra("UserLatitude", String.valueOf(locMod.getCurrentLocation().latitude));
		openView.putExtra("UserLongitude", String.valueOf(locMod.getCurrentLocation().longitude));
		openView.putExtra("ViewDateCreated", String.valueOf(view.getDateCreated().toLocaleString()));
		
		if(view.getImage()!=null)
		{
			openView.putExtra("ViewImagePath", view.getImage().getAbsolutePath());
		}
		else
		{
			openView.putExtra("ViewImagePath", "NO_IMAGE");
		}
		
		startActivity(openView);
		
		return false;
	}
	
	public void focusOnLocation()
	{
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locMod.getCurrentLocation(), 12.0f));
	}
}
