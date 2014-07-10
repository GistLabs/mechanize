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
