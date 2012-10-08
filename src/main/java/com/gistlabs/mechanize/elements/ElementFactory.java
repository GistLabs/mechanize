package com.gistlabs.mechanize.elements;

import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

public interface ElementFactory<Page extends RequestBuilderFactory<Page>> {

	public Element<Page> build(Page page, com.gistlabs.mechanize.elements.spi.Element element);
}
