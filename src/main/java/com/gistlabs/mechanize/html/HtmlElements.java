package com.gistlabs.mechanize.html;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import com.gistlabs.mechanize.query.Query;

/**
 * Collection of all HTML elements of a HtmlPage object and the underlying Jsoup DOM-Document. 
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
//TODO check for extension of PageElements
public class HtmlElements {

	private HtmlPage page; 
	
	private HtmlElement root;

	//TODO May increase memory footprint when html code changes
	private final Map<Element, HtmlElement> elementCache = new HashMap<Element, HtmlElement>();

	public HtmlElements(HtmlPage page, Document document) {
		this.page = page;
		this.root = getHtmlElement(document);
	}
	
	/** Returns the root element representing the org.jsoup.Document page element. */ 
	public HtmlElement getRoot() {
		return root;
	}
	
	/** Returns the first element matching the query in a deep first left right search. */ 
	public HtmlElement get(Query query) {
		return root.get(query);
	}
	
	/** Returns the elements matching the query in a deep first left right search. */ 
	public List<HtmlElement> getAll(Query query) {
		return root.getAll(query);
	}
	
	public HtmlElement getHtmlElement(Element element) {
		HtmlElement htmlElement = elementCache.get(element);
		
		if(htmlElement == null) {
			htmlElement = new HtmlElement(page, element);
			elementCache.put(element, htmlElement);
		}
		
		return htmlElement;
	}
	
	@Override
	public String toString() {
		return root.toString();
	}

	static HtmlElement get(HtmlPage page, Query query, List<Node> nodes) {
		for(Node node : nodes) {
			if(node instanceof Element) {
				if(query.matches((Element)node))
					return new HtmlElement(page, (Element)node);
				HtmlElement result = get(page, query, node.childNodes());
				if(result != null)
					return result;
			}
		}
		return null;
	}

	static void getAll(HtmlPage page, List<HtmlElement> result, Query query, List<Node> nodes) {
		for(Node node : nodes) {
			if(node instanceof Element) {
				if(query.matches((Element)node))
					result.add(new HtmlElement(page, (Element)node));
				getAll(page, result, query, node.childNodes());
			}
		}
	}
}
