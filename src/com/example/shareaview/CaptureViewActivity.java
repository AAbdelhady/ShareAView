package com.example.shareaview;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CaptureViewActivity extends Activity {

	static final int REQUEST_TAKE_PHOTO = 1;
	
	LocationModule locMod;
	CameraModule camMod;
	CommunicationModule comMod;
	CompassModule compassMod;
    File photoFile = null;
    
    EditText desc;
    String description = "";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture_view);
		
		locMod = new LocationModule(this);
		camMod = new CameraModule();
		comMod = new CommunicationModule(this);
		compassMod = new CompassModule(this,getSystemService(SENSOR_SERVICE));
		
		desc = (EditText) findViewById(R.id.editText1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.capture_view, menu);
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
	
	public void dispatchCamApp(View view) {
		description = desc.getText().toString();
		
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
		    try {
		            photoFile = camMod.createImageFile();
		    } catch (IOException ex) {
		            // Error occurred while creating the File
		    }
		    
		    // Continue only if the File was successfully created
		    if (photoFile != null) {
		    	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
		                    Uri.fromFile(photoFile));
		        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
		    }
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
	    	ViewEntry viewRecord = new ViewEntry(locMod.getCurrentLocation(), 
	    			                             description, 
	    			                             compassMod.getSensorReading(), 
	    			                             photoFile);
	    	comMod.uploadView(viewRecord);
	    	
	    	Toast.makeText(this, "Your view has been recorded", Toast.LENGTH_SHORT).show();
	    	
	    	Intent i = new Intent(this, MainActivity.class);
	    	startActivity(i); 
	    }
	}
}
