/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.node.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gistlabs.mechanize.document.json.exceptions.JsonException;
import com.gistlabs.mechanize.document.json.node.JsonNode;

public class IndexedAttributeNode extends AttributeNode {

	private final int index;
	private final JSONArray array;

	public IndexedAttributeNode(final JsonNode parent, final String name, final JSONArray array, final int index) {
		super(parent, name);
		this.array = array;
		this.index = index;
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
	public String getValue() {
		try {
			return this.array.getString(this.index);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public void setValue(final String value) {
		try {
			this.array.put(this.index, value);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}
}
