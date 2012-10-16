/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.form;

import com.gistlabs.mechanize.document.Node;

/** 
 * Represents an input element of type 'checkbox'. 
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Checkbox extends Checkable {

	public Checkbox(Form form, Node node) {
		super(form, node);
	}
}
