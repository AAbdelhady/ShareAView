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
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class CommunicationModule {

	private final double LATITUDE_RANGE  = 0.2;
	private final double LONGITUDE_RANGE = 0.4;
	private final String saveFile = "saveFile.csv";
	private Context context; 
	
	public CommunicationModule(Context context) {
		this.context = context;
		
		/*String tmp = "view test,58.3821141,26.7313681,122.0,/storage/emulated/0/Pictures/JPEG_20141214_133621_994044336.jpg,1418558220370;;Tartu Main hall ,58.3805251,26.7243583,138.0,/storage/emulated/0/Pictures/JPEG_20141215_163620_569444400.jpg,1418654160370;;In the library ,58.3766926,26.7206544,82.0,/storage/emulated/0/Pictures/JPEG_20141215_172551_569444400.jpg,1418657160370;;ayham we mohamed,58.3793273,26.7143398,57.0,/storage/emulated/0/Pictures/JPEG_20141216_120430_569444400.jpg,1418724240370;;my description ,58.3820923,26.7314372,191.0,/storage/emulated/0/Pictures/JPEG_20141216_153429_569444400.jpg,1418736900370;;Skype,59.3974969,24.661511,50.0,/storage/emulated/0/Pictures/JPEG_20141217_133517_-448357164.jpg,1418816100370;;Random Building ,59.3971528,24.6687754,93.0,/storage/emulated/0/Pictures/JPEG_20141217_143707_-448357164.jpg,1418819820370;;Forest in front of TTU,59.3967558,24.6723729,342.0,/storage/emulated/0/Pictures/JPEG_20141217_144008_1260021235.jpg,1418820000370;;lecture,59.3967226,24.66232,336.0,/storage/emulated/0/Pictures/JPEG_20141217_163320_-448357164.jpg,1418826780370;;ay 5ara,59.4366858,24.7689935,100.0,/storage/emulated/0/Pictures/JPEG_20141217_195343_-448357164.jpg,1418838780370";
		FileOutputStream fos;
		try {
			fos = context.openFileOutput(saveFile, Context.MODE_PRIVATE);
			fos.write(tmp.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public boolean uploadView(ViewEntry view)
	{
		String fileEntry = ";;"
						 + view.getDescription() + "," 
						 + view.getCoords().latitude + "," 
						 + view.getCoords().longitude + ","
						 + view.getDirection() + ","
						 + view.getImage().getAbsolutePath() + ","
						 + String.valueOf(view.getDateCreated().getTime());
		
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
				views.add(new ViewEntry(new LatLng(Double.parseDouble(entry[1]),Double.parseDouble(entry[2])), entry[0], Float.parseFloat(entry[3]), new File(entry[4]), new Date(Long.parseLong(entry[5]))));
		}
		
		
		return views;
	}
}
