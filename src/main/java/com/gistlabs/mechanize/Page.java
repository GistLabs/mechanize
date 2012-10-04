/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static com.gistlabs.mechanize.query.QueryBuilder.byIdOrClass;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
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
import com.gistlabs.mechanize.exceptions.MechanizeIOException;
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

	private final ByteArrayOutputStream originalContent;
	private final Document document;

	private Links links;
	private Forms forms;
	private Images images;
	
	public Page(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
		this.agent = agent;
		this.request = request;
		this.response = response;

		this.uri = inspectUri(request, response);
		
		try {
			originalContent = new ByteArrayOutputStream(getIntContentLength(response));
			InputStream in = new CopyInputStream(response.getEntity().getContent(), originalContent);
			this.document = Jsoup.parse(in, JsoupDataUtil.getCharsetFromContentType(response.getEntity().getContentType()), this.uri);
		} catch(RuntimeException rex) {
			throw rex;
		} catch(Throwable th) {
			throw new MechanizeException(th);
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
			Elements links = document.getElementsByTag("a");
			this.links = new Links(this, links);
		}
		return this.links;
	}

	/**
	 *  Query for a matching form, find first match by either id or by class attributes.
	 * 
	 * @param query wrapped with byIdOrClass()
	 * @return first Form found
	 */
	public Form form(String query) {
		return forms().get(byIdOrClass(query));
	}

	public Forms forms() {
		if(this.forms == null) {
			Elements forms = document.getElementsByTag("form");
			this.forms = new Forms(this, forms);
		}
		return this.forms;
	}
	
	public Images images() {
		if(this.images == null) {
			Elements images = document.getElementsByTag("img");
			this.images = new Images(this, images);
		}
		return this.images;
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
	 * @throws IllegalArgumentException If file already exists */
	public void saveToFile(File file) {
		if(file.exists())
			throw new IllegalArgumentException("File '" + file.toString() + "' already exists.");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(getDocument().outerHtml());
		} catch (IOException e) {
			throw new MechanizeIOException(e);
		}
		finally {
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
	}
}
