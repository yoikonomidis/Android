package com.example.kidiyaservice;

import kidiya.utils.Settings;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class MapActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_map);
		
		// Getting reference to the SupportMapFragment of activity_main.xml
		SupportMapFragment mapFragment = ApplicationSettings.instance().mapFragment(this);

		// Check if first run, and prevent map from resetting when screen is rotated
		if (savedInstanceState == null) {
			mapFragment.setRetainInstance(true);
		}
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		restoreCameraState();		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		storeCameraState();
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
	
	/**
	 * This function stores camera's state (latitude, longitude, zoom) in settings
	 */
	private void storeCameraState(){
		CameraPosition mMyCam = ApplicationSettings.instance().googleMap().getCameraPosition();
		double longitude = mMyCam.target.longitude;
		double latitude = mMyCam.target.latitude;
		double zoom = mMyCam.zoom;
 
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
        .target(startPosition)      // Sets the center of the map to Mountain View
        .zoom((float) zoom)         // Sets the zoom
        //.bearing(90)              // Sets the orientation of the camera to east
        //.tilt(30)                 // Sets the tilt of the camera to 30 degrees
        .build();                   // Creates a CameraPosition from the builder
        
        ApplicationSettings.instance().googleMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

}
