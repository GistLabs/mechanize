/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.image;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.document.AbstractDocument;
import com.gistlabs.mechanize.util.apache.ContentType;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class ImagesTest extends MechanizeTestCase {

	protected String contentType() {
		return ContentType.TEXT_HTML.getMimeType();
	}

	@Test
	public void testFollowingAnAbsoluteLink() {
		addPageRequest("http://www.test.com", 
				newHtml("Test Page", "<img src=\"test.png\"/>"));
		
		AbstractDocument page = agent().get("http://www.test.com");
		Image image = page.images().find("*[src='test.png']"); 
		assertEquals("http://www.test.com/test.png", image.getAbsoluteSrc());
	}
}
