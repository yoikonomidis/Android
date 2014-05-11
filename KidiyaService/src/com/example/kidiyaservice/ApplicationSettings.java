package com.example.kidiyaservice;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.EventCallback;

public class ApplicationSettings {

	private static ApplicationSettings m_instance;
	private static SupportMapFragment m_mapFragment;
	private static GoogleMap m_googleMap;
	
	private HashMap<String, Station> m_stationsHashMap = new HashMap<String, Station>();
	
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
		m_googleMap.setMyLocationEnabled(true);
	}
	
	/**
	 * Fetch station info from server
	 */
	public void fetchStations(){
		m_stationsHashMap.clear();
		Log.d("ApplicationSettings", "FETCH STATIONINFO");
		KidiyaAPI.instance().receiveEvent("stationInfo", setStationInfo());
		KidiyaAPI.instance().transmitEvent("getStations", null);
	}
	
	public HashMap<String, Station> stationsHashMap(){
		return m_stationsHashMap;
	}
	
	private EventCallback setStationInfo(){		
		return new EventCallback(){
			@Override
			public void onEvent(JSONArray jsonData, Acknowledge ack) {
				Log.v("ApplicationSettings", "Event stationInfo received with json arg: " + jsonData.toString());
				
				try{
					for(int i=0; i<jsonData.getJSONArray(0).length(); i++){
						m_stationsHashMap.put(jsonData.getJSONArray(0).getJSONObject(i).getJSONObject("properties").getString("id"), 
								new Station(jsonData.getJSONArray(0).getJSONObject(i).getJSONObject("properties").getString("id"), 
										jsonData.getJSONArray(0).getJSONObject(i).getJSONObject("properties").getString("name"),
										jsonData.getJSONArray(0).getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getDouble(0),
										jsonData.getJSONArray(0).getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getDouble(1)));
					}

					KidiyaAPI.instance().stopReceivingEvent("stationInfo",setStationInfo());

				}catch (JSONException e){
					e.printStackTrace();
				}
			}
		};
	}
}
