/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

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
	
	Query() {
		this.or = this;
	}
	
	Query add(QueryPart part) {
		parts.add(part);
		return this;
	}

	Query add(Object stringOrPattern, Selector selector) {
		Pattern pattern = stringOrPattern instanceof String ? new Pattern((String)stringOrPattern, false) : (Pattern)stringOrPattern;
		return add(new QueryPart(pattern, selector));
	}
	
	public boolean matches(Element element) {
		for(QueryPart part : parts) 
			if(part.matches(element))
				return true;
		return false;
	}
	
	public Query everything() {
		return add(new EverythingQueryPart());
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
				toString.append("or");
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
		private final Pattern pattern;
		private final Selector selector;
		
		public QueryPart(Pattern pattern, Selector selector) {
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
	}
	
	/** Query part to match every element. */
	private static class EverythingQueryPart extends QueryPart {
		public EverythingQueryPart() {
			super(null, null);
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
}
