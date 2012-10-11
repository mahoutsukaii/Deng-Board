package me.mcgavin.lms;

import org.apache.http.Header;

public class WebPage {

	private String content;
	private Header[] headers;
	public WebPage()
	{
		content = null;
		headers = null;
	}
	public WebPage(String content, Header[] headers) {
		setContent(content);
		setHeaders(headers);
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Header[] getHeaders() {
		return headers;
	}
	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}
}
