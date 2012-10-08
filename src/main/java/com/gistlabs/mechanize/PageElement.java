/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import com.gistlabs.mechanize.html.HtmlElement;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public abstract class PageElement implements RequestBuilderFactory {
	protected final Page page;
	protected final HtmlElement element;
	
	public PageElement(Page page, HtmlElement element) {
		this.page = page;
		this.element = element;
	}

	protected HtmlElement getElement() {
		return element;
	}
	
	public Page getPage() {
		return page;
	}
	
	@Override
	public RequestBuilder doRequest(String uri) {
		return getPage().doRequest(uri);
	}
	
	public boolean hasAttribute(String key) {
		return element.hasAttribute(key);
	}
	
	public String getAttribute(String key) {
		return element.getAttribute(key);
	}
}
