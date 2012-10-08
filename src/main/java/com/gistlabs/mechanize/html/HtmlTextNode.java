/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.html;

import org.jsoup.nodes.TextNode;


/**
 * Implements a TextNode representation
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class HtmlTextNode extends HtmlNode {
	
	public HtmlTextNode(HtmlPage page, TextNode node) {
		super(page, node);
	}
	
	public TextNode getTextNode() {
		return (TextNode)getNode();
	}
}
