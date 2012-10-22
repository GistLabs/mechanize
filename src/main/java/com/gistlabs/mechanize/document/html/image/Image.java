/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.image;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.document.pageElements.PageElement;

/** 
 * Represents an image within a page.  
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Image extends PageElement {
	
	public Image(Resource page, Node node) {
		super(page, node);
	}

	/**
	 * Get the image, can then saveTo()
	 */
	public Resource get() {
		return node.hasAttribute("src") ? doRequest(getAbsoluteSrc()).get() : null;
	}

	
	/** Returns the absolute url for the given image or null if no src-attribute is provided. */
	public String getAbsoluteSrc() {
		return getNode().hasAttribute("src") ? absoluteUrl(getNode().getAttribute("src")) : null;
	}
}
