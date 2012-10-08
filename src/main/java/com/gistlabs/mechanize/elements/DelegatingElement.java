package com.gistlabs.mechanize.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

/**
 * Empty implementation... probably implement as wrapper/delgator for SPI Element<..>
 * @author jheintz
 *
 * @param <Page>
 */
public class DelegatingElement<Page extends RequestBuilderFactory<Page>> implements Element<Page> {
	private final Page page;
	private final com.gistlabs.mechanize.elements.spi.Element delegate;
	private final ElementFactory<Page> factory;
	
	public DelegatingElement(Page page, com.gistlabs.mechanize.elements.spi.Element delegate) {
		this(page, delegate, new DefaultElementFactory<Page>());
	}
	
	public DelegatingElement(Page page, com.gistlabs.mechanize.elements.spi.Element delegate, ElementFactory<Page> factory) {
		this.page = page;
		this.delegate = delegate;
		this.factory = factory;
	}

	@Override
	public Page getPage() {
		return this.page;
	}

	@Override
	public String getElementName() {
		return this.delegate.getElementName();
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
	public String getContent() {
		return this.delegate.getContent();
	}

	@Override
	public void setContent(String content) {
		this.delegate.setContent(content);
	}

	@Override
	public List<Element<Page>> getChildren() {
		List<Element<Page>> result = new ArrayList<Element<Page>>();
		for (com.gistlabs.mechanize.elements.spi.Element element : this.delegate.getChildren()) {
			result.add(factory.build(getPage(), element));
		}
		return result;
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

	@Override
	public Iterator<Element<Page>> iterator() {
		return getChildren().iterator();
	}
}
