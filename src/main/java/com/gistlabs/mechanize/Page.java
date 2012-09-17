/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.gistlabs.mechanize.exceptions.MechanizeIOException;
import com.gistlabs.mechanize.form.Forms;
import com.gistlabs.mechanize.image.Images;
import com.gistlabs.mechanize.link.Links;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** Represents an HTML page.  
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
	
	public Page(MechanizeAgent agent, String uri, Document document, String content, HttpRequestBase request, HttpResponse response) {
		this.agent = agent;
		this.uri = uri;
		this.document = document;
		this.originalContent = content;
		this.request = request;
		this.response = response;
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
