package com.gistlabs.mechanize.elements;

import com.gistlabs.mechanize.elements.spi.Element;
import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

public class Image<Page extends RequestBuilderFactory<Page>> extends DelegatingElement<Page> {

	public Image(Page page, Element delegate) {
		super(page, delegate);
	}

	public Image(Page page, Element delegate, ElementFactory<Page> factory) {
		super(page, delegate, factory);
	}

	/**
	 * Get the image, can then saveTo()
	 */
	public Page get() {
		if(hasAttribute("src"))
			return getPage().doRequest(srcRef()).get();
		return null;
	}

	
	/** Returns the absolute url for the given image or null if no src-attribute is provided. */
	public String srcRef() {
		if(hasAttribute("src"))
			return getPage().absoluteUrl(getAttribute("src"));
		return null;
	}
}
