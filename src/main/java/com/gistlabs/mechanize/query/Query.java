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

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Query {
	
	private final List<QueryPart> parts = new ArrayList<QueryPart>(); 
	public final Query or;
	public final Query and;
	
	Query() {
		this.or = this;
		this.and = new AndQuery(this);
	}
	
	Query(Query parent) {
		this.or = parent.or;
		this.and = this;
	}
	
	Query add(QueryPart part) {
		if(this instanceof AndQuery) {
			((AndQuery)this).parent.parts.add(part);
			return ((AndQuery)this).parent;
		}
		else {
			this.parts.add(part);
			return this;
		}
	}
	
	private static class AndQuery extends Query {
		private final Query parent; 
		public AndQuery(Query parentQuery) {
			super(parentQuery);
			this.parent = parentQuery;
		}
		
		@Override
		public String toString() {
			return parent.toString();
		}
	}

	Query add(Object stringOrPattern, Selector selector) {
		Pattern pattern = stringOrPattern instanceof String ? new Pattern((String)stringOrPattern, false) : (Pattern)stringOrPattern;
		return this.add(new QueryPart(this instanceof AndQuery, pattern, selector));
	}
	
	public boolean matches(Element element) {
		if(this instanceof AndQuery) {
			return ((AndQuery)this).parent.matches(element);
		}
		else {
			boolean last = false;
			for(QueryPart part : parts) {
				if(part.isAnd() && last == true) {
					boolean current = part.matches(element);
					last = current;
				}
				else if(part.isAnd() && last == false) {
					last = false;
				}
				else if(!part.isAnd() && last == true) {
					return true;
				}
				else {
					last = part.matches(element);
				}
			}
			return last;
		}
	}
	
	public Query everything() {
		return add(new EverythingQueryPart(this instanceof AndQuery));
	}
	
	public Query inBrackets(Query query) {
		return add(new InBracketsQueryPart(this instanceof AndQuery, query));
	}

	public Query not(Query query) {
		return add(new NotQueryPart(this instanceof AndQuery, query));
	}

	public Query byAny(String string) {
		return add(string, Selector.ANY);
	}

	public Query byName(String string) {
		return add(string, Selector.NAME);
	}
	
	public Query byId(String string) {
		return add(string, Selector.ID);
	}

	public Query byIdOrClass(String string) {
		return add(string, Selector.ID_OR_CLASZ);
	}

	public Query byIdOrClassOrName(String string) {
		return add(string, Selector.ID_OR_CLASZ_OR_NAME);
	}

	public Query byNameOrId(String string) {
		return add(string, Selector.NAME_OR_ID);
	}

	public Query byTag(String string) {
		return add(string, Selector.TAG);
	}

	public Query byClass(String string) {
		return add(string, Selector.CLASZ);
	}
	
	public Query byHRef(String string) {
		return add(string, Selector.HREF);
	}
	
	public Query bySrc(String string) {
		return add(string, Selector.SRC);
	}
	
	public Query byTitle(String string) {
		return add(string, Selector.TITLE);
	}

	public Query byWidth(String string) {
		return add(string, Selector.WIDTH);
	}

	public Query byHeight(String string) {
		return add(string, Selector.HEIGHT);
	}

	public Query byValue(String string) {
		return add(string, Selector.VALUE);
	}
	
	public Query byType(String string) {
		return add(string, Selector.TYPE);
	}

	public Query byText(String string) {
		return add(string, Selector.TEXT);
	}

	public Query byInnerHtml(String string) {
		return add(string, Selector.INNER_HTML);
	}

	public Query byHtml(String string) {
		return add(string, Selector.HTML);
	}

	public Query byAny(Pattern pattern) {
		return add(pattern, Selector.ANY);
	}

	public Query byName(Pattern pattern) {
		return add(pattern, Selector.NAME);
	}
	
	public Query byId(Pattern pattern) {
		return add(pattern, Selector.ID);
	}

	public Query byNameOrId(Pattern pattern) {
		return add(pattern, Selector.NAME_OR_ID);
	}

	public Query byTag(Pattern pattern) {
		return add(pattern, Selector.TAG);
	}

	public Query byClass(Pattern pattern) {
		return add(pattern, Selector.CLASZ);
	}
	
	public Query byHRef(Pattern pattern) {
		return add(pattern, Selector.HREF);
	}
	
	public Query bySrc(Pattern pattern) {
		return add(pattern, Selector.SRC);
	}
	
	public Query byTitle(Pattern pattern) {
		return add(pattern, Selector.TITLE);
	}

	public Query byWidth(Pattern pattern) {
		return add(pattern, Selector.WIDTH);
	}

	public Query byHeight(Pattern pattern) {
		return add(pattern, Selector.HEIGHT);
	}

	public Query byValue(Pattern pattern) {
		return add(pattern, Selector.VALUE);
	}

	public Query byType(Pattern pattern) {
		return add(pattern, Selector.TYPE);
	}

	public Query byText(Pattern pattern) {
		return add(pattern, Selector.TEXT);
	}
	
	public Query byInnerHtml(Pattern pattern) {
		return add(pattern, Selector.INNER_HTML);
	}

	public Query byHtml(Pattern pattern) {
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
	
	private static class QueryPart {
		private final boolean isAnd;
		private final Pattern pattern;
		private final Selector selector;
		
		public QueryPart(boolean isAnd, Pattern pattern, Selector selector) {
			this.isAnd = isAnd;
			this.pattern = pattern;
			this.selector = selector;
		}
		
		@Override
		public String toString() {
			return "<" + pattern.toString() + "," + selector.toString() + ">";
		}
		
		public boolean matches(Element element) {
			boolean isMatch = false;

			if(selector.includesAny()) {
				for(Attribute attribute : element.attributes())
					if(pattern.doesMatch(attribute.getValue()))
						isMatch = true;
			}

			if(selector.includesClass() || selector.includesAny())
				for(String className : element.classNames())
					if(pattern.doesMatch(className))
						isMatch = true;
			
			if(!isMatch && selector.includesId())
				isMatch = pattern.doesMatch(element.id());
			
			if(!isMatch && selector.includesName()) 
				isMatch = pattern.doesMatch(element.attr("name"));

			if(!isMatch && selector.includesTag()) 
				isMatch = pattern.doesMatch(element.tag().getName());
			
			if(!isMatch && selector.includesAction()) 
				isMatch = pattern.doesMatch(element.attr("action"));
			
			if(!isMatch && selector.includesHRef()) 
				isMatch = pattern.doesMatch(element.attr("href"));
			
			if(!isMatch && selector.includesSrc()) 
				isMatch = pattern.doesMatch(element.attr("src"));

			if(!isMatch && selector.includesTitle()) 
				isMatch = pattern.doesMatch(element.attr("title"));
			
			if(!isMatch && selector.includesWidth()) 
				isMatch = pattern.doesMatch(element.attr("width"));

			if(!isMatch && selector.includesHeight()) 
				isMatch = pattern.doesMatch(element.attr("height"));

			if(!isMatch && selector.includesValue()) 
				isMatch = pattern.doesMatch(element.attr("value"));
			
			if(!isMatch && selector.includesType()) 
				isMatch = pattern.doesMatch(element.attr("type"));

			if(!isMatch && selector.includesText()) 
				isMatch = pattern.doesMatch(element.text());

			if(!isMatch && selector.includesInnerHtml()) 
				isMatch = pattern.doesMatch(element.html());

			if(!isMatch && selector.includesHtml()) 
				isMatch = pattern.doesMatch(element.outerHtml());
			
			return isMatch; 
		}
		
		public boolean isAnd() {
			return isAnd;
		}
	}
	
	/** Query part to match every element. */
	private static class EverythingQueryPart extends QueryPart {
		public EverythingQueryPart(boolean isAnd) {
			super(isAnd, null, null);
		}
		
		@Override
		public boolean matches(Element element) {
			return true;
		}
		
		@Override
		public String toString() {
			return "<everything>";
		}
	}

	/** Query part in brackets. */
	private static class InBracketsQueryPart extends QueryPart {
		private final Query query;
		
		public InBracketsQueryPart(boolean isAnd, Query query) {
			super(isAnd, null, null);
			this.query = query;
		}
		
		@Override
		public boolean matches(Element element) {
			return query.matches(element);
		}
		
		@Override
		public String toString() {
			return "(" + query.toString() + ")";
		}
	}

	/** Query part being a negation. */
	private static class NotQueryPart extends QueryPart {
		private final Query query;
		
		public NotQueryPart(boolean isAnd, Query query) {
			super(isAnd, null, null);
			this.query = query;
		}
		
		@Override
		public boolean matches(Element element) {
			return !query.matches(element);
		}
		
		@Override
		public String toString() {
			return "<not" + query.toString() + ">";
		}
	}
}
