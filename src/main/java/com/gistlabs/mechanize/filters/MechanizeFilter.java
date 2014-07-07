/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.filters;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

/**
 * Inspired by Servlet filters (and CPS) this represents the rest of the filter execution.
 *
 */
public interface MechanizeFilter {

	/**
	 * 
	 */
	public HttpResponse execute(HttpUriRequest request, HttpContext context);

}
