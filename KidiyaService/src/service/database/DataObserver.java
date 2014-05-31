package service.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kidiyaservice.ApplicationSettings;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.EventCallback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import service.Transceiver;
import service.database.DataProvider;
import service.database.SQLiteHelper;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

@SuppressLint("NewApi")
public class DataObserver extends ContentObserver {

	private static final String TAG = "DataObserver";
	//private SQLiteHelper database;
	private Context context;
	private Transceiver m_transceiver;

	public DataObserver(Handler handler, Context context) {
		super(handler);
		this.context = context;
		m_transceiver = Transceiver.instance();
		
		Log.v(TAG, "Connected"); // That appears twice in the log. Why?
	}
	
	 @Override
	 public void onChange(boolean selfChange) {
	     // Handle change.
		 String[] projection = { SQLiteHelper.COLUMN_TIMESTAMP,
					  SQLiteHelper.COLUMN_LATITUDE, SQLiteHelper.COLUMN_LONGITUDE };
		 Cursor c = context.getContentResolver().query(DataProvider.CONTENT_URI, 
					projection, null,
					null, null);
		 c.moveToLast();
		 long time = c.getLong(0);
		 double lat = c.getDouble(1);
		 double longi = c.getDouble(2);
		 Log.v(TAG, "Last rec time: " + time +
				 " long: " + longi +
				 " lat: " + lat);
		
		JSONObject locations = new JSONObject();
		
		JSONObject postData = new JSONObject();
		JSONArray locationArray = new JSONArray();
		try {
			//Create json for postData
			postData.put("id", ApplicationSettings.instance().vehicleId());
			postData.put("name", ApplicationSettings.instance().busLine());
	    
			JSONObject loc = new JSONObject();
			loc.put("latitude", lat);
			loc.put("longitude", longi);
			postData.put("location", loc);
		         
			locationArray.put(postData);
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		// Send an event to the server, along with the JSON message
		m_transceiver.transmitEvent("updateVehicleLocation", locationArray);
		// ############################################################
	 }

	 // Implement the onChange(boolean, Uri) method to take advantage of the new Uri argument.
	 @Override
	 public void onChange(boolean selfChange, Uri uri) {
		 //queryLastLocation();
		 Log.v(TAG, "Last rec uri: " + uri);	
		 String id = uri.getLastPathSegment();
		 Log.v(TAG, "Last rec id: " + id);	
	     // Handle change.
		 String[] projection = { SQLiteHelper.COLUMN_TIMESTAMP,
					  SQLiteHelper.COLUMN_LATITUDE, SQLiteHelper.COLUMN_LONGITUDE };
		 
		 Cursor c = context.getContentResolver().query(DataProvider.CONTENT_URI, 
				 							projection, SQLiteHelper.COLUMN_ID + "='" + id + "'",
				 							null, null);
		 c.moveToFirst();
		 long time = c.getLong(0);
		 double lat = c.getDouble(1);
		 double longi = c.getDouble(2);
		 Log.v(TAG, "Last rec time: " + time +
				 " long: " + longi +
				 " lat: " + lat);
		
		JSONObject locations = new JSONObject();
		
		JSONObject postData = new JSONObject();
		JSONArray locationArray = new JSONArray();
		try {
			//Create json for postData
			postData.put("id", ApplicationSettings.instance().vehicleId());
			postData.put("name", ApplicationSettings.instance().busLine());
	    
			JSONObject loc = new JSONObject();
			loc.put("latitude", lat);
			loc.put("longitude", longi);
			postData.put("location", loc);
		         
			locationArray.put(postData);
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		// Send an event to the server, along with the JSON message
		m_transceiver.transmitEvent("updateVehicleLocation", locationArray);
		// ############################################################
	}

	/**
	 * Handler used when location information is received
	 * @return
	 */
	private EventCallback locationInfoCallback(){		
		return new EventCallback(){
			@Override
			public void onEvent(JSONArray jsonData, Acknowledge ack) {
				Log.v("Transceiver", "Event locationInfo received with json arg: " + jsonData.toString());
			}
		};
	 }
}