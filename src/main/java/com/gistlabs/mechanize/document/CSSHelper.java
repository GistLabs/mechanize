/**
 * Copyright (C) 2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document;

/**
 * Generate CSS Selectors, a convenience module
 * 
 * @author John Heintz
 *
 */
public class CSSHelper {
	public static String byName(String s) {
		return String.format("*[name=\"%s\"]", s);
	}
	public static String byIdOrName(String s) {
		return String.format("#%s, *[name=\"%s\"]", s, s);
	}
	public static String byIdOrClass(String s) {
		return String.format("#%s, .%s", s, s);
	}
	public static String byIdOrClassOrName(String s) {
		return String.format("#%s, .%s, *[name=\"%s\"]", s, s, s);
	}
	public static String byIdOrNameWithValue(String idName, String value) {
		return String.format("#%s[value=\"%s\"], *[name=\"%s\"][value=\"%s\"]", idName, value, idName, value);
	}
	public static String contains(String s) {
		return String.format("*:contains('%s')", s);
	}
}
