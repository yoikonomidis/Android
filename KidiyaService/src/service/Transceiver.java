package service;

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
		final String ip = "192.168.2.3";
		final String port = "3000";
		url = "http://"+ip+":"+port;
		Log.v("Transceiver", "URL: " + url);
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
					Log.v("Transceiver", "Connected");
					m_client = client;												
					m_client.setDisconnectCallback(disconnectCallback());
					m_client.setReconnectCallback(reconnectCallback());
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
		Log.v("Transceiver", "TransmitEvent");	
		if(isConnected())
			m_client.emit(eventName, jsonArray, null);
	}
	
	/**
	 * Receives events along with a JSON message
	 * @param eventName the name of the event
	 * @param eventCallback the callback function
	 */
	public void receiveEvent(String eventName, EventCallback eventCallback){
		Log.v("Transceiver", "ReceiveEvent");
		if(isConnected())
			m_client.addListener(eventName, eventCallback);
	}
	
	/**
	 * Stops receiving events
	 * @param eventName the name of the event
	 * @param eventCallback the callback function
	 */
	public void stopReceivingEvent(String eventName, EventCallback eventCallback){
		Log.v("Transceiver", "StopReceivingEvent");
		if(isConnected())
			m_client.removeListener(eventName, eventCallback);
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
				Log.v("Transceiver", "DisconnectCallback");
				m_client = null;
				m_instance = null;
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
			}
		};
	}
}