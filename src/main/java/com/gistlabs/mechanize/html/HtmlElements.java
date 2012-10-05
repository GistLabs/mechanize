package com.gistlabs.mechanize.html;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import com.gistlabs.mechanize.HtmlPage;
import com.gistlabs.mechanize.query.Query;

/**
 * Collection of all HTML elements of a HtmlPage object and the underlying Jsoup DOM-Document. 
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
//TODO check for extension of PageElements
public class HtmlElements {

	private Document document;
	private HtmlPage page; 

	public HtmlElements(HtmlPage page, Document document) {
		this.page = page;
		this.document = document;
	}
	
	/** Returns the first element matching the query in a deep first left right search. */ 
	public HtmlElement get(Query query) {
		return get(page, query, document.childNodes());
	}
	
	/** Returns the elements matching the query in a deep first left right search. */ 
	public List<HtmlElement> getAll(Query query) {
		List<HtmlElement> result = new ArrayList<HtmlElement>();
		getAll(page, result, query, document.childNodes());
		return result;
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
