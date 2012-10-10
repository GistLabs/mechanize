package com.gistlabs.mechanize.json.element;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.gistlabs.mechanize.json.Element;

public class AttributeElement extends AbstractElement implements Element {
	
	public AttributeElement(Element parent, String name) {
		super(parent, name);
	}

	@Override
	public String getAttribute(String key) {
		return null;
	}

	@Override
	public void setAttribute(String key, String value) {
	}

	@Override
	public boolean hasAttribute(String key) {
		return false;
	}

	@Override
	public Collection<String> getAttributes() {
		return null;
	}

	@Override
	public String getContent() {
		return this.parent.getAttribute(getName());
	}

	@Override
	public void setContent(String value) {
		this.parent.setAttribute(getName(), value);
	}

	@Override
	public Element getChild(String key) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Element> getChildren() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Element> getChildren(String key) {
		return getChildren();
	}
}
