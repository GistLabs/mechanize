/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import static com.gistlabs.mechanize.document.html.query.HtmlQueryBuilder.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.Page;
import com.gistlabs.mechanize.form.Form;
import com.gistlabs.mechanize.image.Image;
import com.gistlabs.mechanize.link.Link;
import com.gistlabs.mechanize.link.Links;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class WikipediaSearchForAngelaMerkelAndDownloadingImagesIT {
	
	@Test
	public void testLoadWikipediaIndexPage() {
		MechanizeAgent agent = new MechanizeAgent();
		Page page = agent.get("http://www.wikipedia.org");
		assertNotNull(page);
		assertTrue(page.size() > 10000);
		Links links = page.links();
		assertTrue(links.size() > 10);
		assertNotNull(links.get(byTitle(regEx(".*English.*"))));
	}
	
	@Test
	public void testClickingEnglishWikipediaVersionLink() {
		MechanizeAgent agent = new MechanizeAgent();
		Page page = agent.get("http://www.wikipedia.org");
		assertNotNull(page);
		assertTrue(page.size() > 10000);
		Links links = page.links();
		assertTrue(links.size() > 10);
		Link link = links.get(byTitle(regEx("English.*")));
		assertNotNull(link);
		Resource englishPage = link.click();
		assertEquals("Wikipedia, the free encyclopedia", englishPage.getTitle());
	}
	
	@Test
	public void testSearchingWikipediaForAngelaMerkelInGermanLanguageUtilizingSelectAndTextInput() {
		MechanizeAgent agent = new MechanizeAgent();
		Page page = agent.get("http://www.wikipedia.org");
		Form form = page.forms().get(byClass("search-form"));
		form.getSelect(byName("language")).getOption("de").select();
		form.getSearch(byName("search")).set("Angela Merkel"); 
		Resource response = form.getSubmitButton(byName("go")).submit();
		assertTrue(response.getTitle().startsWith("Angela Merkel"));
	}
	
	@Test
	public void testDownloadWikipediaLogoImagesToBuffer() {
		MechanizeAgent agent = new MechanizeAgent();
		Page page = agent.get("http://www.wikipedia.org");
		List<Image> images = page.images().getAll(byHtml(regEx(".*Wikipedia.*")));
		assertEquals(2, images.size());
		assertEquals(2479, images.get(0).get().getLength());
		assertEquals(45283, images.get(1).get().getLength());
	}
}
