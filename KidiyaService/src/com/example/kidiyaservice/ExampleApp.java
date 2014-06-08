package com.example.kidiyaservice;

import kidiya.utils.Settings;
import service.KidiyaService;
import android.os.IBinder;
import android.app.Application;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.util.Log;

/*
 * Dummy application that binds to and unbinds from Kidiya service.
 */
public class ExampleApp extends Application implements ServiceConnection {
	private static final String TAG = "ExampleApp";
	private KidiyaService m_boundService;
	
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "Example App installed");
        
        // Initialize Settings
        Settings.instance(this, getResources().getString(R.string.app_name));
    }

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.v(TAG, "Kidiya service is being connected");
        new Thread() {

            @Override
            public void run() {
            	m_boundService = KidiyaAPI.instance().getService();
            	KidiyaAPI.instance().startKidiya(); 
            }
        }.start();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.v(TAG, "Kidiya service is being disconnected");
		m_boundService = null;
	}
}
