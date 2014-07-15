/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.query;

import static org.junit.Assert.assertEquals;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import com.gistlabs.mechanize.document.json.node.JsonNode;
import com.gistlabs.mechanize.document.json.node.impl.ArrayNodeImpl;
import com.gistlabs.mechanize.util.css_query.NodeSelector;

/**
 *
 */
public class ArrayRootSelectorTest {

	protected NodeSelector<JsonNode> build(final String json) throws JSONException {
		ArrayNodeImpl node = new ArrayNodeImpl(new JSONArray(json));
		return node.buildNodeSelector();
	}

	@Test
	public void testStar() throws Exception {
		NodeSelector<JsonNode> selector = build("[ { \"a\": \"1\" }, { \"b\": \"2\" }, { \"b\": \"3\" }]");

		assertEquals(1, selector.findAll("a").size());
		assertEquals(2, selector.findAll("b").size());
	}
}
