/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.form;

import com.gistlabs.mechanize.document.Node;
import com.gistlabs.mechanize.document.PageElement;
import com.gistlabs.mechanize.document.query.AbstractQuery;
import com.gistlabs.mechanize.document.query.QueryStrategy;

/**
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class FormElement extends PageElement {

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

	public boolean matches(QueryStrategy queryStrategy, AbstractQuery<?> query) {
		return query.matches(queryStrategy, getNode());
	}
}
