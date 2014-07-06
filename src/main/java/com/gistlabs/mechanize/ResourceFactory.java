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

/**
 * This is a page factory, that can be registered 
 * 
 * 
 * @author jheintz
 *
 */
public interface ResourceFactory {
	Collection<String> getContentMatches();
	Resource buildPage(Mechanize agent, HttpRequestBase request, HttpResponse response);
}
