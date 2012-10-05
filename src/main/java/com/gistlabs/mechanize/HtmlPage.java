/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gistlabs.mechanize.form.Forms;
import com.gistlabs.mechanize.image.Images;
import com.gistlabs.mechanize.link.Links;
import com.gistlabs.mechanize.util.Collections;
import com.gistlabs.mechanize.util.Util;

public class HtmlPage extends Page {
	public static Collection<String> CONTENT_MATCHERS = 
		Collections.collection(
				ContentType.TEXT_HTML.getMimeType(), 
				ContentType.APPLICATION_ATOM_XML.getMimeType(), 
				ContentType.APPLICATION_XHTML_XML.getMimeType(), 
				ContentType.APPLICATION_XML.getMimeType());


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
