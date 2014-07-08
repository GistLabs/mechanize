/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.node.impl;

import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.gistlabs.mechanize.document.json.exceptions.JsonException;
import com.gistlabs.mechanize.document.json.node.JsonNode;

public class AttributeNode extends AbstractJsonNode {

	public AttributeNode(final JsonNode parent, final String name) {
		super(parent, name);
	}

	@Override
	public String toString() {
		try {
			return new JSONObject().put(name, getValue()).toString();
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public String getAttribute(final String key) {
		return null;
	}

	@Override
	public void setAttribute(final String key, final String value) {
	}

	@Override
	public boolean hasAttribute(final String key) {
		return false;
	}

	@Override
	public List<String> getAttributeNames() {
		return null;
	}

	@Override
	public String getValue() {
		return this.parent.getAttribute(getName());
	}

	@Override
	public void setValue(final String value) {
		this.parent.setAttribute(getName(), value);
	}

	@Override
	public <T extends JsonNode> T getChild(final String key) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JsonNode> getChildren(final String... names) {
		return Collections.EMPTY_LIST;
	}
}
