/*
 * Copyright (C) 2011 Gist Labs, LLC.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.gistlabs.mechanize.integration.test;

import static org.junit.Assert.assertTrue;
import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.form.Form;

import org.junit.Test;

import static com.gistlabs.mechanize.QueryBuilder.*;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class GoogleSearchForAngelaMerkelIT {

	@Test
	public void testGooglePageSearchForm() {
		MechanizeAgent agent = new MechanizeAgent();
		Page page = agent.get("http://www.google.com");
		Form form = page.forms().get(byName("f"));
		form.get("q").set("Angela Merkel");
		Page response = form.submit();
		assertTrue(response.getTitle().startsWith("Angela Merkel"));
	}
}
