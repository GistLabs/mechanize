/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.query;

/**
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class AbstractQueryBuilder {

	public static String [] attributes(String ... attributeNames) {
		return attributeNames;
	}

	public static Pattern string(String string) {
		return new Pattern(string, false);
	}

	public static Pattern regEx(String pattern) {
		return new Pattern(pattern, true);
	}

	public static Pattern caseInsensitive(String string) {
		return new Pattern(string, false).setCompareLowerCase(true);
	}

	public static Pattern caseInsensitive(Pattern pattern) {
		return new Pattern(pattern.getValue(), pattern.isRegularExpression()).setCompareLowerCase(true);
	}

	public AbstractQueryBuilder() {
		super();
	}

}