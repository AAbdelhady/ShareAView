package com.example.shareaview;

import java.io.File;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

public class ViewEntry {
	private LatLng coords;
	private String description;
	private float direction;
	private File image;
	private Date dateCreated;
	

	public ViewEntry(LatLng coords, String description, float direction, File image, Date dateCreated)
	{
		this.coords = coords;
		this.description = description;
		this.direction = direction;
		this.image = (image);
		this.dateCreated  = dateCreated;
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

	public Date getDateCreated() {
		return dateCreated;
	}
}
