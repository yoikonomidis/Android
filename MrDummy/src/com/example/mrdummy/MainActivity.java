package com.example.mrdummy;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private static final String TAG = "MainActivity";
	//private static String urlget = "http://192.168.2.5:3000/emplist";
	//private static String urlpost = "http://192.168.2.5:3000/addemp";
	private static EditText editTextIP, editTextPort, editTextFunction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editTextIP = (EditText) this.findViewById(R.id.editTextIP);
		editTextPort = (EditText) this.findViewById(R.id.editTextPort);
		editTextFunction = (EditText) this.findViewById(R.id.editTextFunction);
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
		String ip, port, function;
		ip = editTextIP.getText().toString();
		port = editTextPort.getText().toString();
		function = editTextFunction.getText().toString();
		new Rester().execute("GET", ip, port, function);
	}
	
	public void startPosting(View view) {
	    // Do something in response to button	
		String ip, port, function;
		ip = editTextIP.getText().toString();
		port = editTextPort.getText().toString();
		function = editTextFunction.getText().toString();
		new Rester().execute("POST", ip, port, function);
	}

}
