package com.gistlabs.mechanize.elements.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.nodes.Attribute;

import com.gistlabs.mechanize.elements.Element;
import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

public class HtmlElement<Page extends RequestBuilderFactory<Page>> implements Element<Page> {
	private final Page page;
	private org.jsoup.nodes.Element html;
	
	public HtmlElement(Page page, org.jsoup.nodes.Element html) {
		this.page = page;
		this.html = html;
	}

	@Override
	public Page getPage() {
		return page;
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
	public String getValue() {
		return this.hasAttribute("value") ? this.getAttribute("value") : this.html.text();
	}

	/**
	 * This becomes not good and weird if an element has both textual content and a value attribute...
	 */
	@Override
	public void setValue(String value) {
		if (hasAttribute("value"))
			setAttribute("value", value);
		else
			this.html.text(value);
	}

	@Override
	public List<Element<Page>> getChildren() {
		List<Element<Page>> result = new ArrayList<Element<Page>>();
		for (org.jsoup.nodes.Element htmlChild : this.html.children()) {
			result.add(new HtmlElement<Page>(getPage(), htmlChild));
		}
		return result;
	}

	@Override
	public Element<Page> get(String query) {
		// TODO Can a CSS query run directly against the com.gistlabs.mechanize.elements.Element type?
		return null;
	}

	@Override
	public List<Element<Page>> getAll(String query) {
		// TODO Can a CSS query run directly against the com.gistlabs.mechanize.elements.Element type?
		return null;
	}

}
