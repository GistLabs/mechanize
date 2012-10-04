/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import static com.gistlabs.mechanize.query.QueryBuilder.byClass;
import static com.gistlabs.mechanize.query.QueryBuilder.byName;
import static com.gistlabs.mechanize.query.QueryBuilder.byTitle;
import static com.gistlabs.mechanize.query.QueryBuilder.regEx;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.form.Form;
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
		Page englishPage = link.click();
		assertEquals("Wikipedia, the free encyclopedia", englishPage.getTitle());
	}
	
	@Test
	public void testSearchingWikipediaForAngelaMerkelInGermanLanguageUtilizingSelectAndTextInput() {
		MechanizeAgent agent = new MechanizeAgent();
		Page page = agent.get("http://www.wikipedia.org");
		Form form = page.forms().get(byClass("search-form"));
		form.getSelect(byName("language")).getOption("de").select();
		form.getSearch(byName("search")).set("Angela Merkel"); 
		Page response = form.getSubmitButton(byName("go")).submit();
		assertTrue(response.getTitle().startsWith("Angela Merkel"));
	}
}
