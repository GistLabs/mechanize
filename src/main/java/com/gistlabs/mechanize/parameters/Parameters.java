/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Representation of a parameter list being able to use multiple String values for a single parameter.
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @author John Heintz <john@gistlabs.com>
 */
public class Parameters implements Iterable<Parameter> {

	private final LinkedHashMap<String, Parameter> parameters = new LinkedHashMap<String, Parameter>();
	//	private final List<Parameter> formParameters = new ArrayList<Parameter>();
	//	private final Map<String, Parameter> parameterNames = new HashMap<String, Parameter>();

	public Parameters() {
	}

	public Parameters(final Map<String, Object> parameters) {
		List<String> sortedKeys = new ArrayList<String>(parameters.keySet());
		Collections.sort(sortedKeys);
		for(String name : sortedKeys) {
			Object value = parameters.get(name);
			if(value != null && !(value instanceof String []))
				add(name, value.toString());
			else if(value instanceof String [])
				add(name, (String [])value);
		}
	}

	/** Returns true if at least one value is present for the given parameter name. */
	public boolean has(final String name) {
		return this.parameters.containsKey(name);
	}

	/** Returns the current values of the parameters in natural sort order or null if none. */
	public String [] get(final String name) {
		if(has(name)) {
			List<String> values = parameters.get(name).getValues();
			Collections.sort(values);
			return values.toArray(new String [values.size()]);
		}
		else
			return null;
	}

	/** Returns the parameter names in the order they where added. */
	public String [] getNames() {
		String [] result = new String[parameters.size()];
		parameters.keySet().toArray(result);
		return result;
	}

	public Parameters set(final String name, final String ... values) {
		remove(name);
		add(name, values);
		return this;
	}

	public Parameters remove(final String name) {
		parameters.remove(name);
		return this;
	}

	public Parameters add(final String name, final String ... values) {
		for(String value : values)
			add(name, value);
		return this;
	}

	public Parameters set(final String name, final String value) {
		remove(name);
		add(name, value);
		return this;
	}

	public Parameters add(final String name, final String value) {
		if(has(name))
			parameters.get(name).addValue(value);
		else
			parameters.put(name, new Parameter(name, value));
		return this;
	}

	public Collection<Parameter> getParameters() {
		return parameters.values();
	}

	@Override
	public Iterator<Parameter> iterator() {
		return parameters.values().iterator();
	}

	/** Returns the sum of all values within the parameters. */
	public int getValueCount() {
		int count = 0;
		for (Parameter parameter : this)
			count += parameter.getValues().size();

		return count;
	}

	@Override
	public String toString() {
		return parameters.toString();
	}
}
