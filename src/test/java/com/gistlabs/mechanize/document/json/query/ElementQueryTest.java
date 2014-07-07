/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.query;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.gistlabs.mechanize.document.json.node.JsonNode;
import com.gistlabs.mechanize.document.json.node.impl.ObjectNodeImpl;


public class ElementQueryTest {

	protected JsonNode build(final String json) throws JSONException {
		return new ObjectNodeImpl(new JSONObject(json));
	}

	@Test
	public void testAttributeTilda() throws Exception {
		JsonNode node = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y foo bar\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");

		assertNotNull(node.find("b[x~=\"y\"]"));
		assertEquals(1, node.findAll("b[x~=\"foo\"]").size());
		assertEquals(1, node.findAll("b[x~=\"bar\"]").size());

		assertEquals(0, node.findAll("b[x~=\"baz\"]").size());
	}
}


