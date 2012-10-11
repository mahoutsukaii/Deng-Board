package me.mcgavin.lms.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingScreen extends ProgressDialog{

	public LoadingScreen(Context context, String title) {
		super(context);
	//	setContentView(R.layout.loading);
		setCancelable(false);
		setMessage(title);
		// TODO Auto-generated constructor stub
	}


}
