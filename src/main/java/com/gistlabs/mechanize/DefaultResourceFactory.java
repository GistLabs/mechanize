/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.ResourceFactory;

public class DefaultResourceFactory implements ResourceFactory {

	@Override
	public Collection<String> getContentMatches() {
		return DefaultResource.CONTENT_MATCHERS;
	}

	@Override
	public Resource buildPage(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
		return new DefaultResource(agent, request, response);
	}

}
