package com.gistlabs.mechanize.elements;

import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

public class Link<Page extends RequestBuilderFactory<Page>> extends AbstractElement<Page> {

	public Link(Page page, Element<Page> delegate) {
		super(page, delegate);
	}

	public Page click() {
		if(hasAttribute("href"))
			return getPage().doRequest(href()).get();
		return null;
	}
	
	public String href() {
		return getPage().absoluteUrl(getAttribute("href"));
	}
}
