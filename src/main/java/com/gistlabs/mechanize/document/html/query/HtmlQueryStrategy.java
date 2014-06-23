/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.query;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import com.gistlabs.mechanize.document.html.HtmlElement;
import com.gistlabs.mechanize.document.html.HtmlSpecialAttributes;
import com.gistlabs.mechanize.document.html.HtmlTextNode;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.document.query.QueryStrategy;
import com.gistlabs.mechanize.util.Util;

@Deprecated
public class HtmlQueryStrategy implements QueryStrategy {

	@Override
	public String getAttributeValue(Object object, String attribute) {
		if(object instanceof Node)
			return ((Node)object).getAttribute(attribute);
		else if(object instanceof Element) {
			return HtmlElement.getAttributeValueOfJSoupElement((Element)object, attribute);
		}
		else if(object instanceof TextNode) {
			return HtmlTextNode.getAttributeValueOfJSoupTextNode((TextNode)object, attribute);
		}
		else
			return null;
	}

	@Override
	public List<String> getAttributeNames(Object object) {
		if(object instanceof Node) {
			return ((Node)object).getAttributeNames();
		}
		else if(object instanceof Element) {
			return HtmlElement.getAttributeNamesForJSoupElement((Element)object);
		}
		else if(object instanceof TextNode) {
			return HtmlTextNode.getAttributeNamesOfJSoupTextNode((TextNode)object);
		}
		else
			return Util.newEmptyList();
	}
	
	@Override
	public boolean isMultipleValueAttribute(Object object, String attributeKey) {
		if(object instanceof Node && attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_CLASS_NAMES))
			return true;
		else
			return false;
	}
}