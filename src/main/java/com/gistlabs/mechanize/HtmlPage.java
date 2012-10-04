package com.gistlabs.mechanize;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gistlabs.mechanize.form.Forms;
import com.gistlabs.mechanize.image.Images;
import com.gistlabs.mechanize.link.Links;
import com.gistlabs.mechanize.util.Util;

public class HtmlPage extends Page {

	private Document document;

	public HtmlPage(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
		super(agent, request, response);
	}

	@Override
	protected void loadPage() throws Exception {
		this.document = Jsoup.parse(getInputStream(), getContentEncoding(response), this.uri);
	}

	public Document getDocument() {
		return document;
	}
	
	protected Links loadLinks() {
		Elements links = document.getElementsByTag("a");
		return new Links(this, links);
	}
	
	protected Forms loadForms() {
		Elements forms = document.getElementsByTag("form");
		return new Forms(this, forms);
	}
	
	protected Images loadImages() {
		Elements images = document.getElementsByTag("img");
		return new Images(this, images);
	}

	/**
	 * Returns the title of the page or null.
	 */
	public String getTitle() {
		Element title = Util.findFirstByTag(document, "title");
		return title != null ? title.html() : null; 
	}
	
	/**
	 * Serialize the contents of this page into a string
	 * 
	 * @return
	 */
	public String asString() {
		return getDocument().toString();
	}

}
