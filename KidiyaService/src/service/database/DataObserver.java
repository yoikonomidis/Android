package service.database;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	// Handler used when location information is received
	private EventCallback locationInfoCallback = new EventCallback(){		
		@Override
		public void onEvent(JSONArray jsonData, Acknowledge ack) {
			Log.v("Transceiver", "Event locationInfo received with json arg: " + jsonData.toString());
		}
	};

	public DataObserver(Handler handler, Context context) {
		super(handler);
		this.context = context;
		
		Log.v(TAG, "Connected"); // That appears twice in the log. Why?
	}
	
	 @Override
	 public void onChange(boolean selfChange) {
	     onChange(selfChange, null);
	     //queryLastLocation();
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
		 long longi = c.getLong(1);
		 long lat = c.getLong(2);
		 Log.v(TAG, "Last rec time: " + time +
				 " long: " + longi +
				 " lat: " + lat);
		
		JSONObject locations = new JSONObject();
		try {
			JSONArray locationArray = new JSONArray();
			locationArray.put(time);
			locationArray.put(longi);
			locationArray.put(lat);
			locations.put("locationInfo", locationArray);
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(locations);
		// Send an event to the server, along with the JSON message
		Transceiver.instance().transmitEvent("getLocation", jsonArray);
	 }
}