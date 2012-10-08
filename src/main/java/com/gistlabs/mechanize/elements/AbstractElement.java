package com.gistlabs.mechanize.elements;

import java.util.Collection;
import java.util.List;

import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

/**
 * Empty implementation... probably implement as wrapper/delgator for Element<..>
 * @author jheintz
 *
 * @param <Page>
 */
public abstract class AbstractElement<Page extends RequestBuilderFactory<Page>> implements Element<Page> {
	private final Page page;
	private final Element<Page> delegate;
	
	public AbstractElement(Page page, Element<Page> delegate) {
		this.page = page;
		this.delegate = delegate;
	}

	@Override
	public Page getPage() {
		return this.page;
	}

	@Override
	public String getAttribute(String key) {
		return this.delegate.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, String value) {
		this.delegate.setAttribute(key, value);
	}

	@Override
	public boolean hasAttribute(String key) {
		return this.delegate.hasAttribute(key);
	}

	@Override
	public Collection<String> getAttributes() {
		return this.delegate.getAttributes();
	}

	@Override
	public String getValue() {
		return this.delegate.getValue();
	}

	@Override
	public void setValue(String value) {
		this.delegate.setValue(value);
	}

	@Override
	public List<Element<Page>> getChildren() {
		return this.delegate.getChildren();
	}

	@Override
	public Element<Page> get(String query) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Element<Page>> getAll(String query) {
		// TODO Auto-generated method stub
		return null;
	}
}
