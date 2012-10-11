package me.mcgavin.lms.dialogs;


import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogBox extends AlertDialog   {

	public AlertDialogBox(Context context, String message) {
		super(context);
		setMessage(message);
	}




}
