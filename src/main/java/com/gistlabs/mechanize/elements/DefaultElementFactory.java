package com.gistlabs.mechanize.elements;

import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

public class DefaultElementFactory<Page extends RequestBuilderFactory<Page>> implements ElementFactory<Page> {

	@Override
	public Element<Page> build(Page page, com.gistlabs.mechanize.elements.spi.Element element) {
		return new DelegatingElement<Page>(page, element, this);
	}

}
