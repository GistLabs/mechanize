/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import com.gistlabs.mechanize.requestor.RequestBuilder;
import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public abstract class PageElement implements RequestBuilderFactory<Page> {
	protected final Page page;
	protected final Node node;
	
	public PageElement(Page page, Node node) {
		this.page = page;
		this.node = node;
	}

	public Node getNode() {
		return node;
	}
	
	public Page getPage() {
		return page;
	}
	
	@Override
	public RequestBuilder<Page> doRequest(String uri) {
		return getPage().doRequest(uri);
	}
	
	public boolean hasAttribute(String key) {
		return node.hasAttribute(key);
	}
	
	public String getAttribute(String key) {
		return node.getAttribute(key);
	}

	@Override
	public String absoluteUrl(String uri) {
		// TODO Auto-generated method stub
		return null;
	}
}
