/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.Page;
import com.gistlabs.mechanize.document.html.form.Form;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class GoogleSearchForMechanizeJavaIT {

	@Test
	public void testGooglePageSearchForm() {
		MechanizeAgent agent = new MechanizeAgent();
		Page page = agent.get("http://www.google.com");
		Form form = page.form("f");
		form.get("q").set("mechanize java");
		Resource response = form.submit();
		assertTrue(response.getTitle().startsWith("mechanize java"));
	}
}
