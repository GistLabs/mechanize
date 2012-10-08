/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.link;

import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.PageElement;
import com.gistlabs.mechanize.html.HtmlElement;

/** 
 * Represents a link within a page.  
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Link extends PageElement {
	public Link(Page page, HtmlElement link) {
		super(page, link);
	}

	/**
	 * Follows the link by using the original agent.
	 */
	public Page click() {
		return hasAttribute("href") ? doRequest(href()).get() : null;
	}
	
	public String href() {
		return hasAttribute("href") ? element.getAbsoluteUrl("href") : null;
	}
}
