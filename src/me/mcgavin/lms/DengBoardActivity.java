package me.mcgavin.lms;

import java.util.ArrayList;

import org.apache.http.Header;

import me.mcgavin.lms.adapters.NavigatorAdapter;
import me.mcgavin.lms.adapters.SubjectAdapter;
import me.mcgavin.lms.listeners.MainListener;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ViewFlipper;


public class DengBoardActivity extends Activity implements MainListener {
	/** Called when the activity is first created. */
	public LoginSession session; //The login session we will use
	private ListView subjectListView; // the view of the subject list
	private ViewFlipper viewFlipper; // the main view port
	private String currentURL; // current URL used for opening externally
	
	/* Called when the activity is created. Set's app to full screen, and starts the login session*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//	setContentView(R.layout.main);


		startSession(false);


	}
	
	/* Initialises the session */
	public void startSession(boolean override)
	{
		viewFlipper = new ViewFlipper(this);
		viewFlipper.addView((LinearLayout)getLayoutInflater().inflate(R.layout.main, null));
		setContentView(viewFlipper);
		if(session == null || override) {
		session = new LoginSession(this);
		session.start();
		}
	}

	/* Called when the LoginSession has completed downloading / arranging the subjects */
	@Override
	public void onSubjectsLoaded() {
		// set up the listView and show it
		SubjectAdapter adapter = new SubjectAdapter(this, R.layout.listview_item_row, session.getSubjects());
		subjectListView = (ListView)findViewById(R.id.subjectlistview);
		subjectListView.addFooterView((View)getLayoutInflater().inflate(R.layout.list_foot, null));
		subjectListView.addHeaderView((View)getLayoutInflater().inflate(R.layout.list_foot, null));
		subjectListView.setAdapter(adapter);
		SubjectViewer subjectViewer = new SubjectViewer(this, this);
		subjectListView.setOnItemClickListener(subjectViewer);

	}

	/* Called when a subject is clicked */
	@Override
	public void onSubjectClicked(ArrayList<Navigator> navigators) {
		LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.navigatorlist, null);
		viewFlipper.addView(linearLayout);
		viewFlipper.setDisplayedChild(1);
		ListView navListView = (ListView)findViewById(R.id.navlistview);
		NavigatorAdapter adapter = new NavigatorAdapter(this, R.layout.navigator_list_row, navigators);
		navListView.addFooterView((View)getLayoutInflater().inflate(R.layout.list_foot, null));
		navListView.addHeaderView((View)getLayoutInflater().inflate(R.layout.list_foot, null));
		navListView.setAdapter(adapter);
		navListView.setOnItemClickListener(new NodeViewer(this,this));
	}

	/* Called when a menu item is clicked in a subject */
	@Override
	public void onNavigatorClicked(String url, WebPage webpage) {

		currentURL = url;
		LinearLayout linearLayout;// = new LinearLayout(this);
		linearLayout = new LinearLayout(this);


		WebView webView = new WebView(this);
		webView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		webView.getSettings().setJavaScriptEnabled(true);
		linearLayout.addView(webView);//(WebView)findViewById(R.id.nodeviewer);
		String contentType = "text/html";
		for(Header header : webpage.getHeaders())
		{
			if(header.getName().equals("Content-Type"))
				contentType = header.getValue().split(";")[0];
		}
		Log.d("content Type", contentType);
		String content = webpage.getContent();
	
		webView.loadDataWithBaseURL(url, content, contentType, "utf-8", null);

		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.substring(url.length()-4).equals(".pdf"))
				{
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					startActivity(intent);
					return true;
				}
				loadNewPage(url);
				return true;
			}
		});
		viewFlipper.addView(linearLayout);
		viewFlipper.setDisplayedChild(viewFlipper.getChildCount()-1);
	}
 /* Loads a new webpage and puts it in a new view */
	private void loadNewPage(String url) 
	{
		NodeViewer node = new NodeViewer(this,this);
		node.downloadExtra(url);
	}
	/* called when the back button is pressed, changes the view to a previous */
	@Override
	public void onBackPressed() {
		if(viewFlipper.getDisplayedChild() > 0) {
			viewFlipper.setDisplayedChild(viewFlipper.getDisplayedChild()-1);
			viewFlipper.removeViewAt(viewFlipper.getDisplayedChild()+1);
		}
		 else {
			 Intent startMain = new Intent(Intent.ACTION_MAIN);
			 startMain.addCategory(Intent.CATEGORY_HOME);
			 startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 startActivity(startMain);
		 }
	}
	
/* Called when the menu button is pressed. Will hide the Load external if no page is loaded. */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.layout.menu, menu);
		if(viewFlipper.getDisplayedChild() < 2) {
			menu.getItem(2).setVisible(false);
			
		}
		
	
		return super.onPrepareOptionsMenu(menu);
	}
	/* Called when an item is selected, and will do the specified action */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemID;
		if((itemID =  item.getItemId())==R.id.menu_logout)
		{
			startSession(true);
		}
		else if (itemID == R.id.menu_quit)
		{
			finish();
		}
		else if (itemID == R.id.menu_load)
		{
			if(viewFlipper.getDisplayedChild() > 1)
			{
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(currentURL));
				startActivity(intent);
			}
		}
		return true;
	}
}