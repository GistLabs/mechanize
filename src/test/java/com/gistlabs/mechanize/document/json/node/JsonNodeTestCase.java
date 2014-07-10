/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.node;

import com.gistlabs.mechanize.document.json.node.impl.ObjectNodeImpl;
import com.gistlabs.mechanize.document.json.node.impl.TestElementBaseClass;

public abstract class JsonNodeTestCase extends TestElementBaseClass {

	public static JsonNode from(String json) {
		return new ObjectNodeImpl(parseJson(json));
	}
			
}
