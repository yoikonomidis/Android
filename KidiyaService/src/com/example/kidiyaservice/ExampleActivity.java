package com.example.kidiyaservice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/*
 * Dummy activity that connects ExampleApp.
 */
public class ExampleActivity extends Activity {
     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.application_main);
         ExampleApp app = (ExampleApp) getApplication();
    }
     
     @Override
     protected void onResume() {
         super.onResume();
     }
}
