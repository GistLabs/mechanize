/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json;

import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.gistlabs.mechanize.Mechanize;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.ResourceFactory;

public class JsonDocumentFactory implements ResourceFactory {

	@Override
	public Collection<String> getContentMatches() {
		return JsonDocument.CONTENT_MATCHERS;
	}

	@Override
	public Resource buildPage(final Mechanize agent, final HttpRequestBase request, final HttpResponse response) {
		return new JsonDocument(agent, request, response);
	}

}
