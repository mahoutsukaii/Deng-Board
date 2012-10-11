package me.mcgavin.lms.dialogs;

import me.mcgavin.lms.R;
import me.mcgavin.lms.listeners.LoginListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;


public class LoginDialog extends Dialog implements OnClickListener{

	private LoginListener loginListener;
	
	public LoginDialog(Context context) {
		super(context);

	}
	
	public void setLoginListener(LoginListener listener) {
		this.loginListener = listener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		setTitle("Log in the the LMS");
		setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				loginListener.onLoginCancel();
				
			}});
		Button loginButton = (Button)findViewById(R.id.loginButton);
		loginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
	
		EditText username = (EditText)findViewById(R.id.username);
		EditText password = (EditText)findViewById(R.id.password);
		
		loginListener.onLoginClick(username.getText().toString(), password.getText().toString());
	}

}
