package com.example.kidiyaservice;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class ApplicationSettings {

	private static ApplicationSettings m_instance;
	private static SupportMapFragment m_mapFragment;
	private static GoogleMap m_googleMap;
	
	private ApplicationSettings(){
	}
	
	/**
	 * Returns the existing ApplicationSettings object if exists, otherwise it creates a new ApplicationSettings instance
	 * @return the Settings instance
	 */
	public static ApplicationSettings instance(){
		if(m_instance == null)
		{
			m_instance = new ApplicationSettings();
		}
		return m_instance;
	}

	/**
	 * Creates the SupportMapFragment object and its map
	 * @param fragmentActivity the activity that contains the map view
	 * @return the SupportMapFragment object
	 */
	public SupportMapFragment mapFragment(FragmentActivity fragmentActivity){
		
		//TODO: Investigate the possibility of creating only one SupportMapFragment object and retain it after a FragmentActivity is destroyed
		m_mapFragment = (SupportMapFragment) fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.map);
		m_mapFragment.setRetainInstance(true);

		//TODO: Investigate the possibility of creating only one GoogleMap object and retain it after a FragmentActivity is destroyed
		m_googleMap = m_mapFragment.getMap();
		
		return m_mapFragment;
	}
	
	/**
	 * Returns the google map of the MapFragment
	 * @return the fragment's google map
	 */
	public GoogleMap googleMap(){
		return m_googleMap;
	}
	
	
}
