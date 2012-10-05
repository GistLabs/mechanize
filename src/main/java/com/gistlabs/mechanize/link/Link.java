/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.link;

import org.jsoup.nodes.Element;

import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.PageElement;

/** 
 * Represents a link within a page.  
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Link extends PageElement {
	public Link(Page page, Element link) {
		super(page, link);
	}

	/**
	 * Follows the link by using the original agent.
	 */
	public Page click() {
		if(hasAttribute("href"))
			return doRequest(href()).get();
		return null;
	}
	
	public String href() {
		if(hasAttribute("href")) {
			return element.absUrl("href");
		}
		return null;
	}
}
