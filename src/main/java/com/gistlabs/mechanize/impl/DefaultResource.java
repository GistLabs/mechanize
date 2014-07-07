/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.impl;

import java.util.Arrays;
import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.gistlabs.mechanize.AbstractResource;
import com.gistlabs.mechanize.Mechanize;
import com.gistlabs.mechanize.util.apache.ContentType;

public class DefaultResource extends AbstractResource {
	public static Collection<String> CONTENT_MATCHERS = 
			Arrays.asList(
				ContentType.WILDCARD.getMimeType(), 
				ContentType.APPLICATION_OCTET_STREAM.getMimeType(), 
				ContentType.DEFAULT_BINARY.getMimeType(), 
				ContentType.DEFAULT_TEXT.getMimeType());
	
	public DefaultResource(Mechanize agent, HttpRequestBase request, HttpResponse response) {
		super(agent, request, response);
	}
}
