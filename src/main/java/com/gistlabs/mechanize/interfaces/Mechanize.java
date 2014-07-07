/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.interfaces;

import org.apache.http.HttpEntity;

import com.gistlabs.mechanize.parameters.Parameters;
import com.gistlabs.mechanize.requestor.RequestBuilder;

/**
 */
public interface Mechanize {

	/**
	 * Generate a RequestBuilder based on this Mechanize
	 * 
	 * @param uri The starting uri
	 * @return
	 */
	public RequestBuilder<Resource> doRequest(final String uri);

	/**
	 * GET a URI
	 * @param uri
	 * @return
	 */
	public <T extends Resource> T get(final String uri);

	/**
	 * PUT an Entity to a URI
	 * @param uri
	 * @return
	 */
	public <T extends Resource> T put(final String uri, HttpEntity entity);

	/**
	 * DELETE a URI
	 * @param uri
	 * @param params
	 * @return
	 */
	public <T extends Resource> T delete(final String uri);

	/**
	 * POST either URL encoded or multi-part encoded content body, based on presence of file content body parameters
	 * @param uri
	 * @param params
	 * @return
	 */
	public <T extends Resource> T post(final String uri, final Parameters params);

	/**
	 * POST an Entity
	 * @param uri
	 * @param params
	 * @return
	 */
	public <T extends Resource> T post(final String uri, final Parameters params, HttpEntity entity);
}
