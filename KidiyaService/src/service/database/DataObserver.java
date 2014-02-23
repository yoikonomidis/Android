package service.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

@SuppressLint("NewApi")
public class DataObserver extends ContentObserver {

	private static final String TAG = "DataObserver";
	//private SQLiteHelper database;
	private Context context;

	public DataObserver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}
	
	 @Override
	 public void onChange(boolean selfChange) {
	     onChange(selfChange, null);
	 }

	 // Implement the onChange(boolean, Uri) method to take advantage of the new Uri argument.
	 @Override
	 public void onChange(boolean selfChange, Uri uri) {
	     // Handle change.
//		 String query = "SELECT COLUMN_ID from TABLE_LOCATIONS order by COLUMN_ID DESC limit 1";
//		 SQLiteHelper database = new SQLiteHelper(this.context);
//		 SQLiteDatabase db = database.getWritableDatabase();
//		 long lastId = 0;
//		 Cursor c = db.rawQuery(query, null);
//		 if (c != null && c.moveToFirst()) {
//		     lastId = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
//		 }
//		 Log.v(TAG, "Last rec id: " + lastId);
		 
	 }

}