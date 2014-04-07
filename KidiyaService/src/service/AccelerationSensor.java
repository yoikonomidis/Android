package service;

import service.database.DataProvider;
import service.database.SQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class AccelerationSensor implements SensorEventListener {
	protected static final String TAG = "AccelerationSensor";
	private Context context;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private static AccelerationSensor instance = null;
	private static long currentTime = System.nanoTime();

    public static AccelerationSensor getInstance(Context context) {
        if (instance == null) {
            instance = new AccelerationSensor(context);
        }
        return instance;
    }
    
    protected AccelerationSensor(Context context) {
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    
    @Override
    public void onSensorChanged(SensorEvent event){
    	//update database only after every 2 seconds
    	if (System.nanoTime() > currentTime + 2000000000){
    		Log.v(TAG, "New acceleration: " + event.values[0] + ", "
    				+ event.values[1] + ", "
    				+ event.values[2]);
    		ContentValues values = new ContentValues();
		    values.put(SQLiteHelper.COLUMN_ACCEL_X, event.values[0]);
		    values.put(SQLiteHelper.COLUMN_ACCEL_Y, event.values[1]);
		    values.put(SQLiteHelper.COLUMN_ACCEL_Z, event.values[2]);
		   
		    context.getContentResolver().insert(DataProvider.CONTENT_URI2, values);
		    
    		currentTime = System.nanoTime();}
	}
    
    public void start(){
    	mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    public void stop(){
    	mSensorManager.unregisterListener(this, mSensor);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
}
