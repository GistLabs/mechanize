/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.headers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Representation of a header list being able to use multiple String values for a single parameter.
 */
public class Headers implements Iterable<Header> {

	private final LinkedHashMap<String, Header> headers = new LinkedHashMap<String, Header>();

	public Headers() {
	}

	public Headers(final Map<String, Object> parameters) {
		List<String> sortedKeys = new ArrayList<String>(parameters.keySet());
		for(String name : sortedKeys) {
			Object value = parameters.get(name);
			if(value != null && !(value instanceof String []))
				add(name, value.toString());
			else if(value instanceof String [])
				add(name, (String [])value);
		}
	}

	/** Returns true if at least one value is present for the given header name. */
	public boolean has(final String name) {
		return this.headers.containsKey(name);
	}

	/** Returns the current values of the headers in natural sort order or null if none. */
	public String [] get(final String name) {
		if(has(name)) {
			List<String> values = headers.get(name).getValues();
			return values.toArray(new String [values.size()]);
		}
		else
			return null;
	}

	/** Returns the header names in the order they where added. */
	public String [] getNames() {
		String [] result = new String[headers.size()];
		headers.keySet().toArray(result);
		return result;
	}

	public Headers set(final String name, final String ... values) {
		remove(name);
		add(name, values);
		return this;
	}

	public Headers remove(final String name) {
		headers.remove(name);
		return this;
	}

	public Headers add(final String name, final String ... values) {
		for(String value : values)
			add(name, value);
		return this;
	}

	public Headers set(final String name, final String value) {
		remove(name);
		add(name, value);
		return this;
	}

	public Headers add(final String name, final String value) {
		if(has(name))
			headers.get(name).addValue(value);
		else
			headers.put(name, new Header(name, value));
		return this;
	}

	public Collection<Header> getHeaderss() {
		return headers.values();
	}

	@Override
	public Iterator<Header> iterator() {
		return headers.values().iterator();
	}

	/** Returns the sum of all values within the header collection. */
	public int getValueCount() {
		int count = 0;
		for (Header header : this)
			count += header.getValues().size();

		return count;
	}

	@Override
	public String toString() {
		return headers.toString();
	}
}
