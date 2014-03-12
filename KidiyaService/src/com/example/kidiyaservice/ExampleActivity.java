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


/*
 * Dummy activity that connects ExampleApp.
 */
public class ExampleActivity extends Activity {
	
	private Context m_context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application_main);
//		setContentView(R.layout.map_layout);
		m_context = this;
		
		// Start the application
		ExampleApp app = (ExampleApp) getApplication();
		
		// Create the main menu page
		createMainMenu();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void createMainMenu(){
		GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(m_context));
        gridview.setNumColumns(1);		
		
        // Proceed to Map page when the image button is pressed
		gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Intent intent = new Intent(m_context, MapActivity.class);
	            
//	            String message = editText.getText().toString();
//	            intent.putExtra(EXTRA_MESSAGE, message);
	            startActivity(intent);

	        }
	    });
	}
}
