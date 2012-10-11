package me.mcgavin.lms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import android.os.AsyncTask;
import android.util.Base64;
import me.mcgavin.lms.dialogs.AlertDialogBox;
import me.mcgavin.lms.dialogs.LoadingScreen;
import me.mcgavin.lms.dialogs.LoginDialog;
import me.mcgavin.lms.listeners.DownloadListener;
import me.mcgavin.lms.listeners.LoginListener;
import me.mcgavin.lms.listeners.MainListener;

public class LoginSession implements LoginListener {

	private HttpClient httpClient;
	private DengBoardActivity activity;
	private LoginDialog loginDialog;
	private MainListener mainListener;
	private HashMap<String, WebPage> webpages;
	private ArrayList<Subject> subjects;
	private String name;

	public LoginSession(DengBoardActivity activity) {
		this.activity = activity;
		mainListener = activity;

		httpClient = new DefaultHttpClient();
		webpages = new HashMap<String, WebPage>();
		subjects = new ArrayList<Subject>();

	}
	public void start() {
		loginDialog = new LoginDialog(activity);
		loginDialog.setLoginListener(this);
		loginDialog.show();

	}
	
	public void deletePage(String url)
	{
		webpages.remove(url);
	}
	
	public WebPage getWebpage(final String url, final DownloadListener listener) {
		if(!webpages.containsKey(url))
		{
			AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>(){

				LoadingScreen ls;
				@Override
				protected String doInBackground(String... params) {
					return DownloadWebpage(url).getContent();
					
				}
				@Override
				protected void onPreExecute() {
					ls = new LoadingScreen(activity, "Loading ...");
					ls.show();
					super.onPreExecute();
				}
				@Override
				protected void onPostExecute(String result) {
					ls.dismiss();
					listener.onDownloadComplete(url);
					super.onPostExecute(result);
				}};
				 task.execute();
				return null;
		} else
		return webpages.get(url);

			
	}


	public ArrayList<Subject> getSubjects() {
		return this.subjects;
	}
	private void downloadSubjects() {
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
		LoadingScreen loadingScreen;
		@Override
			protected String doInBackground(String... params) {
			
			String xmlData = DownloadWebpage("http://app.lms.unimelb.edu.au/webapps/portal/execute/tabs/tabAction?action=refreshAjaxModule&modId=_4_1&tabId=_1_1&tab_tab_group_id=_41_1").getContent();
			Pattern p = Pattern.compile("id%3D_(.*?)</a>");
			Matcher m = p.matcher(xmlData);
			while(m.find())
			{
				String actualCode = "";
				for(int i = 0; i < m.group(1).length(); i++)
				{
					if(m.group(1).charAt(i) =='%')
					{
						actualCode = m.group(1).substring(0, i);
						break;
					}
				}
				Subject subject = new Subject();
				String[] details = m.group(1).substring(actualCode.length()+25).split(":");
				subject.setName(details[1]);
				subject.setCode(details[0]);
				subject.setActualCode(actualCode);
				subjects.add(subject);
			}

			return null;
			}
	
		@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				loadingScreen = new LoadingScreen(activity, "Downloading subjects..");
				loadingScreen.show();
			}
		@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					loadingScreen.dismiss();
					onSubjectsDownloaded();
				}
		}; 
		task.execute();
		}

	private void onSubjectsDownloaded() {
		loginDialog.dismiss();
		mainListener.onSubjectsLoaded();
	}

	@Override
	public void onLoginClick(final String username, final String password) {

		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

			LoadingScreen loadingScreen;
			@Override
			protected String doInBackground(String... arg0) {
				try {
					HttpGet httpget;
					HttpResponse response;
					HttpEntity entity;
					HttpPost httpost = new HttpPost("https://app.lms.unimelb.edu.au/webapps/login/index.php");
					httpget = new HttpGet("http://app.lms.unimelb.edu.au/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_41_19");
					List <NameValuePair> nvps = new ArrayList <NameValuePair>();
					nvps.add(new BasicNameValuePair("user_id", username));
					nvps.add(new BasicNameValuePair("encoded_pw", Base64.encodeToString(password.getBytes(), 0)));
					httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
					response = httpClient.execute(httpost);
					entity = response.getEntity();
					entity.consumeContent();

					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					//LETS GET THEIR NAME!
					String httpNonsense = httpClient.execute(httpget, responseHandler);
					if(httpNonsense.contains("<title>Welcome,"))
					{
						Pattern p = Pattern.compile("<title>(.*?)</title>");
						Matcher m = p.matcher(httpNonsense);
						while(m.find())
						{	
							return m.group(0);
						}
					}
					else
					{
						return null;

					}
				}
				catch (Exception e) {

				}
				return null;
			}
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loadingScreen = new LoadingScreen(activity, "Logging in...");
				loadingScreen.show();

			}
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				loadingScreen.dismiss();
				onLoginComplete(result);
			}
		};

		task.execute();

	}

	private void onLoginComplete(String name) {
		this.name = name;
		if(name == null) {
			AlertDialogBox alert = new AlertDialogBox(activity, "Log in failed, please try again.");
			alert.show();
		}
		else downloadSubjects();
	}

	private WebPage DownloadWebpage(String url) {
		try {
			
			HttpResponse response = httpClient.execute(new HttpGet(url));
			WebPage webpage = new WebPage();
			webpage.setContent(EntityUtils.toString(response.getEntity()));
			webpage.setHeaders(response.getAllHeaders());
			webpages.put(url, webpage);

		} catch (Exception e) {
			
			webpages.put(url, new WebPage("Bad url: "+ url, null));
			e.printStackTrace();
		} 
		return webpages.get(url);

	}
	@Override
	public void onLoginCancel() {
		activity.finish();
	}
	public String getName() {
		return name;
	}

	
}
