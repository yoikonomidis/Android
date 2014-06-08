package com.example.kidiyaservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import kidiya.utils.ImageAdapter;
import kidiya.utils.Settings;


/*
 * Dummy activity that connects ExampleApp.
 */
public class ExampleActivity extends Activity {
	
	private Context m_context;
	private ExampleApp m_application;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application_main);
//		setContentView(R.layout.map_layout);
		m_context = this;
		
		// Check if Google Play Services is installed on the device
		ApplicationSettings.googlePlayServicesExists(m_context);

		// Initialize Settings
		Settings.instance(m_context, getResources().getString(R.string.app_name));

		// Initialize ApplicationSettings
		ApplicationSettings.instance();
		
		// Start the application
		m_application = (ExampleApp) getApplication();
		
		KidiyaAPI.setupKidiyaAPI(m_application, m_application);
//		KidiyaAPI.instance().startKidiya();
		
		// Create the main menu page
		createMainMenu();
		
		// TODO: Add a user friendly message if connection is not established after the attempts, and exit application
		int connectionAttempts = 10;
		
		// Make sure a connection to the server is established before we attempt to fetch station info
		while((!KidiyaAPI.instance().isConnected()) && (connectionAttempts > 0)){
			connectionAttempts--;
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		ApplicationSettings.instance().fetchStations();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroy(){		
		super.onDestroy();
				
//		KidiyaAPI.instance().stopKidiya();
	}
	
	private void createMainMenu(){
		GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(m_context));
        gridview.setNumColumns(1);		
		
        // Proceed to Map page when the image button is pressed
		gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	if(ApplicationSettings.googlePlayServicesExists(m_context)){
	        		Intent intent = new Intent(m_context, MapActivity.class);

	        		//	            String message = editText.getText().toString();
	        		//	            intent.putExtra(EXTRA_MESSAGE, message);
	        		startActivity(intent);
	        	}
	        }
	    });
	}
}
