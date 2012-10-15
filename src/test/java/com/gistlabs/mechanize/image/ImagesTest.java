/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.image;

import static com.gistlabs.mechanize.html.query.HtmlQueryBuilder.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.document.Page;

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
	
	@Test
	public void testLoadingAllImages() {
		agent.addPageRequest("http://www.test.com", 
				newHtml("Test Page", "<img src=\"test.png\"/><img src=\"test2.png\"/>"));
		agent.addPageRequest("http://www.test.com/test.png", "ImageContent");
		agent.addPageRequest("http://www.test.com/test2.png", "ImageContent");
		
		Page page = agent.get("http://www.test.com");
		ImageCollection imageCollection = page.images().loadAll();
		assertEquals(2, imageCollection.size());
		assertTrue(imageCollection.hasLoaded(page.images().get(0)));
		assertTrue(imageCollection.hasLoaded(page.images().get(1)));
	}
	
	@Test
	public void tesLoadingAllMissingImages() {
		agent.addPageRequest("http://www.test.com", 
				newHtml("Test Page", "<img src=\"test.png\"/>"));
		
		agent.addPageRequest("http://www.test.com/test.png", "ImageContent");
		
		agent.addPageRequest("http://www.test.com/2",  
				newHtml("Test Page", "<img src=\"test.png\"/><img src=\"test2.png\"/>"));

		agent.addPageRequest("http://www.test.com/test2.png", "ImageContent");
		
		Page page = agent.get("http://www.test.com");
		ImageCollection imageCollection = new ImageCollection();
		page.images().loadAllMissing(imageCollection);
		assertEquals(1, imageCollection.size());
		
		page = agent.get("http://www.test.com/2");
		page.images().loadAllMissing(imageCollection);
		assertEquals(2, imageCollection.size());
	}
}
