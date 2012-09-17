/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.cookie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.RequestInterceptor;
import com.gistlabs.mechanize.cookie.Cookie;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class CookiesTest {
	@Test
	public void testWikipediaSendsNoCookies() {
		MechanizeAgent agent = new MechanizeAgent();
		
		agent.get("http://www.wikipedia.org");
		assertEquals(0, agent.cookies().getCount());
	}

	@Test
	public void testCookieRepresentiveIsTheSame() {
		MechanizeAgent agent = new MechanizeAgent();
		Cookie cookie = agent.cookies().addNewCookie("ID", "Value", ".test.com");
		assertSame(cookie, agent.cookies().get("ID", ".test.com"));
		assertSame(cookie, agent.cookies().get("ID", ".test.com"));
	}

	/** Tests if google stores two cookies. It uses a special link to prevent google form choosing a different homepage depending on the ip being used. */
	@Test
	public void testGoogleComSendsTwoCookies() {
		MechanizeAgent agent = new MechanizeAgent();
		agent.addInterceptor(new RequestInterceptor() {
			public void intercept(MechanizeAgent agent, HttpRequestBase request) {
				request.addHeader("Accept-Language", "en-US");
			}
		});
		
		agent.get("https://www.google.co.uk/setprefdomain?prefdom=US&sig=0_iEtQ0487gjqkcvDjBk5XCH1G_WU%3D");
		assertEquals(2, agent.cookies().getCount());
		assertNotNull(agent.cookies().get("NID", ".google.co.uk"));
		assertNotNull(agent.cookies().get("PREF", ".google.co.uk")); 
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
