/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.json;

import static org.junit.Assert.*;

import java.io.InputStreamReader;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

public class JsonTest {

	/**
	 * Example from https://www.dropbox.com/developers/reference/api#account-info in dropbox.account.info.json
	 */
	@Test
	public void testJsonParsing() throws Exception {
		JSONObject json = new JSONObject(new JSONTokener(new InputStreamReader(getClass().getResourceAsStream("impl/dropbox.account.info.json"))));
		assertNotNull(json);
		assertEquals(12345678, json.getLong("uid"));
	}

	@Test
	public void testEmptyKey() throws Exception {
		JSONObject json = new JSONObject("{ \"one\" : \"two\", \"\" : \"four\" }");
		assertEquals("four", json.getString(""));
	}
}
