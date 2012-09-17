/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import org.jsoup.nodes.Element;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public abstract class PageElement {
	protected final Page page;
	protected final Element element;
	
	public PageElement(Page page, Element element) {
		this.page = page;
		this.element = element;
	}

	public Element getElement() {
		return element;
	}
	
	public Page getPage() {
		return page;
	}
}
