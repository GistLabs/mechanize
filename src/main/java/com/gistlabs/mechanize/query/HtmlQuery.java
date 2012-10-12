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

	HtmlQuery add(Object stringOrPattern, Selector selector) {
		Pattern pattern = stringOrPattern instanceof String ? new Pattern((String)stringOrPattern, false) : (Pattern)stringOrPattern;
		return this.add(new QueryPart(this instanceof AndQuery, pattern, selector));
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

	public HtmlQuery byAny(String string) {
		return add(string, Selector.ANY);
	}

	public HtmlQuery byName(String string) {
		return add(string, Selector.NAME);
	}
	
	public HtmlQuery byId(String string) {
		return add(string, Selector.ID);
	}

	public HtmlQuery byIdOrClass(String string) {
		return add(string, Selector.ID_OR_CLASZ);
	}

	public HtmlQuery byIdOrClassOrName(String string) {
		return add(string, Selector.ID_OR_CLASZ_OR_NAME);
	}

	public HtmlQuery byNameOrId(String string) {
		return add(string, Selector.NAME_OR_ID);
	}

	public HtmlQuery byTag(String string) {
		return add(string, Selector.TAG);
	}

	public HtmlQuery byClass(String string) {
		return add(string, Selector.CLASZ);
	}
	
	public HtmlQuery byHRef(String string) {
		return add(string, Selector.HREF);
	}
	
	public HtmlQuery bySrc(String string) {
		return add(string, Selector.SRC);
	}
	
	public HtmlQuery byTitle(String string) {
		return add(string, Selector.TITLE);
	}

	public HtmlQuery byWidth(String string) {
		return add(string, Selector.WIDTH);
	}

	public HtmlQuery byHeight(String string) {
		return add(string, Selector.HEIGHT);
	}

	public HtmlQuery byValue(String string) {
		return add(string, Selector.VALUE);
	}
	
	public HtmlQuery byType(String string) {
		return add(string, Selector.TYPE);
	}

	public HtmlQuery byText(String string) {
		return add(string, Selector.TEXT);
	}

	public HtmlQuery byInnerHtml(String string) {
		return add(string, Selector.INNER_HTML);
	}

	public HtmlQuery byHtml(String string) {
		return add(string, Selector.HTML);
	}

	public HtmlQuery byAny(Pattern pattern) {
		return add(pattern, Selector.ANY);
	}

	public HtmlQuery byName(Pattern pattern) {
		return add(pattern, Selector.NAME);
	}
	
	public HtmlQuery byId(Pattern pattern) {
		return add(pattern, Selector.ID);
	}

	public HtmlQuery byNameOrId(Pattern pattern) {
		return add(pattern, Selector.NAME_OR_ID);
	}

	public HtmlQuery byTag(Pattern pattern) {
		return add(pattern, Selector.TAG);
	}

	public HtmlQuery byClass(Pattern pattern) {
		return add(pattern, Selector.CLASZ);
	}
	
	public HtmlQuery byHRef(Pattern pattern) {
		return add(pattern, Selector.HREF);
	}
	
	public HtmlQuery bySrc(Pattern pattern) {
		return add(pattern, Selector.SRC);
	}
	
	public HtmlQuery byTitle(Pattern pattern) {
		return add(pattern, Selector.TITLE);
	}

	public HtmlQuery byWidth(Pattern pattern) {
		return add(pattern, Selector.WIDTH);
	}

	public HtmlQuery byHeight(Pattern pattern) {
		return add(pattern, Selector.HEIGHT);
	}

	public HtmlQuery byValue(Pattern pattern) {
		return add(pattern, Selector.VALUE);
	}

	public HtmlQuery byType(Pattern pattern) {
		return add(pattern, Selector.TYPE);
	}

	public HtmlQuery byText(Pattern pattern) {
		return add(pattern, Selector.TEXT);
	}
	
	public HtmlQuery byInnerHtml(Pattern pattern) {
		return add(pattern, Selector.INNER_HTML);
	}

	public HtmlQuery byHtml(Pattern pattern) {
		return add(pattern, Selector.HTML);
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
