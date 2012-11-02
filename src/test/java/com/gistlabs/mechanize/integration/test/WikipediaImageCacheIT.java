/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import org.json.JSONException;
import org.junit.Test;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Resource;

/**
 * Test a well known and http cachable resource
 */
public class WikipediaImageCacheIT {

	@SuppressWarnings("unused")
	@Test
	public void testCachedResource() throws JSONException {
		MechanizeAgent agent = new MechanizeAgent();
		Resource res1 = agent.get("http://api.androidhive.info/contacts/");
		Resource res2 = agent.get("http://api.androidhive.info/contacts/");
		Resource res3 = agent.get("http://api.androidhive.info/contacts/");
	}
}
