/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

public class ContentPage extends Page {

	public ContentPage(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
		super(agent, request, response);
	}
}
