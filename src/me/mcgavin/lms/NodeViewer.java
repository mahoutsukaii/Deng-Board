package me.mcgavin.lms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



import me.mcgavin.lms.listeners.DownloadListener;
import me.mcgavin.lms.listeners.MainListener;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class NodeViewer implements OnItemClickListener,DownloadListener {

	private DengBoardActivity activity;
	private MainListener listener;
	private Navigator navigator;
	private WebPage webpage;

	public NodeViewer(DengBoardActivity activity, MainListener listener)
	{
		this.activity = activity;
		this.listener = listener;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		navigator = (Navigator)arg0.getItemAtPosition(arg2);


		if((webpage=activity.session.getWebpage(navigator.getUrl(), this))!=null) {
			sendOnLoad();
		}

		
	}
	public void downloadExtra(String url)
	{
		if(navigator == null)
			navigator = new Navigator(url, "EXTRA");
		if((webpage=activity.session.getWebpage(navigator.getUrl(), this))!=null) {
			sendOnLoad();
		}
	}
	@Override
	public void onDownloadComplete(String url) {
		downloadExtra(url);

	}

	private void sendOnLoad() {
		
		if(navigator.getUrl().contains("webapp")) {
			Pattern p = Pattern.compile("(<div id=\"contentPanel\")([\\s\\S]*)(</html>)", Pattern.MULTILINE);
			Matcher m = p.matcher(webpage.getContent());
			while(m.find())
			{
				webpage.setContent(m.group());
				
			}
		}
		listener.onNavigatorClicked(navigator.getUrl(),webpage);
	}

}
