/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.image;

import static com.gistlabs.mechanize.query.QueryBuilder.bySrc;
import static org.junit.Assert.assertEquals;
import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.image.Image;

import org.junit.Test;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class ImagesTest extends MechanizeTestCase {
	@Test
	public void testFollowingAnAbsoluteLink() {
		agent.addPageRequest("http://www.test.com", 
				newHtml("Test Page", "<img src=\"test.png\"/>"));
		
		Page page = agent.get("http://www.test.com");
		Image image = page.images().get(bySrc("test.png")); 
		assertEquals("http://www.test.com/test.png", image.getAbsoluteSrc());
	}
}
