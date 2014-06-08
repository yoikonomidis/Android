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
    private final ServiceConnection m_serviceConnection;
    private KidiyaService m_KidiyaService;
    private final Context m_context;
    private static final String TAG = "Kidiya API";
    private boolean m_serviceBound = false;
    
    public static void setupKidiyaAPI(Context context, ServiceConnection serviceConnection){
    	if(m_KidiyaAPI == null){
    		Log.d("KIDIYA_API", "Initialize KidiyaAPI");
    		m_KidiyaAPI = new KidiyaAPI(context, serviceConnection);
    	}
    	
    	Transceiver.instance().connect();
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
	
    public void disconnectFromServer(){
    	Transceiver.instance().disconnect();
    }
    
    public void startKidiya(){
    	bindToKidiyaService();
    }
    
    public void stopKidiya(){
    	unbindFromKidiyaService();
    	m_KidiyaService.stopKidiya();
    	m_KidiyaService = null;    
    	disconnectFromServer();
    }
    
    public void startKidiyaService(){
    	m_KidiyaService.startKidiya();
    }    
    
    public KidiyaService getService() {
        return m_KidiyaService;
    }
    
    /**
     * Service connection to handle connection with the Kidiya service.
     */
    private class KidiyaServiceConnection implements ServiceConnection {
        private final ServiceConnection KidiyaServiceConnection;

        public KidiyaServiceConnection(ServiceConnection serviceConnection) {
        	KidiyaServiceConnection = serviceConnection;
        }

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.v(TAG, "Bound to Kidiya API...");

            m_KidiyaService = ((KidiyaBinder) binder).getService();
            m_serviceBound = true;

            if (KidiyaServiceConnection != null) {
            	KidiyaServiceConnection.onServiceConnected(className, binder);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.v(TAG, "Kidiya API disconnected...");

            m_KidiyaService = null;
            m_serviceBound = false;

            if (KidiyaServiceConnection != null) {
            	KidiyaServiceConnection.onServiceDisconnected(className);
            }
        }
    }

    private KidiyaAPI(Context context) {
        this(context, null);
    }

    private KidiyaAPI(Context context, ServiceConnection serviceConnection) {
        m_serviceConnection = new KidiyaServiceConnection(serviceConnection);
        m_context = context;
    }
    
    private void bindToKidiyaService() {
        if (!m_serviceBound) {
        	Log.d("KIDIYA_API", "Binding...");
            final Intent serviceIntent = new Intent(m_context, KidiyaService.class);
            boolean bindResult = m_context.bindService(serviceIntent, m_serviceConnection,
                    Context.BIND_AUTO_CREATE);
        } else {
            // already bound
        }
    }
    
    private void unbindFromKidiyaService() {
        if (true == m_serviceBound && null != m_serviceConnection) {
        	Log.d("KIDIYA_API", "Unbinding...");
            m_context.unbindService(m_serviceConnection);
        } else {
            // already unbound
        }

        m_serviceBound = false;
    }
}
