/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html;

import java.util.List;

import org.jsoup.nodes.TextNode;

import com.gistlabs.mechanize.util.Util;


/**
 * Implements a TextNode representation
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class HtmlTextNode extends HtmlNode {
	
	public HtmlTextNode(HtmlDocument page, TextNode node) {
		super(page, node);
	}
	
	public TextNode getJsoupTextNode() {
		return (TextNode)getJsoupNode();
	}
	
	@Override
	public String getValue() {
		return getAttribute(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TEXT);
	}
	
	@Override
	public String getAttribute(String attributeKey) {
		if(attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TEXT))
			return getJsoupTextNode().text();
		else
			return super.getAttribute(attributeKey);
	}
	
	@Override
	public List<String> getAttributeNames() {
		return getAttributeNamesOfJSoupTextNode(getJsoupTextNode());
	}

	public static String getAttributeValueOfJSoupTextNode(TextNode textNode,
			String attribute) {
		if(attribute.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TEXT))
			return textNode.text();
		else
			return null;
	}
	
	public static List<String> getAttributeNamesOfJSoupTextNode(TextNode textNode) {
		List<String> names = Util.newEmptyList();
		names.add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TEXT);
		return names;
	}
}
