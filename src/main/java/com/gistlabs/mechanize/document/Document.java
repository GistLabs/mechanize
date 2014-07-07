/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document;

import static com.gistlabs.mechanize.util.css.CSSHelper.byIdOrClass;
import static com.gistlabs.mechanize.util.css.CSSHelper.byIdOrClassOrName;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.gistlabs.mechanize.Mechanize;
import com.gistlabs.mechanize.AbstractResource;
import com.gistlabs.mechanize.document.html.form.Form;
import com.gistlabs.mechanize.document.html.form.Forms;
import com.gistlabs.mechanize.document.html.image.Images;
import com.gistlabs.mechanize.document.link.Link;
import com.gistlabs.mechanize.document.link.Links;
import com.gistlabs.mechanize.document.node.Node;

/**
 * Represents a single or multiple-page document having a root node.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public abstract class Document extends AbstractResource {
	private Links links;
	private Forms forms;
	private Images images;

	public Document(Mechanize agent, HttpRequestBase request, HttpResponse response) {
		super(agent, request, response);
	}

	/**
	 * Returns the root node of the document
	 * @return
	 */
	public abstract Node getRoot();
	
	public Node find(String csss) {
		return getRoot().find(csss);
	}
	
	public List<? extends Node> findAll(String csss) {
		return getRoot().findAll(csss);
	}

	/**
	 *  Query for a matching link, find first match by either id or by class attributes.
	 * 
	 * @param query wrapped with byIdOrClass()
	 * @return first Link found
	 */
	public Link link(String query) {
		return links().find(byIdOrClass(query));
	}
	
	public Links links() {
		if(this.links == null) {
			this.links = loadLinks();
		}
		return this.links;
	}

	protected Links loadLinks() {
		return new Links(this, null);
	}

	/**
	 *  Query for a matching form, find first match by either id or by class attributes.
	 * 
	 * @param query wrapped with byIdOrClass()
	 * @return first Form found
	 */
	public Form form(String query) {
		return forms().find(byIdOrClassOrName(query));
	}

	public Forms forms() {
		if(this.forms == null) {
			this.forms = loadForms();
		}
		return this.forms;
	}

	protected Forms loadForms() {
		return new Forms(this, null);
	}

	public Images images() {
		if(this.images == null) {
			this.images = loadImages();
		}
		return this.images;
	}

	protected Images loadImages() {
		return new Images(this, null);
	}

}
