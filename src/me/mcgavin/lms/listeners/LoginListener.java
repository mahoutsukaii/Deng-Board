package me.mcgavin.lms.listeners;

public interface LoginListener {
	
	public void onLoginCancel();
	public void onLoginClick(String username, String password);
}
