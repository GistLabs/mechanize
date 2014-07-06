/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import static com.gistlabs.mechanize.util.css.CSSHelper.byIdOrName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gistlabs.mechanize.Mechanize;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.Document;
import com.gistlabs.mechanize.document.html.form.Form;
import com.gistlabs.mechanize.document.link.Link;
import com.gistlabs.mechanize.document.link.Links;
import com.gistlabs.mechanize.impl.MechanizeAgent;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class WikipediaSearchForAngelaMerkelAndDownloadingImagesIT {

	@Test
	public void testLoadWikipediaIndexPage() {
		Mechanize agent = new MechanizeAgent();
		Document page = agent.get("http://www.wikipedia.org");
		assertNotNull(page);
		assertTrue(page.size() > 10000);
		Links links = page.links();
		assertTrue(links.size() > 10);
		assertNotNull(links.find("*[title*='English']"));
	}

	@Test
	public void testClickingEnglishWikipediaVersionLink() {
		Mechanize agent = new MechanizeAgent();
		Document page = agent.get("http://www.wikipedia.org");
		assertNotNull(page);
		assertTrue(page.size() > 10000);
		Links links = page.links();
		assertTrue(links.size() > 10);
		Link link = links.find("*[title*='English']");
		assertNotNull(link);
		Resource englishPage = link.click();
		assertEquals("Wikipedia, the free encyclopedia", englishPage.getTitle());
	}

	@Test
	public void testSearchingWikipediaForAngelaMerkelInGermanLanguageUtilizingSelectAndTextInput() {
		Mechanize agent = new MechanizeAgent();
		Document page = agent.get("http://www.wikipedia.org");
		Form form = page.forms().find(".search-form");
		form.findSelect(byIdOrName("language")).getOption("de").select();
		form.findSearch(byIdOrName("search")).set("Angela Merkel");
		Resource response = form.findSubmitButton(byIdOrName("go")).submit();
		assertTrue(response.getTitle().startsWith("Angela Merkel"));
	}
}
