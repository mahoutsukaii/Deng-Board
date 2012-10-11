package me.mcgavin.lms;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.mcgavin.lms.listeners.DownloadListener;
import me.mcgavin.lms.listeners.MainListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class SubjectViewer implements OnItemClickListener, DownloadListener {

	private String pageURL = "http://app.lms.unimelb.edu.au/webapps/blackboard/content/courseMenu.jsp?course_id=";
	private DengBoardActivity activity;
	private Subject subject;
	private WebPage webpage;
	private MainListener listener;
	
	private ArrayList<Navigator> navigators;
	
	public SubjectViewer(DengBoardActivity activity, MainListener listener)
	{
		
		this.activity = activity;
		this.listener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		navigators = new ArrayList<Navigator>();
		subject = (Subject)arg0.getItemAtPosition(position);
		if((webpage=activity.session.getWebpage(pageURL+subject.getActualCode(), this))!=null)
			buildView();

	}

	@Override
	public void onDownloadComplete(String url) {
		webpage = activity.session.getWebpage(url, null);
		buildView();

	}
	
	public void buildView() {
		webpage.setContent( webpage.getContent().substring(webpage.getContent().indexOf("class=\"courseMenu\">")));
		
		Pattern p = Pattern.compile("(href=\")(.*?)(\".*\">)(.*?)(</span>)"); // Dont you just love regex!?
		Matcher m = p.matcher(webpage.getContent());
		while(m.find())
		{
			String url = m.group(2);
	//		if(!url.contains("http")) {
	//			url = "http://app.lms.unimelb.edu.au"+url;
	//		}
			navigators.add(new Navigator(url, m.group(4)));
			//url group = 2
			//name = 4
		}
		
		listener.onSubjectClicked(navigators);
		
	}
	

}
