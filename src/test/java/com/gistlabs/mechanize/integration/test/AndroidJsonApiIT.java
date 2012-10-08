/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.junit.Test;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.json.JsonPage;

/**
 * 
 */
public class AndroidJsonApiIT {

	@Test
	public void testAndroidJsonApiDemo() throws JSONException {
		MechanizeAgent agent = new MechanizeAgent();
		JsonPage page = (JsonPage) agent.get("http://api.androidhive.info/contacts/");
		
		assertNotNull(page.json().getJSONArray("contacts"));
	}
}
