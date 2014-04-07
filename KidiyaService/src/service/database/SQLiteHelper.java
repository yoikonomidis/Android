package service.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_LOCATIONS = "locations";
	public static final String TABLE_ACCELERATIONS = "accelerations";
	public static final String TABLE_OVERALL = "overall";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CREATED_AT = "created_at";
	public static final String COLUMN_LOCATION_ID = "_id_location";
	public static final String COLUMN_ACCEL_ID = "_id_accel";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_ACCEL_X = "accelX";
	public static final String COLUMN_ACCEL_Y = "accelY";
	public static final String COLUMN_ACCEL_Z = "accelZ";

	private static final String DATABASE_NAME = "sensors.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String CREATE_TABLE_LOCATION = "create table "
			+ TABLE_LOCATIONS + "(" + COLUMN_ID
		    + " integer primary key autoincrement, " + COLUMN_TIMESTAMP
			+ " long not null, " + COLUMN_LATITUDE
			+ " double not null, " + COLUMN_LONGITUDE
			+ " double not null);";
	private static final String CREATE_TABLE_ACCELERATION = "create table "
			+ TABLE_ACCELERATIONS + "(" + COLUMN_ID
		    + " integer primary key autoincrement, " + COLUMN_ACCEL_X
			+ " double not null, " + COLUMN_ACCEL_Y
			+ " double not null, " + COLUMN_ACCEL_Z
			+ " double not null);";
	private static final String CREATE_TABLE_OVERALL = "create table "
			+ TABLE_OVERALL + "(" + COLUMN_ID
		    + " integer primary key autoincrement, " + COLUMN_LOCATION_ID
			+ " integer not null, " + COLUMN_ACCEL_ID
			+ " integer not null, " + COLUMN_CREATED_AT
			+ " datetime);";


	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_LOCATION);
		database.execSQL(CREATE_TABLE_ACCELERATION);
		database.execSQL(CREATE_TABLE_OVERALL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCELERATIONS);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_OVERALL);
	    onCreate(db);
	}
}
