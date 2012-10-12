package com.gistlabs.mechanize.html.query;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import com.gistlabs.mechanize.Node;
import com.gistlabs.mechanize.html.HtmlElement;
import com.gistlabs.mechanize.html.HtmlTextNode;
import com.gistlabs.mechanize.query.QueryStrategy;
import com.gistlabs.mechanize.util.Util;

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
		if(object instanceof Node)
			return ((Node)object).isMultipleValueAttribute(attributeKey);
		else
			return false;
	}
}