package com.gistlabs.mechanize.html;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.gistlabs.mechanize.query.Query;

/**
 * Represents an element of a html document.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class HtmlElement extends HtmlNode {
	public HtmlElement(HtmlPage page, Element element) {
		super(page, element);
	}
	
	public Element getElement() {
		return (Element)getNode();
	}
	
	/** Returns the first child element matching the query by performing a deep first left right search. */
	public HtmlElement get(Query query) {
		return HtmlElements.get(getPage(), query, getElement().childNodes());
	}

	/** Returns all child elements matching the query by performing a deep first left right search. */
	public List<HtmlElement> getAll(Query query) {
		List<HtmlElement> result = new ArrayList<HtmlElement>();
		HtmlElements.getAll(getPage(), result, query, getElement().childNodes());
		return result;
	}
	
	/** Returns the child elements. */
	public List<HtmlNode> getChildren() {
		List<HtmlNode> result = new ArrayList<HtmlNode>();
		for(Node node : getElement().childNodes()) {
			if(node instanceof Element)
				result.add(new HtmlElement(getPage(), (Element)node));
			else if(node instanceof TextNode) 
				result.add(new HtmlTextNode(getPage(), (TextNode)node));
		}
		
		return result;
	}
	
	public boolean hasAttribute(String attributeKey) {
		return getElement().hasAttr(attributeKey);
	}

	public String getAttribute(String attributeKey) {
		return getElement().attr(attributeKey);
	}
	
	public String getAbsoluteUrl(String attributeKey) {
		return getElement().absUrl(attributeKey);
	}
	
	/** Returns the inner text of this element without style and tag information. */
	public String getText() {
		return getElement().text();
	}
	
	/** Returns the inner HTML string of this element including all child HTML but not the 
	 *  element's own HTML representation. */
	public String getInnerHtml() {
		return getElement().html(); 
	}
	
	/** Returns the HTML string including this element. */ 
	public String getHtml() {
		return getElement().outerHtml();
	}
	
	/** Returns the name of the tag of this element. */
	public String getTagName() {
		return getElement().tagName();
	}
}
