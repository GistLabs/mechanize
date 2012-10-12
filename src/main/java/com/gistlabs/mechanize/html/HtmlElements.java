/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import com.gistlabs.mechanize.html.query.HtmlQueryStrategy;
import com.gistlabs.mechanize.query.AbstractQuery;
import com.gistlabs.mechanize.query.Query;
import com.gistlabs.mechanize.query.QueryStrategy;

/**
 * Collection of all HTML elements of a HtmlPage object and the underlying Jsoup DOM-Document. 
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
//TODO check for extension of PageElements
public class HtmlElements {

	private HtmlPage page; 
	
	private HtmlElement root;

	//TODO May increase memory footprint when html code changes
	private final Map<org.jsoup.nodes.Node, HtmlNode> elementCache = new HashMap<org.jsoup.nodes.Node, HtmlNode>();
	
	public HtmlElements(HtmlPage page, Document document) {
		this.page = page;
		this.root = getHtmlElement(document);
	}
	
	/** Returns the root element representing the org.jsoup.Document page element. */ 
	public HtmlElement getRoot() {
		return root;
	}
	
	/** Returns the first element matching the query in a deep first left right search. */ 
	public HtmlElement get(AbstractQuery<?> query) {
		return root.get(query);
	}
	
	/** Returns the elements matching the query in a deep first left right search. */ 
	public List<HtmlElement> getAll(AbstractQuery<?> query) {
		return root.getAll(query);
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

	public static HtmlElement getFormHtmlNodes(HtmlPage page, Query query, List<? extends HtmlNode> nodes) {
		List<org.jsoup.nodes.Node> jsoupNodes = new ArrayList<org.jsoup.nodes.Node>();
		for(HtmlNode node : nodes)
			jsoupNodes.add(node.getJsoupNode());
		return get(page, query, jsoupNodes);
	}
	
	public static HtmlElement get(HtmlPage page, AbstractQuery<?> query, List<org.jsoup.nodes.Node> nodes) {
		return get(page, query, new HtmlQueryStrategy(), nodes);
	}
	
	public static HtmlElement get(HtmlPage page, AbstractQuery<?> query, QueryStrategy queryStrategy, List<org.jsoup.nodes.Node> nodes) {
		for(org.jsoup.nodes.Node node : nodes) {
			if(node instanceof Element) {
				if(query.matches(queryStrategy, (Element)node))
					return page.htmlElements().getHtmlElement((Element)node);
				HtmlElement result = get(page, query, queryStrategy, node.childNodes());
				if(result != null)
					return result;
			}
		}
		return null;
	}

	public static void getAll(HtmlPage page, List<HtmlElement> result, AbstractQuery<?> query, List<org.jsoup.nodes.Node> nodes) {
		getAll(page, result, query, new HtmlQueryStrategy(), nodes);
	}
	
	public static void getAll(HtmlPage page, List<HtmlElement> result, AbstractQuery<?> query, QueryStrategy queryStrategy, List<org.jsoup.nodes.Node> nodes) {
		for(org.jsoup.nodes.Node node : nodes) {
			if(node instanceof Element) {
				if(query.matches(queryStrategy, (Element)node))
					result.add(page.htmlElements().getHtmlElement((Element)node));
				getAll(page, result, query, queryStrategy, node.childNodes());
			}
		}
	}
}
