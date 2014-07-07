/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.form;

import com.gistlabs.mechanize.document.node.Node;

/**
 * Represents an input element of type 'text' or input without a type. 
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Text extends FormElement {

	public Text(Form form, Node node) {
		super(form, node);
	}
	
	/** Returns the max length attribute as integer or -1 if its not present or not parseable. */
	public int getMaxLength() {
		if(getNode().hasAttribute("maxlength")) {
			try {
				return Integer.parseInt(getNode().getAttribute("maxlength"));
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
