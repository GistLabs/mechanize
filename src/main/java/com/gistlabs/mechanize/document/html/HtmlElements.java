/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

/**
 * Collection of all HTML elements of a HtmlPage object and the underlying Jsoup DOM-Document. 
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
//TODO check for extension of PageElements
public class HtmlElements {

	private HtmlDocument page; 
	
	private HtmlElement root;

	//TODO May increase memory footprint when html code changes
	private final Map<org.jsoup.nodes.Node, HtmlNode> elementCache = new HashMap<org.jsoup.nodes.Node, HtmlNode>();
	
	public HtmlElements(HtmlDocument page, Document document) {
		this.page = page;
		this.root = getHtmlElement(document);
	}
	
	/** Returns the root element representing the org.jsoup.Document page element. */ 
	public HtmlElement getRoot() {
		return root;
	}
	
	public HtmlElement find(String csss) {
		return root.find(csss);
	}
	
	@SuppressWarnings("unchecked")
	public List<HtmlElement> findAll(String csss) {
		return (List<HtmlElement>)root.findAll(csss);
	}
	
	public HtmlNode getHtmlNode(org.jsoup.nodes.Node node) {
		if(elementCache.containsKey(node)) {
			return elementCache.get(node);
		}
		else {
			HtmlNode htmlNode = null;
			if(node instanceof Element)
				htmlNode = new HtmlElement(page, (Element)node);
			else if(node instanceof TextNode)
				htmlNode = new HtmlTextNode(page, (TextNode)node);
			else
				htmlNode = new HtmlNode(page, node);
			elementCache.put(node, htmlNode);
			return htmlNode;
		}
	}
	
	public HtmlElement getHtmlElement(Element element) {
		return (HtmlElement)getHtmlNode(element);
	}

	public HtmlTextNode getHtmlTextNode(TextNode node) {
		return (HtmlTextNode)getHtmlNode(node);
	}
	
	@Override
	public String toString() {
		return root.toString();
	}
}
