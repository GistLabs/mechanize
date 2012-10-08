/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.form;

import java.util.List;

import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.PageElements;
import com.gistlabs.mechanize.html.HtmlElement;

/** 
 * A collection of Form objects. 
 *
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Forms extends PageElements<Form> {
	public Forms(Page page) {
		this(page, null);
	}
	public Forms(Page page, List<HtmlElement> forms) {
		super(page, forms);
	}
	
	@Override
	protected Form newRepresentation(HtmlElement element) {
		return new Form(getPage(), element);
	}
}
