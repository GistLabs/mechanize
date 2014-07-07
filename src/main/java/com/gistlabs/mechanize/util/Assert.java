/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.util;

/**
 * Provides Assertion methods for parameters and alike.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class Assert {
	public static void notNull(Object object) {
		if(object == null)
			throw new NullPointerException();
	}  

	public static void notNull(Object object, String message) {
		if(object == null)
			throw new NullPointerException(message);
	}
}
