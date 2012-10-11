package me.mcgavin.lms;

import android.text.Html;

public class Navigator {
	
	private String url;
	private String name;
	
	public Navigator(String url, String name)
	{
		setUrl(url);
		setName(name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = Html.fromHtml(name).toString();
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		if(!url.contains("http"))
			this.url = "http://app.lms.unimelb.edu.au"+url;
		else
		this.url = url;
	}

}
