/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.junit.Test;

import com.gistlabs.mechanize.Mechanize;
import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.json.JsonDocument;
import com.gistlabs.mechanize.impl.MechanizeAgent;

/**
 *
 */
public class GoogleApiJsonIT {
	String googleUrl = "https://www.googleapis.com/urlshortener/v1/url";
	String shortUrl = "http://goo.gl/daal8";
	String longUrl = "http://gistlabs.com/software/mechanize-for-java/";

	@Test
	public void testShortUrl() throws JSONException {
		Mechanize agent = new MechanizeAgent();
		HtmlDocument html = agent.get(shortUrl);
		assertEquals(longUrl, html.getUri());
	}

	@Test
	public void testGoogleApi() throws JSONException {
		MechanizeAgent agent = new MechanizeAgent();
		JsonDocument json = agent.doRequest(googleUrl).add("shortUrl", shortUrl).add("projection", "FULL").get();
		//String debug = json.asString();

		assertEquals(longUrl, json.getRoot().find("longUrl").getValue());

		//FRAGILE TEST no longer works
		//String value = json.getRoot().find("analytics month countries#US count").getValue();
		//assertTrue(value, Integer.valueOf(value)>=1);
	}
}
