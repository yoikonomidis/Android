package com.example.kidiyaservice;

import service.KidiyaService;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;

/*
 * Dummy application that binds to and unbinds from Kidiya service.
 */
public class ExampleApp extends Application implements ServiceConnection {
	private static final String TAG = "ExampleApp";
	private KidiyaAPI mKidiyaAPI;
	private KidiyaService boundService;
	
    @Override
    public void onCreate() {
        super.onCreate();
		// TODO Auto-generated method stub
        Log.v(TAG, "Example App installed");
        mKidiyaAPI = new KidiyaAPI(this, this);
    }

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.v(TAG, "Kidiya service is being connected");
		boundService = ((KidiyaService.KidiyaBinder)service).getService();
		mKidiyaAPI.unbindFromKidiyaService();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.v(TAG, "Kidiya service is being disconnected");
		boundService = null;
	}
}
