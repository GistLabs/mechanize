/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;
import com.gistlabs.mechanize.filters.MechanizeFilter;

class MechanizeHttpClientFilter implements MechanizeFilter {
	private final AbstractHttpClient client;

	MechanizeHttpClientFilter(final AbstractHttpClient client) {
		this.client = client;
	}

	@Override
	public HttpResponse execute(final HttpUriRequest request, final HttpContext context) {
		try {
			return client.execute(request, context);
		} catch (Exception e) {
			throw MechanizeExceptionFactory.newException(e);
		}
	}
}