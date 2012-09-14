/*
 * Copyright (C) 2011 Gist Labs, LLC.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.gistlabs.mechanize.form;

import org.jsoup.nodes.Element;

/** Represents an text area element.
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class TextArea extends FormElement {

	public TextArea(Form form, Element element) {
		super(form, element);
	}
	
	@Override
	protected String getDefaultValue() {
		return element.html();
	}
}
