/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.exceptions;

import org.json.JSONArray;

public class JsonArrayException extends JsonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 253115664519387324L;
	private JSONArray array;

	public JsonArrayException() {
	}

	public JsonArrayException(final String arg, final JSONArray array) {
		super(arg);
		this.array = array;
	}

	public JsonArrayException(final Throwable th) {
		super(th);
	}

	public JsonArrayException(final String arg, final Throwable th) {
		super(arg, th);
	}

	public JSONArray getArray() {
		return array;
	}
}
