/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.query;

import static com.gistlabs.mechanize.document.query.AbstractQueryBuilder.*;

import java.util.ArrayList;
import java.util.List;

import com.gistlabs.mechanize.SpecialAttributes;

public class AbstractQuery<T extends AbstractQuery<?>> {

	protected final List<QueryPart> parts = new ArrayList<QueryPart>();

	protected static interface AndQuery<T extends AbstractQuery<?>> {
		T getParent();
	}
	
	@SuppressWarnings("unchecked")
	protected T add(QueryPart part) {
		if(this instanceof AndQuery) {
			((AndQuery<T>)this).getParent().parts.add(part);
			return ((AndQuery<T>)this).getParent();
		}
		else {
			this.parts.add(part);
			return (T)this;
		}
	}

	protected T add(String attributeName, Object stringOrPattern) {
		return add(attributes(attributeName), stringOrPattern);
	}

	protected T add(String [] attributeNames, Object stringOrPattern) {
		Pattern pattern = stringOrPattern instanceof String ? new Pattern((String)stringOrPattern, false) : (Pattern)stringOrPattern;
		return this.add(new QueryPart(this instanceof AndQuery, attributeNames, pattern));
	}

	@SuppressWarnings("unchecked")
	public boolean matches(QueryStrategy queryStrategy, Object object) {
		if(this instanceof AndQuery) {
			return ((AndQuery<T>)this).getParent().matches(queryStrategy, object);
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

	public T everything() {
		return add(new EverythingQueryPart(this instanceof AndQuery));
	}

	public T inBrackets(T query) {
		return add(new InBracketsQueryPart(this instanceof AndQuery, query));
	}

	public T not(T query) {
		return add(new NotQueryPart(this instanceof AndQuery, query));
	}

	public T by(String attribute, String value) {
		return add(attribute, value);
	}

	public T by(String attribute, Pattern pattern) {
		return add(attribute, pattern);
	}

	public T by(String [] attributeNames, String value) {
		return add(attributeNames, value);
	}

	public T by(String [] attributeNames, Pattern value) {
		return add(attributeNames, value);
	}

	public T byAny(String string) {
		return add("*", string);
	}

	public T byAny(Pattern pattern) {
		return add("*", pattern);
	}
	
	public T byNodeName(String string) {
		return add(SpecialAttributes.SPECIAL_ATTRIBUTE_NODE_NAME, string);
	}

	public T byNodeName(Pattern pattern) {
		return add(SpecialAttributes.SPECIAL_ATTRIBUTE_NODE_NAME, pattern);
	}

	public T byNodeValue(String string) {
		return add(SpecialAttributes.SPECIAL_ATTRIBUTE_NODE_VALUE, string);
	}
	
	public T byNodeValue(Pattern pattern) {
		return add(SpecialAttributes.SPECIAL_ATTRIBUTE_NODE_VALUE, pattern);
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
}