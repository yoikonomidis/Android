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
		final String url;
		final String ip = "192.168.178.11";
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
					
					JSONObject vehicles = new JSONObject();
					try {
						JSONArray vehicleArray = new JSONArray();
						vehicleArray.put("220");
						vehicleArray.put("230");
						vehicleArray.put("235");
						vehicles.put("vehicles", vehicleArray);
					} catch (JSONException e) {
					    // TODO Auto-generated catch block
					    e.printStackTrace();
					}
					JSONArray jsonArray = new JSONArray();
					jsonArray.put(vehicles);
					
					transmitEvent("getVehicleLocation", jsonArray);					
					receiveEvent("vehicleInfo", vehicleInfoCallback());
										
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
		Log.v("Transceiver", "TransmitEvent");		
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
	 * Handler used when client disconnects
	 * @return
	 */
	private DisconnectCallback disconnectCallback(){
		return new DisconnectCallback(){
			@Override
			public void onDisconnect(Exception arg0) {
				Log.v("Transceiver", "DisconnectCallback");
				m_client.disconnect();
			}
			
		};
	}
	
	/**
	 * Handler used when vehicle information is received
	 * @return
	 */
	private EventCallback vehicleInfoCallback(){		
		return new EventCallback(){
			@Override
			public void onEvent(JSONArray jsonData, Acknowledge ack) {
				Log.v("Transceiver", "Event vehicleInfo received with json arg: " + jsonData.toString());
			}
		};
	}
}