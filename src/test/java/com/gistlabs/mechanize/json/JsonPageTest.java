/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.json;

import static org.junit.Assert.*;

import org.apache.http.entity.ContentType;
import org.junit.Test;

import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.Page;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class JsonPageTest extends MechanizeTestCase {
	
	@Test
	public void testLoadJson() {
		agent.addPageRequest("GET", "http://test.com", getClass().getResourceAsStream("dropbox.account.info.json")).setContentType(ContentType.APPLICATION_JSON.getMimeType());		
		Page page = agent.get("http://test.com");
		assertNotNull(page);
		assertEquals(JsonPage.class, page.getClass());
	}
	
	@Test
	public void testParseJson() {
		agent.addPageRequest("GET", "http://test.com", getClass().getResourceAsStream("dropbox.account.info.json")).setContentType(ContentType.APPLICATION_JSON.getMimeType());		
		JsonPage page = (JsonPage) agent.get("http://test.com");
		assertNotNull(page.json());
	}
}