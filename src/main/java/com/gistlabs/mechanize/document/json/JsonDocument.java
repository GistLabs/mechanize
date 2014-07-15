/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.gistlabs.mechanize.Mechanize;
import com.gistlabs.mechanize.document.AbstractDocument;
import com.gistlabs.mechanize.document.json.node.JsonNode;
import com.gistlabs.mechanize.document.json.node.impl.ArrayNodeImpl;
import com.gistlabs.mechanize.document.json.node.impl.ObjectNodeImpl;
import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;
import com.gistlabs.mechanize.util.apache.ContentType;

public class JsonDocument extends AbstractDocument {
	public static Collection<String> CONTENT_MATCHERS =
			Arrays.asList(
					ContentType.APPLICATION_JSON.getMimeType());

	private JsonNode json;

	public JsonDocument(final Mechanize agent, final HttpRequestBase request, final HttpResponse response) {
		super(agent, request, response);
	}

	@Override
	protected void loadPage() throws Exception {
		try {
			JSONTokener jsonTokener = new JSONTokener(new InputStreamReader(getInputStream()));
			char nextClean = jsonTokener.nextClean(); jsonTokener.back();
			
			switch (nextClean) {
			case '{':
				this.json = new ObjectNodeImpl(new JSONObject(jsonTokener));
				break;
			case '[':
				this.json = new ArrayNodeImpl(new JSONArray(jsonTokener));
				break;
			default:
				throw new IllegalStateException(String.format("Error processing token=%s from request=%s",nextClean, this.getRequest()));
			}
		} catch (Exception e) {
			throw MechanizeExceptionFactory.newException(e);
		}
	}

	@Override
	public JsonNode getRoot() {
		return this.json;
	}
}
