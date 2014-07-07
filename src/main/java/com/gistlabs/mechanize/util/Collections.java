/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.util;

import java.util.Collection;
import java.util.HashSet;

public class Collections {
//	@SafeVarargs
	public static <T> Collection<T> collection(T... entries) {
		HashSet<T> result = new HashSet<T>(entries.length);
		for (T entry : entries) {
			result.add(entry);
		}
		return result;
	}
}
