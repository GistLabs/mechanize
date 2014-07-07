/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.json.JsonDocument;
import com.gistlabs.mechanize.document.json.node.JsonNode;
import com.gistlabs.mechanize.util.apache.ContentType;

/**
 */
public class JsonPageTest extends MechanizeTestCase {

	@Test
	public void testLoadJson() {
		agent.addPageRequest("GET", "http://test.com", getClass().getResourceAsStream("dropbox.account.info.json")).setContentType(ContentType.APPLICATION_JSON.getMimeType());
		Resource page = agent.get("http://test.com");
		assertNotNull(page);
		assertEquals(JsonDocument.class, page.getClass());
	}

	@Test
	public void testParseJson() {
		agent.addPageRequest("GET", "http://test.com", getClass().getResourceAsStream("dropbox.account.info.json")).setContentType(ContentType.APPLICATION_JSON.getMimeType());
		JsonDocument page = (JsonDocument) agent.get("http://test.com");
		assertNotNull(page.getRoot());

		assertEquals("US", page.getRoot().getAttribute("country"));

		JsonNode node = page.getRoot().find("quota_info");
		assertEquals("107374182400000", node.getAttribute("quota"));
	}
}