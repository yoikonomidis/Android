package kidiya.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.kidiyaservice.R;

public class ImageAdapter extends BaseAdapter {

	private Context m_context;

	public ImageAdapter(Context c) {
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
