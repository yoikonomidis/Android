package com.example.kidiyaservice;

import kidiya.utils.Tools;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;

public class MapActivity extends FragmentActivity {

	private GoogleMap m_googleMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Getting reference to the SupportMapFragment of activity_main.xml
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        
        // Getting GoogleMap object from the fragment
        setGoogleMap(mapFragment.getMap());
        
        // Check if first run, and prevent map from resetting when screen is rotated
        if (savedInstanceState == null) {
        	mapFragment.setRetainInstance(true);
        }
	}
	
	@Override
	protected void onResume(){
		//TODO: Create a single SharedPreferences object.
		SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
		
		//TODO: Create a restoreCameraState method in Tools.
        double longitude = Tools.getDouble(settings, "longitude", 0);  //"initial longitude" is only used on first startup
        double latitude = Tools.getDouble(settings, "latitude", 0);
        double zoom = Tools.getDouble(settings, "zoom", 0);

        LatLng startPosition = new LatLng(latitude, longitude); //with longitude and latitude

        CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(startPosition)      // Sets the center of the map to Mountain View
        .zoom((float) zoom)         // Sets the zoom
        //.bearing(90)              // Sets the orientation of the camera to east
        //.tilt(30)                 // Sets the tilt of the camera to 30 degrees
        .build();                   // Creates a CameraPosition from the builder
        
        googleMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		super.onResume();
	}
	
	@Override
	protected void onPause(){
		//TODO: Create a saveCameraState method in Tools.
		CameraPosition mMyCam = googleMap().getCameraPosition();
		double longitude = mMyCam.target.longitude;
		double latitude = mMyCam.target.latitude;
		double zoom = mMyCam.zoom;

		//TODO: Create a single SharedPreferences object. 
		SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
		SharedPreferences.Editor editor = settings.edit();
		
		Tools.putDouble(editor, "longitude", longitude);
		Tools.putDouble(editor, "latitude", latitude);
		Tools.putDouble(editor, "zoom", zoom);
		//put all other values like latitude, angle, zoom...
		editor.commit();
		super.onPause();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
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
	
	public GoogleMap googleMap(){
		return m_googleMap;
	}
	
	public void setGoogleMap(GoogleMap map){
		m_googleMap = map;
	}

}
