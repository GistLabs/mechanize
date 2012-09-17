/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.form;

import org.jsoup.nodes.Element;

/**
 * Represents an input element of type 'text' or input without a type. 
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Text extends FormElement {

	public Text(Form form, Element element) {
		super(form, element);
	}
	
	/** Returns the max length attribute as integer or -1 if its not present or not parseable. */
	public int getMaxLength() {
		if(getElement().hasAttr("maxlength")) {
			try {
				return Integer.parseInt(getElement().attr("maxlength"));
			}
			catch(NumberFormatException e) {
				return -1;
			}
		}
		else
			return -1;
	}
	
	@Override
	public void setValue(String value) {
		if(value != null && getMaxLength() != -1 && value.length() > getMaxLength()) 
			super.setValue(value.substring(0, getMaxLength()));
		else
			super.setValue(value);
	}
}
