package com.gistlabs.mechanize.elements;

import com.gistlabs.mechanize.elements.spi.Element;
import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

public class Link<Page extends RequestBuilderFactory<Page>> extends DelegatingElement<Page> {

	public Link(Page page, Element delegate) {
		super(page, delegate);
	}

	public Link(Page page, Element delegate, ElementFactory<Page> factory) {
		super(page, delegate, factory);
	}

	public Page click() {
		if(hasAttribute("href"))
			return getPage().doRequest(href()).get();
		return null;
	}
	
	public String href() {
		if (hasAttribute("href"))
			return getPage().absoluteUrl(getAttribute("href"));
		return null;
	}
}
