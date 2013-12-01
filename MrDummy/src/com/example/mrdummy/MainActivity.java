package com.example.mrdummy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private static String urlget = "http://192.168.2.3:3000/emplist";
	private static String urlpost = "http://192.168.2.3:3000/androiddata";
	//private static String urlpost = "http://192.168.2.3:80/employees/14/Kurios/Kimonas/15/post";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.v(TAG,"Start Sensing!");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startGeting(View view) {
	    // Do something in response to button	
		new Rester().execute("GET", urlget);
	}
	
	public void startPosting(View view) {
	    // Do something in response to button	
		new Rester().execute("POST", urlpost);
	}

}
