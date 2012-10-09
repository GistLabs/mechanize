package com.gistlabs.mechanize.json.element;

import com.gistlabs.mechanize.json.Element;

public abstract class AbstractElement implements Element {
	protected final String name;
	protected final Element parent;

	public AbstractElement(String name) {
		this(null, name);
	}
		
	public AbstractElement(Element parent, String name) {
		this.parent = parent;
		this.name = name;
	}
		
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public Element getParent() {
		return this.parent;
	}
}