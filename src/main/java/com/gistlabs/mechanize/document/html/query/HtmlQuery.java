/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.query;


import static com.gistlabs.mechanize.document.query.AbstractQueryBuilder.*;

import com.gistlabs.mechanize.document.html.HtmlSpecialAttributes;
import com.gistlabs.mechanize.document.query.AbstractQuery;
import com.gistlabs.mechanize.document.query.Pattern;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class HtmlQuery extends AbstractQuery<HtmlQuery> {
	
	public final HtmlQuery or;
	public final HtmlQuery and;
	
	HtmlQuery() {
		this.or = this;
		this.and = new MyAndQuery(this);
	}
	
	HtmlQuery(HtmlQuery parent) {
		this.or = parent.or;
		this.and = this;
	}
	
	private static class MyAndQuery extends HtmlQuery implements AndQuery<HtmlQuery> {
		private final HtmlQuery parent; 
		
		public MyAndQuery(HtmlQuery parentQuery) {
			super(parentQuery);
			this.parent = parentQuery;
		}
		
		@Override
		public HtmlQuery getParent() {
			return parent;
		}
		
		@Override
		public String toString() {
			return parent.toString();
		}
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
}
