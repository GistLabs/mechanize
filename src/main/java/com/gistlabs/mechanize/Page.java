/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static com.gistlabs.mechanize.query.QueryBuilder.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gistlabs.mechanize.exceptions.MechanizeException;
import com.gistlabs.mechanize.form.Form;
import com.gistlabs.mechanize.form.Forms;
import com.gistlabs.mechanize.image.Images;
import com.gistlabs.mechanize.link.Link;
import com.gistlabs.mechanize.link.Links;
import com.gistlabs.mechanize.util.CopyInputStream;
import com.gistlabs.mechanize.util.JsoupDataUtil;
import com.gistlabs.mechanize.util.Util;

/** Represents an HTML page.  
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Page implements RequestBuilderFactory {
	private final MechanizeAgent agent;
	private final String uri;
	private final HttpRequestBase request;
	private final HttpResponse response;

	private ByteArrayOutputStream originalContent;
	private Document document;

	private Links links;
	private Forms forms;
	private Images images;
	
	public Page(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
		this.agent = agent;
		this.request = request;
		this.response = response;
		this.uri = inspectUri(request, response);
		
		try {
			loadPage();
		} catch(RuntimeException rex) {
			throw rex;
		} catch(Throwable th) {
			throw new MechanizeException(th);
		}
	}

	protected void loadPage() throws IOException {
		this.document = Jsoup.parse(getInputStream(), getContentEncoding(response), this.uri);
	}

	private String getContentEncoding(HttpResponse response) {
		return JsoupDataUtil.getCharsetFromContentType(response.getEntity().getContentType());
	}

	/**
	 * This will return (and cache) the response entity content input stream, or return a stream from the previously cached value.
	 * 
	 * @return
	 * @throws IOException
	 */
	private InputStream getInputStream() throws IOException {
		if (this.originalContent==null) {
			this.originalContent = new ByteArrayOutputStream(getIntContentLength(this.response));
			return new CopyInputStream(response.getEntity().getContent(), this.originalContent);
		} else { // use cached data
			return new ByteArrayInputStream(this.originalContent.toByteArray());
		}
	}

	private int getIntContentLength(HttpResponse response) {
		long longLength = response.getEntity().getContentLength();
		if (longLength<0)
			return 0;
		else if (longLength>Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		else
			return (int)longLength;
	}

	private String inspectUri(HttpRequestBase request, HttpResponse response) {
		Header contentLocation = Util.findHeader(response, "content-location");
		if(contentLocation != null && contentLocation.getValue() != null)
			return contentLocation.getValue();
		else
			return request.getURI().toString();
	}

	@Override
	public RequestBuilder doRequest(String uri) {
		return getAgent().doRequest(uri);
	}

	/**
	 *  Query for a matching link, find first match by either id or by class attributes.
	 * 
	 * @param query wrapped with byIdOrClass()
	 * @return first Link found
	 */
	public Link link(String query) {
		return links().get(byIdOrClass(query));
	}
	
	public Links links() {
		if(this.links == null) {
			this.links = loadLinks();
		}
		return this.links;
	}

	protected Links loadLinks() {
		Elements links = document.getElementsByTag("a");
		return new Links(this, links);
	}

	/**
	 *  Query for a matching form, find first match by either id or by class attributes.
	 * 
	 * @param query wrapped with byIdOrClass()
	 * @return first Form found
	 */
	public Form form(String query) {
		return forms().get(byIdOrClassOrName(query));
	}

	public Forms forms() {
		if(this.forms == null) {
			this.forms = loadForms();
		}
		return this.forms;
	}

	protected Forms loadForms() {
		Elements forms = document.getElementsByTag("form");
		return new Forms(this, forms);
	}
	
	public Images images() {
		if(this.images == null) {
			this.images = loadImages();
		}
		return this.images;
	}

	protected Images loadImages() {
		Elements images = document.getElementsByTag("img");
		return new Images(this, images);
	}
	
	public Document getDocument() {
		return document;
	}
	
	public String getUri() {
		return uri;
	}
	
	/** Returns the title of the page or null. */
	public String getTitle() {
		Element title = Util.findFirstByTag(document, "title");
		return title != null ? title.html() : null; 
	}
	
	public int size() {
		return originalContent.size();
	}
	
	public HttpRequestBase getRequest() {
		return request;
	}
	
	public HttpResponse getResponse() {
		return response;
	}

	public MechanizeAgent getAgent() {
		return agent;
	}

	/** Writes the page document content to file. 
	 * @throws IOException 
	 * @throws IllegalArgumentException If file already exists
	 */
	public void saveTo(File file) throws IOException {
		if(file.exists())
			throw new IllegalArgumentException("File '" + file.toString() + "' already exists.");
		
		Util.copy(getInputStream(), new FileOutputStream(file));
	}
}
