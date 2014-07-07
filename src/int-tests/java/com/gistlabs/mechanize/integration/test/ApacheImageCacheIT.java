/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.junit.Test;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.filters.MechanizeChainFilter;
import com.gistlabs.mechanize.filters.MechanizeFilter;
import com.gistlabs.mechanize.impl.MechanizeAgent;

/**
 * Test a well known and http cachable resource
 */
public class ApacheImageCacheIT {

	@Test
	public void testCachedResource() throws JSONException, Exception {
		MechanizeAgent agent = new MechanizeAgent();
		Resource res1 = agent.get("http://apache.org/images/feather-small.gif");
		assertNull(res1.getResponse().getFirstHeader("Via"));

		Resource res2 = agent.get("http://apache.org/images/feather-small.gif");
		assertTrue(res2.getResponse().getFirstHeader("Via").getValue().indexOf("mechanize")>-1);

		assertEquals(date(res1), date(res2));

		// tweak response to shorten cache time
		res2.getResponse().setHeader("Cache-Control", "max-age=0;must-revalidate");
		res2.getResponse().removeHeaders("Expires");

		// check between the cache and HttpClient to make sure the response is 304 not modified
		agent.addFilter(new MechanizeChainFilter() {
			@Override
			public HttpResponse execute(final HttpUriRequest request, final HttpContext context, final MechanizeFilter chain) {
				HttpResponse response = chain.execute(request, context);
				assertEquals(304, response.getStatusLine().getStatusCode());
				return response;
			}
		});

		Resource res3 = agent.get("http://apache.org/images/feather-small.gif");
		assertTrue(res3.getResponse().getFirstHeader("Via").getValue().indexOf("mechanize")>-1);
	}

	protected Date date(final Resource res1) throws DateParseException {
		return DateUtils.parseDate(res1.getResponse().getFirstHeader("Date").getValue());
	}
}
