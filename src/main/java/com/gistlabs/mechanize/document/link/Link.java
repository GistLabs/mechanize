/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.link;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.documentElements.AbstractDocumentElement;
import com.gistlabs.mechanize.document.node.Node;

/** 
 * Represents a link within a page.  
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Link extends AbstractDocumentElement {
	public Link(Resource page, Node link) {
		super(page, link);
	}

	/**
	 * Follows the link by using the original agent.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Resource> T click() {
		return (T) (hasAttribute("href") ? doRequest(href()).get() : null);
	}
	
	public String href() {
		return hasAttribute("href") ? absoluteUrl(node.getAttribute("href")) : null;
	}
}
