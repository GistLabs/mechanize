/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.form;

import com.gistlabs.mechanize.Page;

import org.jsoup.nodes.Element;

/** Represents an input element of type 'submit'.
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class SubmitButton extends FormElement {

	public SubmitButton(Form form, Element element) {
		super(form, element);
	}
	
	@Override
	public void setValue(String value) {
		throw new UnsupportedOperationException("Value of a submit button may not be changed / set");
	}

	public Page submit() {
		return getForm().submit(this);
	}
}
