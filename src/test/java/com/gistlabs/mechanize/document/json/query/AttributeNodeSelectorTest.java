/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.query;

import static org.junit.Assert.*;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.gistlabs.mechanize.document.json.node.JsonNode;
import com.gistlabs.mechanize.document.json.node.impl.ObjectNodeImpl;
import com.gistlabs.mechanize.util.css_query.NodeSelector;


public class AttributeNodeSelectorTest {

	protected NodeSelector<JsonNode> build(final String json) throws JSONException {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject(json));
		return node.buildNodeSelector();
	}

	@Test
	public void testAttributeAsNode() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2, \"c\" : 3 } ] }");

		List<JsonNode> result = selector.findAll("b x");
		assertEquals(1, result.size());
		assertEquals("y", result.get(0).getValue());
	}

	@Test
	public void testSpecificChildWithAttributeAsNode() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"_b\" : 2, \"c\" : 3 } ] }");

		List<JsonNode> result = selector.findAll("results[_b=\"2\"] c");
		assertEquals(1, result.size());
		assertEquals("3", result.get(0).getValue());
	}
}


