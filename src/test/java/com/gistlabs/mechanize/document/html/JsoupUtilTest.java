/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.gistlabs.mechanize.document.html.JsoupDataUtil;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class JsoupUtilTest {
	@Test
	public void testFindFirstByTagSingleTag() {
		Document document = Jsoup.parse("<html><body><a href=\"A\">A</a><a href=\"B\">B</a></body></html>");
		assertNotNull(JsoupDataUtil.findFirstByTag(document, "a"));
		assertNotNull(JsoupDataUtil.findFirstByTag(document, "body"));
		assertNotNull(JsoupDataUtil.findFirstByTag(document, "body/a"));
		assertNotNull(JsoupDataUtil.findFirstByTag(document, "html/body/a"));
		assertNotNull(JsoupDataUtil.findFirstByTag(document, "html/a"));
		
		assertNull(JsoupDataUtil.findFirstByTag(document, "body/html/a"));
		assertNull(JsoupDataUtil.findFirstByTag(document, "body/unknown"));
	}
}
