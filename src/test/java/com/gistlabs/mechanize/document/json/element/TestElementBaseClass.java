/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.element;

import org.json.JSONException;
import org.json.JSONObject;

import com.gistlabs.mechanize.document.json.exceptions.JsonException;

public abstract class TestElementBaseClass {

	public TestElementBaseClass() {
		super();
	}

	protected JSONObject parseJson(final String json) {
		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

}