package com.example.kidiyaservice;

import service.database.DataProvider;
import service.database.SQLiteHelper;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class LocationActivity extends Activity {
	private EditText mTimestamp;
	private EditText mLatitude;
	private EditText mLongitude;
	
	private Uri locationUri;

	@Override
	protected void onCreate(Bundle bundle) {
	    super.onCreate(bundle);
	    setContentView(R.layout.location_list);
	
	    Bundle extras = getIntent().getExtras();
	
	    // check from the saved Instance
	    locationUri = (bundle == null) ? null : (Uri) bundle
	        .getParcelable(DataProvider.CONTENT_ITEM_TYPE);
	
	    // Or passed from the other activity
	    if (extras != null) {
	      locationUri = extras
	          .getParcelable(DataProvider.CONTENT_ITEM_TYPE);
	
	      fillData(locationUri);
	    }
  	}

	private void fillData(Uri uri) {
		String[] projection = { SQLiteHelper.COLUMN_TIMESTAMP,
			  SQLiteHelper.COLUMN_LATITUDE, SQLiteHelper.COLUMN_LONGITUDE };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
			  null);
		if (cursor != null) {
			cursor.moveToFirst();
			
			mTimestamp.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(SQLiteHelper.COLUMN_TIMESTAMP)));
			mLatitude.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(SQLiteHelper.COLUMN_LATITUDE)));
			mLongitude.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(SQLiteHelper.COLUMN_LONGITUDE)));

			// always close the cursor
			cursor.close();
		}
	}
	
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(DataProvider.CONTENT_ITEM_TYPE, locationUri);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}
	
	private void saveState() {
		String timestamp = mTimestamp.getText().toString();
		String longitude = mLatitude.getText().toString();
		String latitude = mLongitude.getText().toString();
	
	
	    ContentValues values = new ContentValues();
	    values.put(SQLiteHelper.COLUMN_TIMESTAMP, timestamp);
	    values.put(SQLiteHelper.COLUMN_LATITUDE, latitude);
	    values.put(SQLiteHelper.COLUMN_LONGITUDE, longitude);
	
	    if (locationUri == null) {
	    	locationUri = getContentResolver().insert(DataProvider.CONTENT_URI, values);
	    } else {
	    	getContentResolver().update(locationUri, values, null, null);
	    }
	}
	
	private void makeToast() {
	    Toast.makeText(LocationActivity.this, "Please maintain a location",
	    Toast.LENGTH_LONG).show();
	}
} 