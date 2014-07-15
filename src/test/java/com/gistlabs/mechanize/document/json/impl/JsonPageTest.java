/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.document.json.JsonDocument;
import com.gistlabs.mechanize.document.json.node.JsonNode;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.util.apache.ContentType;

/**
 */
public class JsonPageTest extends MechanizeTestCase {

	@Override
	protected String contentType() {
		return ContentType.APPLICATION_JSON.getMimeType();
	}
	
	@Test
	public void testLoadJson() {
		addPageRequest("GET", "http://test.com", getClass().getResourceAsStream("dropbox.account.info.json"));
		JsonDocument page = agent().get("http://test.com");
		assertNotNull(page);
		assertEquals(JsonDocument.class, page.getClass());
	}

	@Test
	public void testParseJson() {
		addPageRequest("GET", "http://test.com", getClass().getResourceAsStream("dropbox.account.info.json"));
		JsonDocument page = agent().get("http://test.com");
		assertNotNull(page.getRoot());

		assertEquals("US", page.getRoot().getAttribute("country"));

		JsonNode node = page.getRoot().find("quota_info");
		assertEquals("107374182400000", node.getAttribute("quota"));
	}

	
	@Test
	public void testTwitterJson() {
		addPageRequest("GET", "http://test.com", getClass().getResourceAsStream("twitter.json"));
		JsonDocument page = agent().get("http://test.com");
		assertNotNull(page);
		assertEquals(JsonDocument.class, page.getClass());
		JsonNode root = page.getRoot();
		List<? extends JsonNode> children = root.getChildren();
		List<? extends Node> users = page.findAll("user");
		assertEquals(17, users.size());
	}
}