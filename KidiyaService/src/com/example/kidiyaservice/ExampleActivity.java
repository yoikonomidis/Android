package com.example.kidiyaservice;

import java.util.List;
import java.util.Random;

import service.database.DataProvider;
import service.database.SQLiteHelper;
import android.annotation.SuppressLint;
import android.app.LoaderManager;
import service.database.models.Location;
import service.database.models.LocationsAO;
import android.app.Activity;
import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/*
 * Dummy activity that connects ExampleApp.
 */
@SuppressLint("NewApi")
public class ExampleActivity extends ListActivity implements
	LoaderManager.LoaderCallbacks<Cursor> {
	private static final String TAG = "ExampleActivity";
	//private LocationsAO locationSource;
	private static final int DELETE_ID = Menu.FIRST + 1;
	
	private SimpleCursorAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.location_list);
        ExampleApp app = (ExampleApp) getApplication();
        
        this.getListView().setDividerHeight(2);
        fillData();
        registerForContextMenu(getListView());
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
    		fillData();
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

    @SuppressLint("NewApi")
	private void fillData() {
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { SQLiteHelper.COLUMN_TIMESTAMP };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.label };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.location_row, null, from,
            to, 0);

        setListAdapter(adapter);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    // creates a new loader after the initLoader () call
    @SuppressLint("NewApi")
	@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    	String[] projection = { SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_TIMESTAMP };
      	CursorLoader cursorLoader = new CursorLoader(this,
    		  DataProvider.CONTENT_URI, projection, null, null, null);
      	return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    	adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    	// data is not available anymore, delete reference
    	adapter.swapCursor(null);
    }
}
