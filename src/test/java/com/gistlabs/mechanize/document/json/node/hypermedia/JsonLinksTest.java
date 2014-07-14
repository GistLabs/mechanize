/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.node.hypermedia;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.gistlabs.mechanize.document.json.hypermedia.JsonLink;
import com.gistlabs.mechanize.document.json.node.JsonNode;
import com.gistlabs.mechanize.document.json.node.JsonNodeTestCase;
import com.gistlabs.mechanize.util.Util;

public class JsonLinksTest extends JsonNodeTestCase {
	JsonNode json;
	
	@Before
	public void parseJson() {
		String jsonString = Util.getStringFromInputStream(getClass().getResourceAsStream("links.json"));
		json = from(jsonString);
	}
	
	@Test
	public void testLinkNames() {
		JsonNode simple = json.find("simple");
		assertEquals("self", new JsonLink(simple).linkRel());
		
		JsonNode relative = json.find("relative");
		assertEquals("relative", new JsonLink(relative).linkRel());
	}
	
	@Test
	public void setVariableProgramatically() {
		JsonNode node = json.find("trivial-template");
		JsonLink link = new JsonLink(node);
		link.set("a", "aaa");
		assertEquals("http://example.com/aaa", link.uri());
	}
	
	@Test
	public void setAllVariableProgramatically() {
		JsonNode node = json.find("template-path-segments");
		JsonLink link = new JsonLink(node);
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("a", "aaa");
		variables.put("b", "bbb");
		link.setAll(variables);
		assertEquals("http://example.com/aaa/bbb/baz", link.uri());
	}
	
	@Test
	public void debug() {
//		JsonNode keys = json.<JsonNode>find("template-keys").getChild("keys");
//		List<String> attributeNames = keys.getAttributeNames();
//		
//		List<? extends JsonNode> children = json.<JsonNode>find("template-list").getChildren("list");
//		List<String> attributeNames2 = children.get(0).getAttributeNames();
//		
//		JsonLink link = BulkJsonLinksTest.link(json.<JsonNode>find("template-keys"));
//		BulkJsonLinksTest.assertExpectedUri(link);
	}	
}
