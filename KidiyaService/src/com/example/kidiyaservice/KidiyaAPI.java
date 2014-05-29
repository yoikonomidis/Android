package com.example.kidiyaservice;

import org.json.JSONArray;

import com.koushikdutta.async.http.socketio.EventCallback;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import service.KidiyaService;
import service.KidiyaService.KidiyaBinder;
import service.Transceiver;

/*
 * High-level API for Kidiya Service.
 */
public class KidiyaAPI {
	private static KidiyaAPI m_KidiyaAPI;
    private final ServiceConnection mServiceConnection;
    private KidiyaService mKidiyaService;
    private final Context mContext;
    private static final String TAG = "Kidiya API";
    private boolean mServiceBound = false;
    
    public static void initialize(Context context, ServiceConnection serviceConnection){
    	if(m_KidiyaAPI == null){
    		m_KidiyaAPI = new KidiyaAPI(context, serviceConnection);
    	}
    }
    
    public static KidiyaAPI instance(){
    	return m_KidiyaAPI;
    }
    
    /**
	 * Transmits an event along with a JSON message
	 * @param eventName the name of the event
	 * @param jsonArray the JSON message
	 */
	public void transmitEvent(String eventName, JSONArray jsonArray){
		Transceiver.instance().transmitEvent(eventName, jsonArray);
	}
	
	/**
	 * Receives events along with a JSON message
	 * @param eventName the name of the event
	 * @param eventCallback the callback function
	 */
	public void receiveEvent(String eventName, EventCallback eventCallback){
		Transceiver.instance().receiveEvent(eventName, eventCallback);
	}
	
	/**
	 * Stops receiving events
	 * @param eventName the name of the event
	 * @param eventCallback the callback function
	 */
	public void stopReceivingEvent(String eventName, EventCallback eventCallback){
		Transceiver.instance().stopReceivingEvent(eventName, eventCallback);
	}
	
	public boolean isConnected(){
		return Transceiver.instance().isConnected();
	}
    
    /**
     * Service connection to handle connection with the Kidiya service.
     */
    private class KidiyaServiceConn implements ServiceConnection {
        private final ServiceConnection mServiceConnection;

        public KidiyaServiceConn(ServiceConnection serviceConnection) {
            mServiceConnection = serviceConnection;
        }

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.v(TAG, "Bound to Kidiya API...");

            mKidiyaService = ((KidiyaBinder) binder).getService();
            mServiceBound = true;

            if (mServiceConnection != null) {
                mServiceConnection.onServiceConnected(className, binder);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.v(TAG, "Kidiya API disconnected...");

            mKidiyaService = null;
            mServiceBound = false;

            if (mServiceConnection != null) {
                mServiceConnection.onServiceDisconnected(className);
            }
        }
    }

    public KidiyaAPI(Context context) {
        this(context, null);
    }

    public KidiyaAPI(Context context, ServiceConnection serviceConnection) {
        mServiceConnection = new KidiyaServiceConn(serviceConnection);
        mContext = context;
    	Transceiver.instance();
        bindToKidiyaService();
    }
    
    public void bindToKidiyaService() {
        if (!mServiceBound) {
            final Intent serviceIntent = new Intent(mContext, KidiyaService.class);
            boolean bindResult = mContext.bindService(serviceIntent, mServiceConnection,
                    Context.BIND_AUTO_CREATE);
        } else {
            // already bound
        }
    }
    
    public void unbindFromKidiyaService() {
        if (true == mServiceBound && null != mServiceConnection) {
            mContext.unbindService(mServiceConnection);
        } else {
            // already unbound
        }
        mKidiyaService = null;
        mServiceBound = false;
    }
    
    public KidiyaService getService() {
        return mKidiyaService;
    }
}
