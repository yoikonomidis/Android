package service.database;

import java.text.Normalizer;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import service.database.DataProvider;
import service.database.SQLiteHelper;
import android.net.Uri;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

@SuppressLint("NewApi")
public class DataObserver extends ContentObserver {

	private static final String TAG = "DataObserver";
	//private SQLiteHelper database;
	private Context context;

	public DataObserver(Handler handler, Context context) {
		super(handler);
		this.context = context;
		// TODO Auto-generated constructor stub
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
		 final String sa1 = "%A%"; // contains an "A"
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
	 }

}