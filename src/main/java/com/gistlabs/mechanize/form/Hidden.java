/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.form;

import com.gistlabs.mechanize.Node;

/** 
 * Represents an input element of type 'hidden'. 
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Hidden extends FormElement {

	public Hidden(Form form, Node node) {
		super(form, node);
	}
	
	@Override
	public void setValue(String value) {
		throw new UnsupportedOperationException("Value of hidden field may not be changed / set");
	}
}
