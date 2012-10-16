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