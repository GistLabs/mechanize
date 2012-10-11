/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.image;

import com.gistlabs.mechanize.Node;
import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.PageElement;

/** 
 * Represents an image within a page.  
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Image extends PageElement {
	
	public Image(Page page, Node node) {
		super(page, node);
	}

	/**
	 * Get the image, can then saveTo()
	 */
	public Page get() {
		return node.hasAttribute("src") ? doRequest(getAbsoluteSrc()).get() : null;
	}

	
	/** Returns the absolute url for the given image or null if no src-attribute is provided. */
	public String getAbsoluteSrc() {
		return getNode().hasAttribute("src") ? getNode().getAbsoluteAttribute("src") : null;
	}
}
