package com.example.kidiyaservice;


import java.util.ArrayList;
import java.util.Arrays;

import kidiya.utils.Settings;
import service.database.DataProvider;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/*
 * Dummy activity that connects ExampleApp.
 */
@SuppressLint("NewApi")
public class ExampleActivity extends ListActivity{
	//private LocationsAO locationSource;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private Context m_context;
	
	// TODO: Bus lines should be retrieved from the server's database
	private String[] busLines = {"220","230","235"};
	private ArrayList<String> busLinesList = new ArrayList<String>(Arrays.asList(busLines));

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerlineid_layout);

		m_context = this;

		// Initialize Settings
		Settings.instance(m_context, getResources().getString(R.string.app_name));

		// Initialize ApplicationSettings
		ApplicationSettings.instance();

		this.getListView().setDividerHeight(2);
		registerForContextMenu(getListView());

		//Creating the instance of ArrayAdapter containing list of bus lines  
		ArrayAdapter<String> busLinesAdapter = new ArrayAdapter<String>  
		(this,android.R.layout.select_dialog_item, busLines);

		//Getting the instance of AutoCompleteTextView  
		final AutoCompleteTextView busLineAutoCompleteTextView= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);  
		busLineAutoCompleteTextView.setThreshold(1);//will start working from first character  
		busLineAutoCompleteTextView.setAdapter(busLinesAdapter);//setting the adapter data into the AutoCompleteTextView
		busLineAutoCompleteTextView.setTextColor(getResources().getColor(android.R.color.black));
		
		final EditText vehicleIdEditText = (EditText)findViewById(R.id.vehicle_id);		

		final Button registerButton = (Button) findViewById(R.id.button_ok);
		registerButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if((busLinesList.contains(busLineAutoCompleteTextView.getText().toString())) 
						&& !(vehicleIdEditText.getText().toString().equals(""))){								
										
					ApplicationSettings.instance().setBusLine(busLineAutoCompleteTextView.getText().toString());
					ApplicationSettings.instance().setVehicleId(Integer.parseInt(vehicleIdEditText.getText().toString()));
					
					if(KidiyaAPI.instance() == null)
						registerButton.setText(R.string.button_update);
					
					ExampleApp app = (ExampleApp) getApplication();
					KidiyaAPI.initialize(app, app);					
				}
				else{
					Toast.makeText(m_context, "Please Enter a valid Bus Line and a valid Vehicle ID", Toast.LENGTH_LONG).show();					
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.listmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createLocation();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
			.getMenuInfo();
			Uri uri = Uri.parse(DataProvider.CONTENT_URI + "/"
					+ info.id);
			getContentResolver().delete(uri, null, null);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private void createLocation() {
		Intent i = new Intent(this, LocationActivity.class);
		startActivity(i);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, LocationActivity.class);
		Uri locationUri = Uri.parse(DataProvider.CONTENT_URI + "/" + id);
		i.putExtra(DataProvider.CONTENT_ITEM_TYPE, locationUri);

		startActivity(i);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}
}
