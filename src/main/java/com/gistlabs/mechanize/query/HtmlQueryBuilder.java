/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.query;

import com.gistlabs.mechanize.query.HtmlQuery.Pattern;

/** 
 * Contains all the static methods to start and extend a query.
 * <p> This is necessary since a object method can not have the same signature as the static methods.</p>
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class HtmlQueryBuilder {
	
	public static HtmlQuery everything() {
		return new HtmlQuery().everything();
	}
	
	public static HtmlQuery inBrackets(HtmlQuery query) {
		return new HtmlQuery().inBrackets(query);
	}

	public static HtmlQuery not(HtmlQuery query) {
		return new HtmlQuery().not(query);
	}

	public static HtmlQuery by(String attribute, String value) {
		return new HtmlQuery().by(attribute, value);
	}
	
	public static HtmlQuery by(String attribute, Pattern pattern) {
		return new HtmlQuery().by(attribute, pattern);
	}

	public static HtmlQuery by(String [] attributeNames, String value) {
		return new HtmlQuery().by(attributeNames, value);
	}

	public static HtmlQuery by(String [] attributeNames, Pattern pattern) {
		return new HtmlQuery().by(attributeNames, pattern);
	}

	public static String [] attributes(String ... attributeNames) {
		return attributeNames;
	}
	
	public static HtmlQuery byAny(String string) {
		return new HtmlQuery().byAny(string);
	}
	
	public static HtmlQuery byName(String string) {
		return new HtmlQuery().byName(string);
	}
	
	public static HtmlQuery byId(String string) {
		return new HtmlQuery().byId(string);
	}

	public static HtmlQuery byIdOrClassOrName(String string) {
		return new HtmlQuery().byIdOrClassOrName(string);
	}

	public static HtmlQuery byIdOrClass(String string) {
		return new HtmlQuery().byIdOrClass(string);
	}

	public static HtmlQuery byNameOrId(String string) {
		return new HtmlQuery().byNameOrId(string);
	}

	public static HtmlQuery byTag(String string) {
		return new HtmlQuery().byTag(string);
	}

	public static HtmlQuery byClass(String string) {
		return new HtmlQuery().byClass(string);
	}
	
	public static HtmlQuery byHRef(String string) {
		return new HtmlQuery().byHRef(string);
	}
	
	public static HtmlQuery bySrc(String string) {
		return new HtmlQuery().bySrc(string);
	}
	
	public static HtmlQuery byTitle(String string) {
		return new HtmlQuery().byTitle(string);
	}

	public static HtmlQuery byWidth(String string) {
		return new HtmlQuery().byWidth(string);
	}

	public static HtmlQuery byHeight(String string) {
		return new HtmlQuery().byHeight(string);
	}

	public static HtmlQuery byValue(String string) {
		return new HtmlQuery().byValue(string);
	}
	
	public static HtmlQuery byType(String string) {
		return new HtmlQuery().byType(string);
	}

	public static HtmlQuery byText(String string) {
		return new HtmlQuery().byText(string);
	}
	
	public static HtmlQuery byInnerHtml(String string) {
		return new HtmlQuery().byInnerHtml(string);
	}

	public static HtmlQuery byHtml(String string) {
		return new HtmlQuery().byHtml(string);
	}
	
	public static HtmlQuery byAny(Pattern pattern) {
		return new HtmlQuery().byAny(pattern);
	}

	public static HtmlQuery byName(Pattern pattern) {
		return new HtmlQuery().byName(pattern);
	}
	
	public static HtmlQuery byId(Pattern pattern) {
		return new HtmlQuery().byId(pattern);
	}

	public static HtmlQuery byNameOrId(Pattern pattern) {
		return new HtmlQuery().byNameOrId(pattern);
	}

	public static HtmlQuery byTag(Pattern pattern) {
		return new HtmlQuery().byTag(pattern);
	}

	public static HtmlQuery byClass(Pattern pattern) {
		return new HtmlQuery().byClass(pattern);
	}
	
	public static HtmlQuery byHRef(Pattern pattern) {
		return new HtmlQuery().byHRef(pattern);
	}
	
	public static HtmlQuery bySrc(Pattern pattern) {
		return new HtmlQuery().bySrc(pattern);
	}
	
	public static HtmlQuery byTitle(Pattern pattern) {
		return new HtmlQuery().byTitle(pattern);
	}

	public static HtmlQuery byWidth(Pattern pattern) {
		return new HtmlQuery().byWidth(pattern);
	}

	public static HtmlQuery byHeight(Pattern pattern) {
		return new HtmlQuery().byHeight(pattern);
	}

	public static HtmlQuery byValue(Pattern pattern) {
		return new HtmlQuery().byValue(pattern);
	}
	
	public static HtmlQuery byType(Pattern pattern) {
		return new HtmlQuery().byType(pattern); 
	}

	public static HtmlQuery byText(Pattern pattern) {
		return new HtmlQuery().byText(pattern); 
	}
	
	public static HtmlQuery byInnerHtml(Pattern pattern) {
		return new HtmlQuery().byInnerHtml(pattern);
	}
	
	public static HtmlQuery byHtml(Pattern pattern) {
		return new HtmlQuery().byHtml(pattern);
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
