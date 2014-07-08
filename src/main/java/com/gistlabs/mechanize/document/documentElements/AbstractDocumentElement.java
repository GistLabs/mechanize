/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.documentElements;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.requestor.RequestBuilder;
import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public abstract class AbstractDocumentElement implements RequestBuilderFactory<Resource> {
	protected final Resource page;
	protected final Node node;
	
	public AbstractDocumentElement(Resource page, Node node) {
		this.page = page;
		this.node = node;
	}

	public Node getNode() {
		return node;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Resource> T getResource() {
		return (T) page;
	}
	
	@Override
	public RequestBuilder<Resource> doRequest(String uri) {
		return getResource().doRequest(uri);
	}
	
	public boolean hasAttribute(String key) {
		return node.hasAttribute(key);
	}
	
	public String getAttribute(String key) {
		return node.getAttribute(key);
	}

	@Override
	public String absoluteUrl(String uri) {
		return getResource().absoluteUrl(uri);
	}
}
