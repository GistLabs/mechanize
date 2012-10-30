/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.headers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implements a multi-value header.
 */
public class Header implements Iterable<String> {
	private final String name;
	private final List<String> values = new ArrayList<String>();

	public Header(final String name, final String value) {
		this.name = name;
		this.values.add(value);
	}

	public String getName() {
		return name;
	}

	public boolean isSingleValue() {
		return values.size() == 1;
	}

	public void addValue(final String value) {
		if(!values.contains(value))
			values.add(value);
	}

	public String getValue() {
		return values.get(0);
	}

	public List<String> getValues() {
		return values;
	}

	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}

	@Override
	public String toString() {
		return String.format("%s: %s", name, values);
	}
}