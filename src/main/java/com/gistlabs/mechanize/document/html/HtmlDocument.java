/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html;

import java.util.Collection;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gistlabs.mechanize.document.html.form.Forms;
import com.gistlabs.mechanize.document.html.image.Images;
import com.gistlabs.mechanize.document.link.Links;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;
import com.gistlabs.mechanize.impl.MechanizeAgent;
import com.gistlabs.mechanize.util.Collections;
import com.gistlabs.mechanize.util.apache.ContentType;

/**
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class HtmlDocument extends com.gistlabs.mechanize.document.Document {
	public static Collection<String> CONTENT_MATCHERS =
			Collections.collection(
					ContentType.TEXT_HTML.getMimeType(),
					ContentType.APPLICATION_ATOM_XML.getMimeType(),
					ContentType.APPLICATION_XHTML_XML.getMimeType(),
					ContentType.APPLICATION_XML.getMimeType());

	private HtmlElements htmlElements;

	private String baseUri;

	public HtmlDocument(final MechanizeAgent agent, final HttpRequestBase request, final HttpResponse response) {
		super(agent, request, response);
	}

	@Override
	public HtmlElement getRoot() {
		return htmlElements().getRoot();
	}

	@Override
	public HtmlElement find(String csss) {
		return (HtmlElement)super.find(csss);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends HtmlElement> findAll(String csss) {
		return (List<? extends HtmlElement>) super.findAll(csss);
	}
	
	@Override
	protected void loadPage() throws Exception {
		Document jsoup = Jsoup.parse(getInputStream(), getContentEncoding(response), getUri());
		setBaseUri(jsoup.head().baseUri());
		this.htmlElements = new HtmlElements(this, jsoup);
	}

	private void setBaseUri(final String baseUri) {
		if (! this.getUri().equals(baseUri))
			this.baseUri = baseUri;
	}

	@Override
	public String getUri() {
		return this.baseUri==null ? super.getUri() : this.baseUri;
	}

	@Override
	protected Links loadLinks() {
		List<? extends Node> links = htmlElements().findAll("a");
		return new Links(this, links);
	}

	@Override
	protected Forms loadForms() {
		List<? extends Node> forms = htmlElements().findAll("form");
		return new Forms(this, forms);
	}

	@Override
	protected Images loadImages() {
		List<HtmlElement> images = htmlElements().findAll("img");
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
	@Override
	public String getTitle() {
		HtmlElement title = htmlElements().find("title");
		return title != null ? title.getText() : null;
	}

	/**
	 * Serialize the contents of this page into a string
	 * 
	 * @return
	 */
	@Override
	public String asString() {
		return htmlElements.toString();
	}

}
