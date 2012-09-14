/*
 * Copyright (C) 2011 Gist Labs, LLC.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.gistlabs.mechanize;

import com.gistlabs.mechanize.Query.Pattern;

/** 
 * Contains all the static methods to start and extend a query.
 * <p> This is necessary since a object method can not have the same signature as the static methods.</p>
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class QueryBuilder {
	
	public static Query everything() {
		return new Query().everything();
	}
	
	public static Query byAny(String string) {
		return new Query().byAny(string);
	}

	public static Query byName(String string) {
		return new Query().byName(string);
	}
	
	public static Query byId(String string) {
		return new Query().byId(string);
	}

	public static Query byNameOrId(String string) {
		return new Query().byNameOrId(string);
	}

	public static Query byTag(String string) {
		return new Query().byTag(string);
	}

	public static Query byClass(String string) {
		return new Query().byClass(string);
	}
	
	public static Query byHRef(String string) {
		return new Query().byHRef(string);
	}
	
	public static Query bySrc(String string) {
		return new Query().bySrc(string);
	}
	
	public static Query byTitle(String string) {
		return new Query().byTitle(string);
	}

	public static Query byWidth(String string) {
		return new Query().byWidth(string);
	}

	public static Query byHeight(String string) {
		return new Query().byHeight(string);
	}

	public static Query byValue(String string) {
		return new Query().byValue(string);
	}
	
	public static Query byType(String string) {
		return new Query().byType(string);
	}
	
	public static Query byInnerHtml(String string) {
		return new Query().byInnerHtml(string);
	}

	public static Query byHtml(String string) {
		return new Query().byHtml(string);
	}
	
	public static Query byAny(Pattern pattern) {
		return new Query().byAny(pattern);
	}

	public static Query byName(Pattern pattern) {
		return new Query().byName(pattern);
	}
	
	public static Query byId(Pattern pattern) {
		return new Query().byId(pattern);
	}

	public static Query byNameOrId(Pattern pattern) {
		return new Query().byNameOrId(pattern);
	}

	public static Query byTag(Pattern pattern) {
		return new Query().byTag(pattern);
	}

	public static Query byClass(Pattern pattern) {
		return new Query().byClass(pattern);
	}
	
	public static Query byHRef(Pattern pattern) {
		return new Query().byHRef(pattern);
	}
	
	public static Query bySrc(Pattern pattern) {
		return new Query().bySrc(pattern);
	}
	
	public static Query byTitle(Pattern pattern) {
		return new Query().byTitle(pattern);
	}

	public static Query byWidth(Pattern pattern) {
		return new Query().byWidth(pattern);
	}

	public static Query byHeight(Pattern pattern) {
		return new Query().byHeight(pattern);
	}

	public static Query byValue(Pattern pattern) {
		return new Query().byValue(pattern);
	}
	
	public static Query byType(Pattern pattern) {
		return new Query().byType(pattern); 
	}
	
	public static Query byInnerHtml(Pattern pattern) {
		return new Query().byInnerHtml(pattern);
	}
	
	public static Query byHtml(Pattern pattern) {
		return new Query().byHtml(pattern);
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
}
