package com.example.shareaview;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewBrowseActivity extends Activity {

	CompassModule compassMod;
	TextView userDir;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_browse);
		
		compassMod = new CompassModule(this,getSystemService(SENSOR_SERVICE));
		
		TextView desc = (TextView) findViewById(R.id.view_description);
		desc.setText("Description:\n\t"+getIntent().getStringExtra("ViewDescription"));
		
		TextView lat = (TextView) findViewById(R.id.view_lat);
		lat.setText("Latitude:\n\t"+getIntent().getStringExtra("ViewLatitude"));
		
		TextView lng = (TextView) findViewById(R.id.view_long);
		lng.setText("Longitude:\n\t"+getIntent().getStringExtra("ViewLongitude"));
		
		TextView viewDir = (TextView) findViewById(R.id.view_direction);
		viewDir.setText("Direction to Face:\n\t"+getIntent().getStringExtra("ViewDirection"));
		
		TextView viewDate = (TextView) findViewById(R.id.view_date_Created);
		viewDate.setText("Capture Date:\n\t"+getIntent().getStringExtra("ViewDateCreated"));
		
		userDir = (TextView) findViewById(R.id.user_direction);
		
		final ImageView viewImg = (ImageView) findViewById(R.id.view_image);
		
		if(getIntent().getStringExtra("ViewImagePath").equals("NO_IMAGE"))
		{
			ColorDrawable cd = new ColorDrawable(Color.GRAY);
			viewImg.setImageDrawable(cd);
		}
		else
		{
			// Get the dimensions of the View
	        int targetW = 1080/4;
	        int targetH = 1920/4;
			
			// Get the dimensions of the bitmap
	        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	        bmOptions.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(getIntent().getStringExtra("ViewImagePath"), bmOptions);
	        int photoW = bmOptions.outWidth;
	        int photoH = bmOptions.outHeight;
	        
	        // Determine how much to scale down the image
	        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
	        
	        // Decode the image file into a Bitmap sized to fill the View
	        bmOptions.inJustDecodeBounds = false;
	        bmOptions.inSampleSize = scaleFactor;
	        bmOptions.inPurgeable = true;
			
			Bitmap imageBitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("ViewImagePath"), bmOptions);
			viewImg.setImageBitmap(imageBitmap);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_browse, menu);
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
	
	public void showUSerDirection(float reading)
	{
		userDir.setText("Direction Facing:\n\t"+String.valueOf(reading));
		
		float viewDir = Float.valueOf(getIntent().getStringExtra("ViewDirection"));
		
		if((reading > viewDir-3)&&(reading < viewDir+3))
		{
			userDir.setTextColor(Color.GREEN);
		}
		else
		{
			userDir.setTextColor(Color.RED);
		}
	}
	
	public void getDirections(View view)
	{
		
		String userLat = getIntent().getStringExtra("UserLatitude");
		String userLng = getIntent().getStringExtra("UserLongitude");
		
		String viewLat = getIntent().getStringExtra("ViewLatitude");
		String viewLng = getIntent().getStringExtra("ViewLongitude");
		
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
			    Uri.parse("http://maps.google.com/maps?saddr="+userLat+","+userLng+"&daddr="+viewLat+","+viewLng));
		startActivity(intent);
	}
}
