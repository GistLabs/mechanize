/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.gistlabs.mechanize.exceptions.MechanizeIOException;
import com.gistlabs.mechanize.form.Forms;
import com.gistlabs.mechanize.image.Images;
import com.gistlabs.mechanize.link.Links;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** Represents an HTML Page.  
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Page {
	private final MechanizeAgent agent;
	private final String originalContent;
	private final String uri;
	private final Document document;
	private final HttpRequestBase request;
	private final HttpResponse response;
	
	private Links links;
	private Forms forms;
	private Images images;
	
	public Page(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) throws IOException, UnsupportedEncodingException {
		this.agent = agent;
		this.request = request;
		this.response = response;
		
		this.originalContent = getContent(response);
		this.uri = getBaseUri(request, response);
		
		this.document = Jsoup.parse(this.originalContent, this.uri);
	}

	private String getBaseUri(HttpRequestBase request2, HttpResponse response2) {
		String baseUri = request.getURI().toString();
		Header contentLocation = Util.findHeader(response, "content-location");
		if(contentLocation != null && contentLocation.getValue() != null)
			baseUri = contentLocation.getValue();
		return baseUri;
	}

	private String getContent(HttpResponse response) throws IOException,
			UnsupportedEncodingException {
		InputStream in = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, getEncoding(response)));
		String line;
		StringBuilder contentBuilder = new StringBuilder();
		while((line = reader.readLine()) != null) {
			contentBuilder.append(line);
			contentBuilder.append('\n');
		}
		return contentBuilder.toString();
	}

	
	private String getEncoding(HttpResponse response) {
		Header encoding = response.getEntity().getContentEncoding();
		return encoding != null ? encoding.getValue() : Charset.defaultCharset().name();
	}

	public Links links() {
		if(this.links == null) {
			Elements links = document.getElementsByTag("a");
			this.links = new Links(this, links);
		}
		return this.links;
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
	
	public String getContentType() {
		return response.getEntity().getContentType().getValue();
	}
	
	/** Returns the title of the page or null. */
	public String getTitle() {
		Element title = Util.findFirstByTag(document, "title");
		return title != null ? title.html() : null; 
	}
	
	public int size() {
		return originalContent.length();
	}
	
	public String getOriginalContent() {
		return originalContent;
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
