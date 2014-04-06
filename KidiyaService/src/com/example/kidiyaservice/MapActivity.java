package com.example.kidiyaservice;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kidiya.utils.Settings;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.EventCallback;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class MapActivity extends FragmentActivity {
	
	private HashMap<String, HashMap<String, Marker>> m_busLineHashMap = new HashMap<String, HashMap<String, Marker>>();
	private String[] arrayVehicles = {"220", "230", "235"};
	private Handler m_handler = new Handler();
	
	// Handler used when vehicle information is received	 
	private EventCallback m_vehicleInfoCallback = new EventCallback(){
		@Override
		public void onEvent(JSONArray jsonData, Acknowledge ack) {
			Log.v("Transceiver", "Event vehicleInfo received with json arg: " + jsonData.toString());
			UpdateBusMarker updateMarker = new UpdateBusMarker(jsonData);
			m_handler.post(updateMarker);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_map);
		
		// Getting reference to the SupportMapFragment of activity_main.xml
		ApplicationSettings.instance().initializeMapFragment(this);
		

		// Check if first run, and prevent map from resetting when screen is rotated
		if (savedInstanceState == null) {
			ApplicationSettings.instance().mapFragment().setRetainInstance(true);
		}
		
		// Show the Up button in the action bar.
		setupActionBar();	
		
		UpdateStationMarkers updateStationMarkers = new UpdateStationMarkers();
		m_handler.post(updateStationMarkers);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		// Instantiate google map
		ApplicationSettings.instance().setGoogleMap();
		
		// ######## Example of how to use the transceiver API ########
		// Register to a specific event from the server
		KidiyaAPI.instance().receiveEvent("vehicleInfo", m_vehicleInfoCallback);
		
		JSONObject vehicles = new JSONObject();
		try {
			JSONArray vehicleArray = new JSONArray();
			vehicleArray.put(arrayVehicles[0]);
			vehicleArray.put(arrayVehicles[1]);
			vehicleArray.put(arrayVehicles[2]);
			vehicles.put("vehicles", vehicleArray);
		} catch (JSONException e) {
		    e.printStackTrace();
		}
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(vehicles);
		// Send an event to the server, along with the JSON message
		KidiyaAPI.instance().transmitEvent("getVehicleLocation", jsonArray);
		// ############################################################
		
		restoreCameraState();		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		storeCameraState();
		
		// De-register from event
		KidiyaAPI.instance().stopReceivingEvent("vehicleInfo", m_vehicleInfoCallback);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		Log.d("DEBUG", "ON DESTROY");
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * This function stores camera's state (latitude, longitude, zoom) in settings
	 */
	private void storeCameraState(){
		CameraPosition m_MyCam = ApplicationSettings.instance().googleMap().getCameraPosition();
		double longitude = m_MyCam.target.longitude;
		double latitude = m_MyCam.target.latitude;
		double zoom = m_MyCam.zoom;
 
		Settings settings = Settings.instance(this, getResources().getString(R.string.app_name));
		settings.setValue("longitude", longitude, Settings.SETTING_TYPE.DOUBLE);
		settings.setValue("latitude", latitude, Settings.SETTING_TYPE.DOUBLE);
		settings.setValue("zoom", zoom, Settings.SETTING_TYPE.DOUBLE);
	}
	
	/**
	 * This function is used to restore the camera's state (latitude, longitude, zoom) from settings
	 */
	private void restoreCameraState(){
		Settings settings = Settings.instance(this, getResources().getString(R.string.app_name));
        double longitude = settings.value("longitude", Settings.SETTING_TYPE.DOUBLE);
        double latitude = settings.value("latitude", Settings.SETTING_TYPE.DOUBLE);
        double zoom = settings.value("zoom", Settings.SETTING_TYPE.DOUBLE);

        LatLng startPosition = new LatLng(latitude, longitude); //with longitude and latitude
        CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(startPosition)      // Sets the center of the map to the last position
        .zoom((float) zoom)         // Sets the zoom
        //.bearing(90)              // Sets the orientation of the camera to east
        //.tilt(30)                 // Sets the tilt of the camera to 30 degrees
        .build();                   // Creates a CameraPosition from the builder
        
        ApplicationSettings.instance().googleMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	class UpdateBusMarker implements Runnable{

		private JSONArray m_jsonArray;
		
		public UpdateBusMarker(JSONArray jsonArray){
			m_jsonArray = jsonArray;
		}
		
		@Override
		public void run() {
			try {
				for(int i=0; i<m_jsonArray.getJSONArray(0).length(); i++){	
					Log.d("DEBUG", m_jsonArray.getJSONArray(0).getJSONObject(i).getJSONObject("location").toString());

					double latitude = m_jsonArray.getJSONArray(0).getJSONObject(i).getJSONObject("location").getDouble("latitude");
					double longitude = m_jsonArray.getJSONArray(0).getJSONObject(i).getJSONObject("location").getDouble("longitude");

					Log.d("DEBUG", latitude + " " + longitude);

					LatLng latlng = new LatLng(latitude, longitude);

					String indexName = m_jsonArray.getJSONArray(0).getJSONObject(i).getString("name");
					String indexId = m_jsonArray.getJSONArray(0).getJSONObject(i).getString("id");
					
					if(m_busLineHashMap.get(indexName) == null){
						m_busLineHashMap.put(indexName, new HashMap<String, Marker>());
					}
					
					if(m_busLineHashMap.get(indexName).get(indexId) != null){
						m_busLineHashMap.get(indexName).get(indexId).setPosition(latlng);
					}
					else{
						m_busLineHashMap.get(indexName).put(indexId, ApplicationSettings.instance().googleMap().addMarker(new MarkerOptions()
						.position(new LatLng(latitude, longitude))
						.title(indexName + " " + indexId)));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class UpdateStationMarkers implements Runnable{
		
		@Override
		public void run() {
			try {
				for(int i=0; i<ApplicationSettings.instance().stationsHashMap().size(); i++){	
					Log.d("DEBUG", "STATION MARKERS");
					ApplicationSettings.instance().googleMap().addMarker(new MarkerOptions()
					.position(ApplicationSettings.instance().stationsHashMap().get(i+"").latLng())
					.title(ApplicationSettings.instance().stationsHashMap().get(i+"").id() + " "
							+ ApplicationSettings.instance().stationsHashMap().get(i+"").name())
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
