/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.node;

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
	public void debug() {
//		JsonNode node = json.find("template-list");
//		Object o = node.getAttribute("list");
//		Object c = node.getChildren("list");
//		List<? extends JsonNode> children = node.getChildren();
//		System.out.println();
	}	
}
