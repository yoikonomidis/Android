package service;

import service.database.DataProvider;
import service.database.SQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class LocationSensor {
	protected static final String TAG = "LocationSensor";
	private Context context;
	private static LocationSensor instance = null;
	private LocationManager locationManager;

	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {

	    public void onProviderEnabled(String provider) {}

	    public void onProviderDisabled(String provider) {}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.v(TAG, "New location: " + location);
			long timestamp = location.getTime();
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
		    ContentValues values = new ContentValues();
		    values.put(SQLiteHelper.COLUMN_TIMESTAMP, timestamp);
		    values.put(SQLiteHelper.COLUMN_LATITUDE, latitude);
		    values.put(SQLiteHelper.COLUMN_LONGITUDE, longitude);
		   
		    context.getContentResolver().insert(DataProvider.CONTENT_URI, values);
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
	};
	
    public static LocationSensor getInstance(Context context) {
        if (instance == null) {
            instance = new LocationSensor(context);
        }
        return instance;
    }
    
    protected LocationSensor(Context context) {
        this.context = context;
        // Acquire a reference to the system Location Manager
    	locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
    
    public void start(){
    	// Register the listener with the Location Manager to receive location updates
    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    
    public void stop(){
    	locationManager.removeUpdates(locationListener);
    }
}
