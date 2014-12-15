package com.example.shareaview;

import java.io.File;

import com.google.android.gms.maps.model.LatLng;

public class ViewEntry {
	private LatLng coords;
	private String description;
	private float direction;
	private File image;
	
	public ViewEntry(LatLng coords, String description, float direction, File image)
	{
		this.coords = coords;
		this.description = description;
		this.direction = direction;
		this.image = (image);
	}

	public LatLng getCoords() {
		return coords;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return this.description;
	}

	public float getDirection() {
		// TODO Auto-generated method stub
		return direction;
	}

	public File getImage() {
		return image;
	}
}
