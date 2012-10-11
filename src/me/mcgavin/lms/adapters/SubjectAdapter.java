package me.mcgavin.lms.adapters;

import java.util.ArrayList;

import me.mcgavin.lms.R;
import me.mcgavin.lms.Subject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SubjectAdapter extends ArrayAdapter<Subject>{

	Context context;
	int layoutResourceId;
	ArrayList<Subject> data = null;

	public SubjectAdapter(Context context, int layoutResourceId,ArrayList<Subject> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		SubjectHolder holder = null;
		
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			

		

		}
		
		else {
			holder = (SubjectHolder)row.getTag();
		}
		holder = new SubjectHolder();
		holder.name = (TextView)row.findViewById(R.id.subname);
		holder.code = (TextView)row.findViewById(R.id.subcode);
		
		holder.name.setTypeface(Typeface.createFromAsset(context.getAssets(), "EraserRegular.ttf"));
		holder.code.setTypeface(Typeface.createFromAsset(context.getAssets(), "EraserRegular.ttf"));
		
		Subject subject = data.get(position);
		holder.name.setText(subject.getName());
		holder.code.setText(subject.getCode());
		
		row.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, (parent.getHeight()-5)/getCount()));
		return row;
		
	}
	
	  static class SubjectHolder
	    {
	        TextView name;
	        TextView code;
	    }
}
