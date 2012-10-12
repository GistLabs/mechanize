/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.html;

import java.util.List;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import com.gistlabs.mechanize.Node;
import com.gistlabs.mechanize.query.AbstractQuery;
import com.gistlabs.mechanize.util.Util;

/**
 * Represents an element of a html document.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class HtmlElement extends HtmlNode implements Node {
	
	public HtmlElement(HtmlPage page, Element jsoupElement) {
		super(page, jsoupElement);
	}
	
	public Element getJsoupElement() {
		return (Element)getJsoupNode();
	}
	
	@Override
	public String getName() {
		return getAttribute(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_NODE_NAME);
	}
	
	@Override
	public String getValue() {
		return getAttribute(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_NODE_VALUE);
	}
	
	@Override
	public boolean isMultipleValueAttribute(String attributeKey) {
		return attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_CLASS_NAMES);
	}

	@Override
	public HtmlElement get(AbstractQuery<?> query) {
		return super.get(query);
	}
	
	@Override
	public List<HtmlElement> getAll(AbstractQuery<?> query) {
		return super.getAll(query);
	}
	
	@Override
	public boolean hasAttribute(String attributeKey) {
		return !isSupportedSpecialAttribute(attributeKey) ? getJsoupElement().hasAttr(attributeKey) : true;
	}

	private boolean isSupportedSpecialAttribute(String attributeKey) {
		return attributeKey.startsWith("${") && (attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_CLASS_NAMES) 
				|| attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_HTML)
				|| attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_INNER_HTML)
				|| attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TAG_NAME)
				|| attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TEXT));
	}

	@Override
	public String getAttribute(String attributeKey) {
		Element element = getJsoupElement();
		return getAttributeValueOfJSoupElement(element, attributeKey);
	}

	public static String getAttributeValueOfJSoupElement(Element element, String attributeKey) {
		if(attributeKey.startsWith("${") && attributeKey.endsWith("}")) {
			if(attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TEXT))
				return element.text();
			else if(attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_INNER_HTML))
				return element.html();
			else if(attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_HTML))
				return element.outerHtml();
			else if(attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TAG_NAME))
				return element.tagName();
			else if(attributeKey.equals(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_CLASS_NAMES)) {
				StringBuilder result = new StringBuilder();
				for(String name : element.classNames()) {
					if(result.length() > 0)
						result.append(",");
					result.append(name);
				}
				return result.toString();
			}
			else 
				return null;
		}
		else
			return element.attr(attributeKey);
	}
	
	/** Returns the class names of the this element as a comma separated list without trailing white-spaces. */
	public String getClassNames() {
		return getAttribute(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_CLASS_NAMES);
	}
	
	/** Returns the inner text of this element without style and tag information. */
	public String getText() {
		return getAttribute(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TEXT);
	}
	
	/** Returns the inner HTML string of this element including all child HTML but not the 
	 *  element's own HTML representation. */
	public String getInnerHtml() {
		return getAttribute(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_INNER_HTML);
	}
	
	/** Returns the HTML string including this element. */ 
	public String getHtml() {
		return getAttribute(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_HTML);
	}
	
	/** Returns the name of the tag of this element. */
	public String getTagName() {
		return getAttribute(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TAG_NAME);
	}
	
	@Override
	public List<String> getAttributeNames() {
		return getAttributeNamesForJSoupElement(getJsoupElement());
	}
	
	public static List<String> getAttributeNamesForJSoupElement(Element element) {
		List<String> result = Util.newEmptyList();
		
		for(Attribute attribute : element.attributes())
			result.add(attribute.getKey());
		
		result.add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_CLASS_NAMES);
		result.add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_HTML);
		result.add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_INNER_HTML);
		result.add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TAG_NAME);
		result.add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TEXT);
		
		return result;
	}
}
