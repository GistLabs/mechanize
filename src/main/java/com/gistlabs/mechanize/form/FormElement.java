/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.form;

import com.gistlabs.mechanize.PageElement;

import org.jsoup.nodes.Element;

/**
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class FormElement extends PageElement {

	private final Form form;
	private String value = null;
	
	public FormElement(Form form, Element element) {
		super(form.getPage(), element);
		this.form = form;
	}
	
	public Form getForm() {
		return form;
	}
	
	public String getType() {
		return element.attr("type");
	}
	
	public String getName() {
		return element.hasAttr("name") ? element.attr("name") : null;
	}

	/** Returns the id of the element or null. */
	public String getId() {
		return getElement().hasAttr("id") ? getElement().attr("id") : null;
	}

	public void set(String value) {
		this.setValue(value);
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String get() {
		return getValue();
	}
	
	public String getValue() {
		return value != null ? value : getDefaultValue();
	}

	protected String getDefaultValue() {
		return element.hasAttr("value") ? element.attr("value") : null;
	}
}
