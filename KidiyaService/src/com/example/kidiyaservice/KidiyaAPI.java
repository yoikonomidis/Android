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
import service.Transceiver;
import service.KidiyaService.KidiyaBinder;

/*
 * High-level API for Kidiya Service.
 */
public class KidiyaAPI{
	private static final String TAG = "Kidiya API";
	private static KidiyaAPI m_KidiyaAPI;
    private final ServiceConnection m_kidiyaServiceConnection;
    private KidiyaService m_kidiyaService;
    private final Context m_context;
    private boolean m_serviceBound = false;

    private KidiyaAPI(Context context) {
        this(context, null);
    }

    private KidiyaAPI(Context context, ServiceConnection serviceConnection) {
        m_kidiyaServiceConnection = new KidiyaServiceConn(serviceConnection);
        m_context = context;
    	Transceiver.instance();
        bindToKidiyaService();
    }
    
    public static void initialize(Context context, ServiceConnection serviceConnection){
    	if(m_KidiyaAPI == null){
    		m_KidiyaAPI = new KidiyaAPI(context, serviceConnection);
    	}
    }
    
    public static KidiyaAPI instance(){
    	return m_KidiyaAPI;
    }
    
    public void bindToKidiyaService() {
        if (!m_serviceBound) {
            final Intent serviceIntent = new Intent(m_context, KidiyaService.class);
            boolean bindResult = m_context.bindService(serviceIntent, m_kidiyaServiceConnection,
                    Context.BIND_AUTO_CREATE);
        } else {
            // already bound
        }
    }
    
    public void unbindFromKidiyaService() {
        if (true == m_serviceBound && null != m_kidiyaServiceConnection) {
            m_context.unbindService(m_kidiyaServiceConnection);
        } else {
            // already unbound
        }
        m_kidiyaService = null;
        m_serviceBound = false;
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
    
    /**
     * Service connection to handle connection with the Kidiya service.
     */
    private class KidiyaServiceConn implements ServiceConnection {
        private final ServiceConnection m_serviceConnection;

        public KidiyaServiceConn(ServiceConnection serviceConnection) {
            m_serviceConnection = serviceConnection;
        }

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.v(TAG, "Bound to Kidiya API...");

            m_kidiyaService = ((KidiyaBinder) binder).getService();
            m_serviceBound = true;

            if (m_serviceConnection != null) {
                m_serviceConnection.onServiceConnected(className, binder);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.v(TAG, "Kidiya API disconnected...");

            m_kidiyaService = null;
            m_serviceBound = false;

            if (m_serviceConnection != null) {
                m_serviceConnection.onServiceDisconnected(className);
            }
        }
    }
}
