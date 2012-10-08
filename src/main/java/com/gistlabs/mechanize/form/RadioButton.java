/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.form;

import com.gistlabs.mechanize.html.HtmlElement;

/** Represents an input element of type 'radio'.  
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class RadioButton extends Checkable {

	public RadioButton(Form form, HtmlElement element) {
		super(form, element);
	}
	
	@Override
	public void setChecked(boolean isChecked) {
		if(isChecked == true)
			uncheckAll();
		super.setChecked(isChecked);
	}

	private void uncheckAll() {
		for(RadioButton button : getForm().getRadioButtons(getName()))
			button.setChecked(false);
	}
}
