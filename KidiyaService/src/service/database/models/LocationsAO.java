package service.database.models;

import java.util.ArrayList;
import java.util.List;

import service.database.SQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LocationsAO {
	// Database fields
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	private String[] allColumns = {SQLiteHelper.COLUMN_ID, 
			SQLiteHelper.COLUMN_TIMESTAMP, SQLiteHelper.COLUMN_LATITUDE,
			SQLiteHelper.COLUMN_LONGITUDE};

	public LocationsAO(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Location createLocation(long timestamp, double latitude, double longitude) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_TIMESTAMP, timestamp);
		values.put(SQLiteHelper.COLUMN_LATITUDE, latitude);
		values.put(SQLiteHelper.COLUMN_LONGITUDE, longitude);
		long insertId = database.insert(SQLiteHelper.TABLE_LOCATIONS, null,
				values);
		Cursor cursor = database.query(SQLiteHelper.TABLE_LOCATIONS,
				allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
    cursor.moveToFirst();
    Location newLocation = cursorToLocation(cursor);
    cursor.close();
    return newLocation;
  }

  public void deleteLocation(Location location) {
	long id = location.getId();
    System.out.println("Location deleted with id: " + id);
    	database.delete(SQLiteHelper.TABLE_LOCATIONS, SQLiteHelper.COLUMN_ID
    			+ " = " + id, null);
  }

  public List<Location> getAllLocations() {
	  List<Location> locations = new ArrayList<Location>();

	  Cursor cursor = database.query(SQLiteHelper.TABLE_LOCATIONS,
			  allColumns, null, null, null, null, null);

	  cursor.moveToFirst();
	  while (!cursor.isAfterLast()) {
		  Location location = cursorToLocation(cursor);
		  locations.add(location);
		  cursor.moveToNext();
	  }
	  // make sure to close the cursor
	  cursor.close();
	  return locations;
  }

  private Location cursorToLocation(Cursor cursor) {
	  Location location = new Location();
	  location.setId(cursor.getLong(0));
	  location.setTimestamp(cursor.getLong(1));
	  location.setLatitude(cursor.getDouble(2));
	  location.setLongitude(cursor.getDouble(3));
	  return location;
  }
  
}
