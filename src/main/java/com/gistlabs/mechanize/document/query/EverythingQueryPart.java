/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.query;

/** Query part to match every element. */
class EverythingQueryPart extends QueryPart {
	public EverythingQueryPart(boolean isAnd) {
		super(isAnd, (String [])null, null);
	}
	
	@Override
	public boolean matches(QueryStrategy queryStrategy, Object object) {
		return true;
	}
	
	@Override
	public String toString() {
		return "<everything>";
	}
}