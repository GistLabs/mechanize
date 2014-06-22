/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.cookie;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeAgent;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class CookiesTest {

	@Test
	public void testCookieRepresentiveIsTheSame() {
		MechanizeAgent agent = new MechanizeAgent();
		Cookie cookie = agent.cookies().addNewCookie("ID", "Value", ".test.com");
		assertSame(cookie, agent.cookies().get("ID", ".test.com"));
		assertSame(cookie, agent.cookies().get("ID", ".test.com"));
	}

	@Test
	public void testRemovingASingleCookie() {
		MechanizeAgent agent = new MechanizeAgent();
		Cookie cookie1 = agent.cookies().addNewCookie("ID", "MyID", ".wikipedia.org");
		Cookie cookie2 = agent.cookies().addNewCookie("ID2", "MySecondID", "wikipedia.org");

		agent.cookies().remove(cookie1);
		assertFalse(agent.cookies().getAll().contains(cookie1));
		assertTrue(agent.cookies().getAll().contains(cookie2));
	}
}
