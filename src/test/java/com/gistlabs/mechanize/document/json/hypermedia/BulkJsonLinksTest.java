/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.hypermedia;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gistlabs.mechanize.document.json.hypermedia.JsonLink;
import com.gistlabs.mechanize.document.json.node.JsonNode;
import com.gistlabs.mechanize.document.json.node.JsonNodeTestCase;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.util.Util;

@RunWith(value = Parameterized.class)
public class BulkJsonLinksTest extends JsonNodeTestCase {
	JsonNode node;
	String name;
	
	public static JsonNode parseJson() {
		String jsonString = Util.getStringFromInputStream(BulkJsonLinksTest.class.getResourceAsStream("links.json"));
		return from(jsonString);
	}

	@Parameters(name="{index} {1}")
	public static Collection<Object[]> data() {
		List<Object[]> results = new ArrayList<Object[]>();
		
		List<? extends JsonNode> children = parseJson().getChildren();
		for (JsonNode jsonNode : children) {
			results.add(new Object[] {jsonNode, jsonNode.getName()});
		}
		
		return results;
	}
	
	public BulkJsonLinksTest(JsonNode node, String name) {
		this.node = node;
	}
		
	@Test
	public void testLinkResolution() {
		JsonLink link = link(find(node));
		assertExpectedUri(link);
	}

	static JsonNode find(JsonNode node) {
		Node nested = node.find("nested-query");
		if (nested==null) {
			return node;
		} else {
			return node.find(nested.getValue());
		}
	}
	
	static JsonLink link(JsonNode node) {
		String baseUrl = node.hasAttribute("baseUrl") ? node.getAttribute("baseUrl") : null; // optionally use data supplied for baseUrl
		String attrName = node.hasAttribute("use-attr") ? node.getAttribute("use-attr") : "href"; // optionally use data supplied to find link attribute
		JsonLink link = new JsonLink(baseUrl, node, attrName, null);
		return link;
	}
	
	static void assertExpectedUri(JsonLink link) {
		String name = link.node().getName();
		String expected = link.node().getAttribute("expected");
		String uri = link.uri();
		assertEquals(name, expected, uri);
	}
}
