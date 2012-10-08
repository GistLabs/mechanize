package com.gistlabs.mechanize.elements.html;

import com.gistlabs.mechanize.elements.CachingElementFactory;
import com.gistlabs.mechanize.elements.Element;
import com.gistlabs.mechanize.elements.ElementFactory;
import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

public class HtmlElementFactory<Page extends RequestBuilderFactory<Page>> extends CachingElementFactory<Page> implements ElementFactory<Page> {

	@Override
	public Element<Page> buildNew(Page page, com.gistlabs.mechanize.elements.spi.Element element) {
		// this can return a Link, or Image, or Form element, or ....
		return null;
	}

}
