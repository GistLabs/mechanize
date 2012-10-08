package com.gistlabs.mechanize.elements.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.nodes.Attribute;

import com.gistlabs.mechanize.elements.spi.Element;

public class HtmlElement implements Element {
	private org.jsoup.nodes.Element html;
	
	public HtmlElement(org.jsoup.nodes.Element html) {
		this.html = html;
	}

	@Override
	public String getElementName() {
		return this.html.tagName();
	}
	
	@Override
	public String getAttribute(String key) {
		return this.html.attr(key);
	}

	@Override
	public void setAttribute(String key, String value) {
		this.html.attr(key, value);
	}

	@Override
	public boolean hasAttribute(String key) {
		return this.html.hasAttr(key);
	}

	@Override
	public Collection<String> getAttributes() {
		List<String> result = new ArrayList<String>();
		for (Attribute attribute : this.html.attributes().asList()) {
			result.add(attribute.getKey());
		}
		return result;
	}

	@Override
	public String getContent() {
		return this.html.text();
	}

	/**
	 * This becomes not good and weird if an element has both textual content and a value attribute...
	 */
	@Override
	public void setContent(String content) {
		this.html.text(content);
	}

	@Override
	public List<HtmlElement> getChildren() {
		List<HtmlElement> result = new ArrayList<HtmlElement>();
		for (org.jsoup.nodes.Element htmlChild : this.html.children()) {
			result.add(new HtmlElement(htmlChild));
		}
		return result;
	}
}
