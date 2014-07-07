/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.form;

import com.gistlabs.mechanize.document.node.Node;

/** Represents an input element of type 'radio'.  
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class RadioButton extends Checkable {

	public RadioButton(Form form, Node node) {
		super(form, node);
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
