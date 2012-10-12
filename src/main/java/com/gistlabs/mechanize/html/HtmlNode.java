/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.html;

import java.util.ArrayList;
import java.util.List;

import com.gistlabs.mechanize.Node;
import com.gistlabs.mechanize.query.HtmlQuery;
import com.gistlabs.mechanize.util.Util;

/**
 * Represents a node.
 * 
 * <p>Note: The HtmlNode.toString() returns the HTML representation of this node and all its child nodes being the 
 *    exactly same as org.jsoup.nodes.Node.toString().</p>
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class HtmlNode implements Node {
	
	private final HtmlPage page;
	private final org.jsoup.nodes.Node node;

	public HtmlNode(HtmlPage page, org.jsoup.nodes.Node node) {
		this.page = page;
		this.node = node;
	}
	
	public org.jsoup.nodes.Node getJsoupNode() {
		return node;
	}
	
	public HtmlPage getPage() {
		return page;
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public String getValue() {
		return null;
	}
	
	@Override
	public boolean hasAttribute(String attributeKey) {
		return false;
	}

	@Override
	public String getAttribute(String attributeKey) {
		return null;
	}
	
	@Override
	public List<String> getAttributeNames() {
		return Util.newEmptyList();
	}
	
	@Override
	public List<HtmlNode> getChildren() {
		List<HtmlNode> result = new ArrayList<HtmlNode>();
		for(org.jsoup.nodes.Node child : node.childNodes()) 
			result.add(getPage().htmlElements().getHtmlNode(child));
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HtmlElement get(HtmlQuery query) {
		return HtmlElements.get(getPage(), query, node.childNodes());
	}

	@Override
	public List<HtmlElement> getAll(HtmlQuery query) {
		List<HtmlElement> result = new ArrayList<HtmlElement>();
		HtmlElements.getAll(getPage(), result, query, node.childNodes());
		return result;
	}
	
	
	@Override
	public String toString() {
		return node.toString();
	}
}
