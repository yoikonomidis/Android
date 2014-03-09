package com.example.kidiyaservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


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
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new MyAdapter(m_context));
        gridview.setNumColumns(1);
        
		ExampleApp app = (ExampleApp) getApplication();
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Intent intent = new Intent(m_context, MapActivity.class);
	            
//	            String message = editText.getText().toString();
//	            intent.putExtra(EXTRA_MESSAGE, message);
	            startActivity(intent);

	        }
	    });

	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}

class MyAdapter extends BaseAdapter {

	private Context m_context;

	public MyAdapter(Context c) {
		m_context = c;
	}

	@Override
	public int getCount() {
		return m_menuIds.length;
	}

	@Override
	public Object getItem(int arg0) {
		return m_menuIds[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View grid;

		if(convertView==null){
			grid = new View(m_context);
//			LayoutInflater inflater = getLayoutInflater();
			LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			grid=inflater.inflate(R.layout.grid_layout, parent, false);
		}else{
			grid = (View)convertView;
		}

		ImageView imageView = (ImageView)grid.findViewById(R.id.image);
		imageView.setImageResource(m_menuIds[position]);

		return grid;
	}
	
	private Integer[] m_menuIds = {
			R.drawable.map_icon
	};
}
