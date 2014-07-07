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
 * Base class for Checkbox and RadioButton.
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @since 2012-09-12
 */
public abstract class Checkable extends FormElement {

	protected boolean isChecked = false;

	public Checkable(Form form, Node node) {
		super(form, node);
		if(node.hasAttribute("checked"))
			isChecked = true;
	}

	public void check() {
		setChecked(true);
	}

	public void uncheck() {
		setChecked(false);
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void setValue(String value) {
		throw new UnsupportedOperationException("Value of a checkbox may not be changed / set");
	}

}