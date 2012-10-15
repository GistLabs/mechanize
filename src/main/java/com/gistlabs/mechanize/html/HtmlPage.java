/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.html;

import static com.gistlabs.mechanize.html.query.HtmlQueryBuilder.*;

import java.util.Collection;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.Node;
import com.gistlabs.mechanize.document.Page;
import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;
import com.gistlabs.mechanize.form.Forms;
import com.gistlabs.mechanize.image.Images;
import com.gistlabs.mechanize.link.Links;
import com.gistlabs.mechanize.util.Collections;

public class HtmlPage extends Page {
	public static Collection<String> CONTENT_MATCHERS = 
		Collections.collection(
				ContentType.TEXT_HTML.getMimeType(), 
				ContentType.APPLICATION_ATOM_XML.getMimeType(), 
				ContentType.APPLICATION_XHTML_XML.getMimeType(), 
				ContentType.APPLICATION_XML.getMimeType());

	private HtmlElements htmlElements;
	
	private String baseUri;

	public HtmlPage(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
		super(agent, request, response);
	}

	public HtmlElement getRoot() {
		return htmlElements().getRoot();
	}
	
	@Override
	protected void loadPage() throws Exception {
		Document jsoup = Jsoup.parse(getInputStream(), getContentEncoding(response), getUri());
		setBaseUri(jsoup.head().baseUri());
		this.htmlElements = new HtmlElements(this, jsoup);
	}
	
	private void setBaseUri(String baseUri) {
		if (! this.getUri().equals(baseUri))
			this.baseUri = baseUri;
	}
	
	@Override
	public String getUri() {
		return this.baseUri==null ? super.getUri() : this.baseUri;
	}

	@Override
	protected Links loadLinks() {
		List<? extends Node> links = htmlElements().getAll(byTag("a"));
		return new Links(this, links);
	}
	
	@Override 
	protected Forms loadForms() {
		List<? extends Node> forms = htmlElements().getAll(byTag("form"));
		return new Forms((Resource)this, forms);
	}
	
	@Override
	protected Images loadImages() {
		List<HtmlElement> images = htmlElements().getAll(byTag("img"));
		return new Images(this, images);
	}
	
	
	public HtmlElements htmlElements() {
		if(htmlElements == null)
			try {
				loadPage();
			} catch (Exception e) {
				throw MechanizeExceptionFactory.newException(e);
			}
		return htmlElements;
	}

	/**
	 * Returns the title of the page or null.
	 */
	public String getTitle() {
		HtmlElement title = htmlElements().get(byTag("title"));
		return title != null ? title.getText() : null; 
	}
	
	/**
	 * Serialize the contents of this page into a string
	 * 
	 * @return
	 */
	public String asString() {
		return htmlElements.toString();
	}

}
