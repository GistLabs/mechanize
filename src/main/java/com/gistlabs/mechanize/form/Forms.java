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

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** 
 * A collection of Form objects. 
 *
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Forms extends PageElements<Form> {
	public Forms(Page page, Elements forms) {
		super(page, forms);
	}
	
	@Override
	protected Form newRepresentation(Element element) {
		return new Form(getPage(), element);
	}
	
	/** Writes all forms and form elements to System.out including all the outer HTML. */
	public void dumpAllToSystemOut() 	{
		dumpToSystemOut(getAll());
	}

	public void dumpToSystemOut(List<Form> forms) {
		for(Form form : forms) {
			System.out.println("form: " + form);
			System.out.println(form.getElement().outerHtml());
			System.out.println();
			for(FormElement element : form) {
				System.out.println("formElement: " + element);
				System.out.println("    * " + element.getElement().outerHtml());
			}
			System.out.println();
			System.out.println();
		}
	}
}
