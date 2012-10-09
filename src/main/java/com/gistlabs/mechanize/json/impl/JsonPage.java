/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.json.impl;

import java.io.InputStreamReader;
import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;
import com.gistlabs.mechanize.util.Collections;

public class JsonPage extends Page {
	public static Collection<String> CONTENT_MATCHERS = 
		Collections.collection(
				ContentType.APPLICATION_JSON.getMimeType());
	
	private JSONObject json;
	
	public JsonPage(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
		super(agent, request, response);
	}

	@Override
	protected void loadPage() throws Exception {
		try {
			this.json = new JSONObject(new JSONTokener(new InputStreamReader(getInputStream())));
		} catch (Exception e) {
			throw MechanizeExceptionFactory.newException(e);
		}	
	}
	
	public JSONObject json() {
		return this.json;
	}
}
