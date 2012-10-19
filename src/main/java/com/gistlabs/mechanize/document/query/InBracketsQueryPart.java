/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.query;

/** Query part in brackets. */
class InBracketsQueryPart extends QueryPart {
	private final AbstractQuery<?> query;
	
	public InBracketsQueryPart(boolean isAnd, AbstractQuery<?> query) {
		super(isAnd, (String [])null, null);
		this.query = query;
	}
	
	@Override
	public boolean matches(QueryStrategy queryStrategy, Object object) {
		return query.matches(queryStrategy, object);
	}
	
	@Override
	public String toString() {
		return "(" + query.toString() + ")";
	}
}