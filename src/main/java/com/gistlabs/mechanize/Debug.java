/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.http.Header;
import org.apache.http.HttpMessage;

/**
 * This is a helper class to easily show the request and response objects in a resource
 *
 */
class Debug {
	private final Resource resource;

	public Debug(final Resource resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		StringWriter result = new StringWriter();
		PrintWriter writer = new PrintWriter(result);

		writer.println("== Request ==");
		writer.println(resource.getRequest().getRequestLine().toString());
		write(resource.getRequest(), writer);

		writer.println();
		writer.println("== Response ==");
		writer.println(resource.getResponse().getStatusLine().toString());
		write(resource.getResponse(), writer);

		return result.toString();
	}

	private void write(final HttpMessage message, final PrintWriter writer) {
		Header[] headers = message.getAllHeaders();
		for (Header header : headers)
			writer.println(header.toString());
	}
}
