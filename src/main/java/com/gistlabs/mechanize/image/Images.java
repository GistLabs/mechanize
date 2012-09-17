/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.image;

import java.util.List;

import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.PageElements;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** 
 * A collection of Image objects.  
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Images extends PageElements<Image> {
	public Images (Page page, Elements forms) {
		super(page, forms);
	}

	@Override
	protected Image newRepresentation(Element element) {
		return new Image(page, element);
	}
	
	/** Writes all forms and form elements to System.out including all the outer HTML. */
	public void dumpAllToSystemOut() {
		dumpToSystemOut(getAll());
	}
		
	public void dumpToSystemOut(List<Image> images) {
		for(Image image : this) {
			System.out.println("image: " + image);
			System.out.println("   * " + image.getElement().outerHtml());
		}
	}
}