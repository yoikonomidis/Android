package com.example.kidiyaservice;

import com.google.android.gms.maps.model.LatLng;

/**
 * Station object
 * @author Yannis
 *
 */
public class Station {

	private String m_id;
	private String m_name;
	private LatLng m_latlng;
	private double m_importanceWeight;
	
	public Station(String id, String name, double lat, double lng){
		setId(id);
		setName(name);
		setLatLng(new LatLng(lat, lng));
	}
	
	public String id(){return m_id;}
	
	public String name(){return m_name;}
	
	public LatLng latLng(){return m_latlng;}
	
	public double importanceWeight(){return m_importanceWeight;}
	
	public void setId(String id){m_id = id;}
	
	public void setName(String name){m_name = name;}
	
	public void setLatLng(LatLng latlng){m_latlng = latlng;}
	
	public void setImportanceWeight(double importanceWeight){m_importanceWeight = importanceWeight;}
}
