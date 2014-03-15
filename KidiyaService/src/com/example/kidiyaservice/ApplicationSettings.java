package com.example.kidiyaservice;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

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
	 * Creates the SupportMapFragment object and its containing google map
	 * @param fragmentActivity the activity that contains the map view
	 */
	public void initializeMapFragment(FragmentActivity fragmentActivity){		
//		m_mapFragment = (SupportMapFragment) fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.map);
		if(m_mapFragment == null){
			m_mapFragment = SupportMapFragment.newInstance();
		}
		
	    FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
	    fragmentTransaction.add(R.id.map, m_mapFragment);
	    fragmentTransaction.commit();
	}
	
	/**
	 * Returns the SupportMapFragment object
	 * @return the SupportMapFragment object
	 */
	public SupportMapFragment mapFragment(){
		return m_mapFragment;
	}
	
	/**
	 * Returns the google map of the MapFragment
	 * @return the fragment's google map
	 */
	public GoogleMap googleMap(){
		return m_googleMap;
	}

	/**
	 * Sets the google map
	 */
	public void setGoogleMap() {
		m_googleMap = m_mapFragment.getMap();
	}
}
