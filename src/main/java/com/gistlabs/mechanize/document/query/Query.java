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
public class Query extends AbstractQuery<Query> {

	public final Query or;
	public final Query and;

	Query() {
		this.or = this;
		this.and = new MyAndQuery(this);
	}
	
	Query(Query parent) {
		this.or = parent.or;
		this.and = this;
	}
	
	private static class MyAndQuery extends Query implements AndQuery<Query> {
		private final Query parent; 
		public MyAndQuery(Query parentQuery) {
			super(parentQuery);
			this.parent = parentQuery;
		}

		public Query getParent() {
			return parent;
		}
		
		@Override
		public String toString() {
			return parent.toString();
		}
	}
}