/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import static org.junit.Assert.*;

import java.util.List;

import org.json.JSONException;
import org.junit.Test;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.json.JsonPage;
import com.gistlabs.mechanize.json.node.JsonNode;

/**
 * 
 */
public class AndroidJsonApiIT {

	@Test
	public void testAndroidJsonApiDemo() throws JSONException {
		MechanizeAgent agent = new MechanizeAgent();
		JsonPage page = (JsonPage) agent.get("http://api.androidhive.info/contacts/");

		List<? extends JsonNode> contacts = page.getRoot().getChildren("contacts");
		assertNotNull(contacts);
		assertEquals("c200", contacts.get(0).getAttribute("id"));
	}
}
