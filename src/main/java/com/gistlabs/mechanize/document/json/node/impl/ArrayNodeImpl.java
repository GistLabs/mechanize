/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.node.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.gistlabs.mechanize.document.json.exceptions.JsonException;
import com.gistlabs.mechanize.document.json.node.JsonNode;

/**
 * Support for nested arrays
 */
public class ArrayNodeImpl extends AbstractJsonNode {
	private final JSONArray array;
	private List<JsonNode> children;

	public ArrayNodeImpl(final JSONArray array) {
		this(null, "", array);
	}

	public ArrayNodeImpl(final JsonNode parent, final String key, final JSONArray array) {
		super(parent, key);
		if (array==null)
			throw new NullPointerException("JSONArray can't be null");
		this.array = array;
	}

	@Override
	public String toString() {
		return array.toString();
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
		return new ArrayList<String>();
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public void setValue(final String value) {
	}

	@Override
	public <T extends JsonNode> T getChild(final String key) {
		return null;
	}

	@Override
	public List<JsonNode> getChildren() {
		return getChildren("*");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<JsonNode> getChildren(final String... names) {
		if (names.length>2)
			return Collections.EMPTY_LIST;
		if (names.length==1 && !"*".equalsIgnoreCase(names[0]))
			return Collections.EMPTY_LIST;

		try {
			if (children==null) {
				children = new ArrayList<JsonNode>();
				for(int i=0;i < array.length();i++){
					Object obj = array.get(i);
					children.add(factory("array", obj, array, i));
				}
			}
			return children;
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}
}
