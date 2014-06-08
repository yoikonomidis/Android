package service;

import kidiya.utils.Settings;
import android.util.Log;

import org.json.JSONArray;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.DisconnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.ReconnectCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;
import com.koushikdutta.async.http.socketio.SocketIORequest;

public class Transceiver {
	private static Transceiver m_instance;
	private SocketIOClient m_client;
	private boolean m_connected;

	private Transceiver(){
		Log.d("TRANSCEIVER", "Transceiver Created");
		m_connected = false;
	}

	public static Transceiver instance(){
		if(m_instance == null)
		{
			m_instance = new Transceiver();
		}
		return m_instance;
	}	
	
	/**
	 * Connect the client to the server - Called in the constructor
	 */
	public void connect(){
		if((!isConnected()) && (m_client == null)){			
			final String url;
			url = "http://" + Settings.instance().serverIP() + ":" + Settings.instance().serverPort();
			Log.v("Transceiver", "URL: " + url);
			SocketIORequest req = new SocketIORequest(url);
			req.setLogging("Socket.IO", Log.VERBOSE);
			SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), req, connectCallback());
		}
	}
	
	/**
	 * Disconnect from the server
	 */
	public void disconnect(){
		if(isConnected())
		{
			m_client.disconnect();
			m_client = null;
		}
	}
	
	/**
	 * Handler used when client is connected
	 * @return
	 */
	private ConnectCallback connectCallback(){
		return new ConnectCallback(){
			@Override
			public void onConnectCompleted(Exception ex, SocketIOClient client) {
				try {
					Log.v("Transceiver", "Connected");
					m_client = client;												
					m_client.setDisconnectCallback(disconnectCallback());
					m_client.setReconnectCallback(reconnectCallback());
					m_connected = true;
				}
				catch (Exception e) {
				}
			}
		};
	}

	/**
	 * Transmits an event along with a JSON message
	 * @param eventName the name of the event
	 * @param jsonArray the JSON message
	 */
	public void transmitEvent(String eventName, JSONArray jsonArray){
		if(isConnected())
			try{
				Log.d("Transceiver", "TransmitEvent");
				m_client.emit(eventName, jsonArray, null);
			}catch(Exception e){ // (Probably not needed) This is used in case the connection is terminated while transmitting
				Log.d("TRANSCEIVER", "Connection Lost");
			}
	}
	
	/**
	 * Receives events along with a JSON message
	 * @param eventName the name of the event
	 * @param eventCallback the callback function
	 */
	public void receiveEvent(String eventName, EventCallback eventCallback){		
		if(isConnected())
			Log.v("Transceiver", "ReceiveEvent");
			m_client.addListener(eventName, eventCallback);
	}
	
	/**
	 * Stops receiving events
	 * @param eventName the name of the event
	 * @param eventCallback the callback function
	 */
	public void stopReceivingEvent(String eventName, EventCallback eventCallback){		
		if(isConnected())
			Log.v("Transceiver", "StopReceivingEvent");
			m_client.removeListener(eventName, eventCallback);
	}
	
	/**
	 * Check whether the client is connected
	 * @return
	 */
	public boolean isConnected(){
		return m_connected;
	}
	
	/**
	 * Handler used when client disconnects
	 * @return
	 */
	private DisconnectCallback disconnectCallback(){
		return new DisconnectCallback(){
			@Override
			public void onDisconnect(Exception arg0) {				
				if(isConnected()){
					Log.v("Transceiver", "DisconnectCallback");
					m_connected = false;
				}
			}
		};
	}
	
	/**
	 * Handler used when client reconnects
	 * @return
	 */
	private ReconnectCallback reconnectCallback(){
		return new ReconnectCallback(){
			@Override
			public void onReconnect() {
				Log.v("Transceiver", "ReconnectCallback");
				m_connected = true;
			}
		};
	}
}