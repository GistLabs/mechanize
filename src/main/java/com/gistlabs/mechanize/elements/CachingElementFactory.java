package com.gistlabs.mechanize.elements;

import java.util.HashMap;
import java.util.Map;

import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

/**
 * Intended to be used one instance per Page
 * 
 * @author jheintz
 *
 * @param <Page>
 */
public class CachingElementFactory<Page extends RequestBuilderFactory<Page>> implements ElementFactory<Page> {
	private final Map<com.gistlabs.mechanize.elements.spi.Element, Element<Page>> cache = new HashMap<com.gistlabs.mechanize.elements.spi.Element, Element<Page>>();

	@Override
	public final Element<Page> build(Page page, com.gistlabs.mechanize.elements.spi.Element element) {
		if (cache.containsKey(element))
			return cache.get(element);
		else {
			Element<Page> buildNew = buildNew(page, element);
			cache.put(element, buildNew);
			return buildNew;
		}
	}

	protected Element<Page> buildNew(Page page, com.gistlabs.mechanize.elements.spi.Element element) {
		return new DelegatingElement<Page>(page, element, this);
	}

}
