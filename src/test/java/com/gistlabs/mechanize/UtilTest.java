/*
 * Copyright (C) 2011 Gist Labs, LLC.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.gistlabs.mechanize;

import static org.junit.Assert.*;

import com.gistlabs.mechanize.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class UtilTest {
	@Test
	public void testFindFirstByTagSingleTag() {
		Document document = Jsoup.parse("<html><body><a href=\"A\">A</a><a href=\"B\">B</a></body></html>");
		assertNotNull(Util.findFirstByTag(document, "a"));
		assertNotNull(Util.findFirstByTag(document, "body"));
		assertNotNull(Util.findFirstByTag(document, "body/a"));
		assertNotNull(Util.findFirstByTag(document, "html/body/a"));
		assertNotNull(Util.findFirstByTag(document, "html/a"));
		
		assertNull(Util.findFirstByTag(document, "body/html/a"));
		assertNull(Util.findFirstByTag(document, "body/unknown"));
	}
}
