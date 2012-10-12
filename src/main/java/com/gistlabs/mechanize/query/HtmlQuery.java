/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.query;

import java.util.ArrayList;
import java.util.List;

import com.gistlabs.mechanize.html.HtmlSpecialAttributes;
import static com.gistlabs.mechanize.query.HtmlQueryBuilder.*;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class HtmlQuery {
	
	private final List<QueryPart> parts = new ArrayList<QueryPart>(); 
	public final HtmlQuery or;
	public final HtmlQuery and;
	
	HtmlQuery() {
		this.or = this;
		this.and = new AndQuery(this);
	}
	
	HtmlQuery(HtmlQuery parent) {
		this.or = parent.or;
		this.and = this;
	}
	
	HtmlQuery add(QueryPart part) {
		if(this instanceof AndQuery) {
			((AndQuery)this).parent.parts.add(part);
			return ((AndQuery)this).parent;
		}
		else {
			this.parts.add(part);
			return this;
		}
	}
	
	private static class AndQuery extends HtmlQuery {
		private final HtmlQuery parent; 
		public AndQuery(HtmlQuery parentQuery) {
			super(parentQuery);
			this.parent = parentQuery;
		}
		
		@Override
		public String toString() {
			return parent.toString();
		}
	}

	HtmlQuery add(String attributeName, Object stringOrPattern) {
		return add(attributes(attributeName), stringOrPattern);
	}
	
	HtmlQuery add(String [] attributeNames, Object stringOrPattern) {
		Pattern pattern = stringOrPattern instanceof String ? new Pattern((String)stringOrPattern, false) : (Pattern)stringOrPattern;
		return this.add(new QueryPart(this instanceof AndQuery, attributeNames, pattern));
	}
	
	public boolean matches(QueryStrategy queryStrategy, Object object) {
		if(this instanceof AndQuery) {
			return ((AndQuery)this).parent.matches(queryStrategy, object);
		}
		else {
			boolean last = false;
			for(QueryPart part : parts) {
				if(part.isAnd() && last == true) {
					boolean current = part.matches(queryStrategy, object);
					last = current;
				}
				else if(part.isAnd() && last == false) {
					last = false;
				}
				else if(!part.isAnd() && last == true) {
					return true;
				}
				else {
					last = part.matches(queryStrategy, object);
				}
			}
			return last;
		}
	}
	
	public HtmlQuery everything() {
		return add(new EverythingQueryPart(this instanceof AndQuery));
	}
	
	public HtmlQuery inBrackets(HtmlQuery query) {
		return add(new InBracketsQueryPart(this instanceof AndQuery, query));
	}

	public HtmlQuery not(HtmlQuery query) {
		return add(new NotQueryPart(this instanceof AndQuery, query));
	}

	public HtmlQuery by(String attribute, String value) {
		return add(attribute, value);
	}

	public HtmlQuery by(String attribute, Pattern pattern) {
		return add(attribute, pattern);
	}

	public HtmlQuery by(String [] attributeNames, String value) {
		return add(attributeNames, value);
	}

	public HtmlQuery by(String [] attributeNames, Pattern value) {
		return add(attributeNames, value);
	}
	
	public HtmlQuery byAny(String string) {
		return add("*", string);
	}

	public HtmlQuery byAny(Pattern pattern) {
		return add("*", pattern);
	}

	public HtmlQuery byName(String string) {
		return add("name", string);
	}

	public HtmlQuery byName(Pattern pattern) {
		return add("name", pattern);
	}
	
	public HtmlQuery byId(String string) {
		return add("id", string);
	}

	public HtmlQuery byId(Pattern pattern) {
		return add("id", pattern);
	}

	public HtmlQuery byIdOrClass(String string) {
		return add(attributes("id", HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_CLASS_NAMES), string);
	}

	public HtmlQuery byIdOrClass(Pattern pattern) {
		return add(attributes("id", HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_CLASS_NAMES), pattern);
	}

	public HtmlQuery byIdOrClassOrName(String string) {
		return add(attributes("id", "name", HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_CLASS_NAMES), string);
	}

	public HtmlQuery byIdOrClassOrName(Pattern pattern) {
		return add(attributes("id", "name", HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_CLASS_NAMES), pattern);
	}

	public HtmlQuery byNameOrId(String string) {
		return add(attributes("name", "id"), string);
	}

	public HtmlQuery byNameOrId(Pattern pattern) {
		return add(attributes("name", "id"), pattern);
	}
	
	public HtmlQuery byTag(String string) {
		return add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TAG_NAME, string);
	}

	public HtmlQuery byTag(Pattern pattern) {
		return add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TAG_NAME, pattern);
	}

	public HtmlQuery byClass(String string) {
		return add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_CLASS_NAMES, string);
	}
	
	public HtmlQuery byClass(Pattern pattern) {
		return add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_CLASS_NAMES, pattern);
	}
	
	public HtmlQuery byHRef(String string) {
		return add("href", string);
	}

	public HtmlQuery byHRef(Pattern pattern) {
		return add("href", pattern);
	}
	
	public HtmlQuery bySrc(String string) {
		return add("src", string);
	}

	public HtmlQuery bySrc(Pattern pattern) {
		return add("src", pattern);
	}
	
	public HtmlQuery byTitle(String string) {
		return add("title", string);
	}

	public HtmlQuery byTitle(Pattern pattern) {
		return add("title", pattern);
	}

	public HtmlQuery byWidth(String string) {
		return add("width", string);
	}

	public HtmlQuery byWidth(Pattern pattern) {
		return add("width", pattern);
	}

	public HtmlQuery byHeight(String string) {
		return add("height", string);
	}

	public HtmlQuery byHeight(Pattern pattern) {
		return add("height", pattern);
	}

	public HtmlQuery byValue(String string) {
		return add("value", string);
	}

	public HtmlQuery byValue(Pattern pattern) {
		return add("value", pattern);
	}
	
	public HtmlQuery byType(String string) {
		return add("type", string);
	}

	public HtmlQuery byType(Pattern pattern) {
		return add("type", pattern);
	}

	public HtmlQuery byText(String string) {
		return add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TEXT, string);
	}

	public HtmlQuery byText(Pattern pattern) {
		return add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_TEXT, pattern);
	}

	public HtmlQuery byInnerHtml(String string) {
		return add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_INNER_HTML, string);
	}

	public HtmlQuery byInnerHtml(Pattern pattern) {
		return add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_INNER_HTML, pattern);
	}
	
	public HtmlQuery byHtml(String string) {
		return add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_HTML, string);
	}

	public HtmlQuery byHtml(Pattern pattern) {
		return add(HtmlSpecialAttributes.SPECIAL_ATTRIBUTE_HTML, pattern);
	}

	public String toString() {
		StringBuilder toString = new StringBuilder();
		for(QueryPart part : parts) {
			if(toString.length() > 0)
				toString.append(part.isAnd() ? "and" : "or");
			toString.append(part);
		}
		return toString.toString();
	}
	
	public static class Pattern {
		private final String value;
		private final boolean isRegularExpression;
		private boolean isCompareLowerCase = false;
		
		public Pattern(String value, boolean isRegularExpression) {
			this.value = value;
			this.isRegularExpression = isRegularExpression;
		}
		
		public String getValue() {
			return value;
		}
		
		public boolean isRegularExpression() {
			return isRegularExpression;
		}
		
		public boolean isCompareLowerCase() {
			return isCompareLowerCase;
		}
		
		public Pattern setCompareLowerCase(boolean isCompareLowerCase) {
			this.isCompareLowerCase = isCompareLowerCase;
			return this;
		}
		
		public boolean doesMatch(String string) {
			if(string != null) {
				if(isRegularExpression) 
					return isCompareLowerCase ? string.toLowerCase().matches(value) : string.matches(value);
				else 
					return isCompareLowerCase ? string.equalsIgnoreCase(value) : string.equals(value);
			}
			else
				return false;
		}
		
		public String toString() {
			if(isRegularExpression) 
				return isCompareLowerCase ? "caseInsensitive(regEx(" + value + "))" : "regEx(" + value + ")"; 
			else 
				return isCompareLowerCase ? "caseInsensitive(" + value + ")" : value;
		}
	}
}
