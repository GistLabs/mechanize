/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.node;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.gistlabs.mechanize.util.Util;

public class JsonLinksTest extends JsonNodeTestCase {
	JsonNode json;
	
	@Before
	public void parseJson() {
		String jsonString = Util.getStringFromInputStream(getClass().getResourceAsStream("links.json"));
		json = from(jsonString);
	}
	
	@Test
	public void testSimple() {
		JsonNode node = json.find("simple");
		assertEquals("simple", node.getName());
		
		JsonLink link = new JsonLink(node);
		assertExpectedUri(link);
	}
	
	@Test
	public void testRelative() {
		JsonNode node = json.find("relative");
		
		JsonLink link = new JsonLink(node.getAttribute("baseUrl"), node);
		assertExpectedUri(link);		
	}
	
	@Test
	public void testTemplate() {
		JsonNode node = json.find("template");
		
		JsonLink link = new JsonLink(node);
		assertExpectedUri(link);
	}
	
	private void assertExpectedUri(JsonLink link) {
		assertEquals(link.node().getAttribute("expected"), link.uri());
	}
}
