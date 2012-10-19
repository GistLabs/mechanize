/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.impl;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeTestCase;

/**
 */
public class JsonCssPerformanceTest extends MechanizeTestCase {

	@Test
	public void testPerformance() {
		for (int i = 0; i < 100; i++)
			new JsonPageTest().testParseJson();
	}
}