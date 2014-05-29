package com.example.kidiyaservice;


import kidiya.utils.Settings;
import service.database.DataProvider;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

/*
 * Dummy activity that connects ExampleApp.
 */
@SuppressLint("NewApi")
public class ExampleActivity extends ListActivity{
	//private LocationsAO locationSource;
	private static final int DELETE_ID = Menu.FIRST + 1;

	private Context m_context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_list);

		m_context = this;

		// Initialize Settings
		Settings.instance(m_context, getResources().getString(R.string.app_name));


		ExampleApp app = (ExampleApp) getApplication();

		this.getListView().setDividerHeight(2);
		registerForContextMenu(getListView());

		// TODO: Add a user friendly message if connection is not established after the attempts
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
