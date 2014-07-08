/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.form;

import com.gistlabs.mechanize.document.documentElements.AbstractDocumentElement;
import com.gistlabs.mechanize.document.node.Node;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class FormElement extends AbstractDocumentElement {

	private final Form form;
	private String value = null;
	
	public FormElement(Form form, Node node) {
		super(form.getResource(), node);
		this.form = form;
	}
	
	public Form getForm() {
		return form;
	}
	
	public String getType() {
		return node.getAttribute("type");
	}
	
	public String getName() {
		return node.hasAttribute("name") ? node.getAttribute("name") : null;
	}

	/** Returns the id of the element or null. */
	public String getId() {
		return getNode().hasAttribute("id") ? getNode().getAttribute("id") : null;
	}

	public void set(String value) {
		this.setValue(value);
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean hasValue() {
		return this.value != null && !"".equals(this.value);
	}
	
	public String get() {
		return getValue();
	}
	
	public String getValue() {
		return value != null ? value : getDefaultValue();
	}

	protected String getDefaultValue() {
		return node.hasAttribute("value") ? node.getAttribute("value") : null;
	}

	public boolean matches(String csss) {
		return node.find(csss)!=null;
	}
}
