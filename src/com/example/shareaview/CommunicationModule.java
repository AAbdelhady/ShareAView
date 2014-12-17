package com.example.shareaview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class CommunicationModule {

	private final double LATITUDE_RANGE  = 0.2;
	private final double LONGITUDE_RANGE = 0.4;
	private final String saveFile = "saveFile.csv";
	private Context context; 
	
	public CommunicationModule(Context context) {
		this.context = context;
	}

	public boolean uploadView(ViewEntry view)
	{
		String fileEntry = ";;"
						 + view.getDescription() + "," 
						 + view.getCoords().latitude + "," 
						 + view.getCoords().longitude + ","
						 + view.getDirection() + ","
						 + view.getImage().getAbsolutePath();
		
		return appendToFile(fileEntry);
	}
	
	private boolean appendToFile(String fileEntry) {
		
		String fileContent = "";
		
		File tmp = new File(saveFile);
		
		fileContent = readFile();
	
		
		fileContent += fileEntry;
		
		FileOutputStream fos;
		try {
			fos = context.openFileOutput(saveFile, Context.MODE_PRIVATE);
			fos.write(fileContent.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private String readFile() {
		try {
	        FileInputStream fis = context.openFileInput(saveFile);

	        if ( fis != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(fis);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            fis.close();
	            return stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }
		return null;
	}
	
	private boolean withinRange(double curLat, double curLng, double viewLat, double viewLng)
	{
		if((curLat+LATITUDE_RANGE>viewLat)&&(curLat-LATITUDE_RANGE<viewLat))
		{
			if((curLng+LONGITUDE_RANGE>viewLng)&&(curLng-LONGITUDE_RANGE<viewLng))
			{
				return true;
			}
		}
		return false;
	}
	

	public ArrayList<ViewEntry> getNearbyViews(LatLng currentLocation)
	{
		ArrayList<ViewEntry> views = new ArrayList<ViewEntry>();
		
		double curLat = currentLocation.latitude;
		double curLng = currentLocation.longitude;
		/*
		views.add(new ViewEntry(new LatLng(curLat+0.02,curLng+0.04), "View1", 50f, null));
		views.add(new ViewEntry(new LatLng(curLat-0.02,curLng+0.04), "View2", 100f, null));
		views.add(new ViewEntry(new LatLng(curLat+0.2,curLng-0.4), "View3", 150f, null));
		views.add(new ViewEntry(new LatLng(curLat-0.2,curLng-0.4), "View4", 200f, null));*/
		
		String savedViews = readFile();
		
		String[] viewEntries = savedViews.split(";;");
		
		for(int i=0;i<viewEntries.length;i++)
		{
			String[] entry = viewEntries[i].split(",");
			if(withinRange(curLat,curLng,Double.parseDouble(entry[1]),Double.parseDouble(entry[2])))
				views.add(new ViewEntry(new LatLng(Double.parseDouble(entry[1]),Double.parseDouble(entry[2])), entry[0], Float.parseFloat(entry[3]), new File(entry[4])));
		}
		
		
		return views;
	}
}
