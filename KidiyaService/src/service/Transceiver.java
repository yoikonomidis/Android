package service;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.DisconnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.ReconnectCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;
import com.koushikdutta.async.http.socketio.SocketIORequest;

public class Transceiver {
	private static Transceiver m_instance = null;
	private SocketIOClient m_client = null;
	private static final String TAG = "Transceiver";

	private Transceiver(){
		connect();
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
	private void connect(){
		// TODO: IP and PORT should be retrieved from the global Settings 
		final String url;
		final String ip = "192.168.2.4";
		final String port = "3000";
		url = "http://"+ip+":"+port;
		Log.v(TAG, "URL: " + url);
	    SocketIORequest req = new SocketIORequest(url);
	    req.setLogging("Socket.IO", Log.VERBOSE);
	    SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), req, connectCallback());
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
					Log.v(TAG, "Connected");
					m_client = client;												
					m_client.setDisconnectCallback(disconnectCallback());
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
		Log.v(TAG, "TransmitEvent");	
		Log.v(TAG, "EventName: " + eventName + "jsonArray: " + jsonArray);
		m_client.emit(eventName, jsonArray, null);
	}
	
	/**
	 * Receives events along with a JSON message
	 * @param eventName the name of the event
	 * @param eventCallback the callback function
	 */
	public void receiveEvent(String eventName, EventCallback eventCallback){
		m_client.on(eventName, eventCallback);
	}
	
	/**
	 * Check whether the client is connected
	 * @return
	 */
	public boolean isConnected(){
		return (m_client != null);
	}
	
	/**
	 * Handler used when client disconnects
	 * @return
	 */
	private DisconnectCallback disconnectCallback(){
		return new DisconnectCallback(){
			@Override
			public void onDisconnect(Exception arg0) {
				Log.v(TAG, "DisconnectCallback");
				if(m_client != null)
					m_client.disconnect();
				m_client = null;
			}
			
		};
	}
}