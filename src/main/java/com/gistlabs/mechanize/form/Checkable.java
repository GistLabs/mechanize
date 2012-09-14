/*
 * Copyright (C) 2011 Gist Labs, LLC.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.gistlabs.mechanize.form;

import org.jsoup.nodes.Element;

/**
 * Base class for Checkbox and RadioButton.
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public abstract class Checkable extends FormElement {

	protected boolean isChecked = false;

	public Checkable(Form form, Element element) {
		super(form, element);
		if(element.hasAttr("checked"))
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