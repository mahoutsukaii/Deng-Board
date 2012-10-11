package me.mcgavin.lms.adapters;

import java.util.ArrayList;

import me.mcgavin.lms.Navigator;
import me.mcgavin.lms.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NavigatorAdapter extends ArrayAdapter<Navigator>{

	Context context;
	int layoutResourceId;
	ArrayList<Navigator> data = null;

	public NavigatorAdapter(Context context, int layoutResourceId,ArrayList<Navigator> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		NavigatorHolder holder = null;
		
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
		
		}

		else {
			holder = (NavigatorHolder)row.getTag();
		}
		holder = new NavigatorHolder();
		holder.name = (TextView)row.findViewById(R.id.navname);
		holder.name.setTypeface(Typeface.createFromAsset(context.getAssets(), "EraserRegular.ttf"));
	//	row.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, (parent.getHeight()-20)/(getCount())));
		Navigator navigator = data.get(position);
		holder.name.setText(navigator.getName());


		return row;
		
	}
	
	  static class NavigatorHolder
	    {
	        TextView name;
	    }
}
