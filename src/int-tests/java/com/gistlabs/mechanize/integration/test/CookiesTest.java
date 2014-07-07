/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.gistlabs.mechanize.Mechanize;
import com.gistlabs.mechanize.impl.MechanizeAgent;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class CookiesTest {
	//@Test // no longer working
	public void xtestWikipediaSendsNoCookies() {
		Mechanize agent = new MechanizeAgent();

		agent.get("http://www.wikipedia.org");
		assertEquals(0, agent.cookies().getCount());
	}


	/** Tests if google stores two cookies. It uses a special link to prevent google form choosing a different homepage depending on the ip being used. */
	@Test
	public void testGoogleComSendsTwoCookies() {
		MechanizeAgent agent = new MechanizeAgent();

		agent.
		doRequest("https://www.google.co.uk/setprefdomain?prefdom=US&sig=0_iEtQ0487gjqkcvDjBk5XCH1G_WU%3D").
		addHeader("Accept-Language", "en-US").
		get();
		assertEquals(2, agent.cookies().getCount());
		assertNotNull(agent.cookies().get("NID", ".google.co.uk"));
		assertNotNull(agent.cookies().get("PREF", ".google.co.uk"));
	}

}
