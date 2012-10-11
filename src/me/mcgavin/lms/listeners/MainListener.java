package me.mcgavin.lms.listeners;

import java.util.ArrayList;

import me.mcgavin.lms.Navigator;
import me.mcgavin.lms.WebPage;

public interface MainListener {

	public void onSubjectsLoaded();
	public void onSubjectClicked(ArrayList<Navigator> navigators);
	public void onNavigatorClicked(String url, WebPage webpage);
}
