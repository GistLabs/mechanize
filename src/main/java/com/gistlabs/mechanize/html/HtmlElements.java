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

import com.gistlabs.mechanize.Node;
import com.gistlabs.mechanize.query.HtmlQuery;
import com.gistlabs.mechanize.query.QueryStrategy;
import com.gistlabs.mechanize.util.Util;

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
	public HtmlElement get(HtmlQuery query) {
		return root.get(query);
	}
	
	/** Returns the elements matching the query in a deep first left right search. */ 
	public List<HtmlElement> getAll(HtmlQuery query) {
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

	public static HtmlElement getFormHtmlNodes(HtmlPage page, HtmlQuery query, List<? extends HtmlNode> nodes) {
		List<org.jsoup.nodes.Node> jsoupNodes = new ArrayList<org.jsoup.nodes.Node>();
		for(HtmlNode node : nodes)
			jsoupNodes.add(node.getJsoupNode());
		return get(page, query, jsoupNodes);
	}
	
	public static HtmlElement get(HtmlPage page, HtmlQuery query, List<org.jsoup.nodes.Node> nodes) {
		return get(page, query, new HtmlQueryStrategy(), nodes);
	}
	
	public static HtmlElement get(HtmlPage page, HtmlQuery query, QueryStrategy queryStrategy, List<org.jsoup.nodes.Node> nodes) {
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

	public static void getAll(HtmlPage page, List<HtmlElement> result, HtmlQuery query, List<org.jsoup.nodes.Node> nodes) {
		getAll(page, result, query, new HtmlQueryStrategy(), nodes);
	}
	
	public static void getAll(HtmlPage page, List<HtmlElement> result, HtmlQuery query, QueryStrategy queryStrategy, List<org.jsoup.nodes.Node> nodes) {
		for(org.jsoup.nodes.Node node : nodes) {
			if(node instanceof Element) {
				if(query.matches(queryStrategy, (Element)node))
					result.add(page.htmlElements().getHtmlElement((Element)node));
				getAll(page, result, query, queryStrategy, node.childNodes());
			}
		}
	}
	
	public static class HtmlQueryStrategy implements QueryStrategy {

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
	}
}
