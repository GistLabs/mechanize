/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gistlabs.mechanize.Parameters.FormHttpParameter;

/**
 * Representation of a parameter list being able to use multiple String values for a single parameter.
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Parameters implements Iterable<FormHttpParameter> {

	private final List<FormHttpParameter> formParameters = new ArrayList<FormHttpParameter>();
	private final Map<String, FormHttpParameter> parameterNames = new HashMap<String, FormHttpParameter>();
	
	public Parameters() {
	}
	
	public Parameters(Map<String, Object> parameters) {
		List<String> sortedKeys = new ArrayList<String>(parameters.keySet());
		Collections.sort(sortedKeys);
		for(String name : sortedKeys) {
			Object value = parameters.get(name);
			if(value != null && !(value instanceof String []))
				add(value.toString());
			else if(value instanceof String [])
				add(name, (String [])value);
		}
	}
	
	/** Returns true if at least one value is present for the given parameter name. */
	public boolean has(String name) {
		return get(name) != null;
	}
	
	/** Returns the current values of the parameters in natural sort order or null if none. */
	public String [] get(String name) {
		if(parameterNames.containsKey(name)) {
			List<String> values = parameterNames.get(name).getValues();
			Collections.sort(values);
			return values.toArray(new String [values.size()]);
		}
		else
			return null;
	}
	
	public Parameters set(String name, String ... values) {
		remove(name);
		add(name, values);
		return this;
	}

	public Parameters remove(String name) {
		FormHttpParameter formHttpParameter = parameterNames.get(name);
		if(formHttpParameter != null) {
			formParameters.remove(formHttpParameter);
			parameterNames.remove(name);
		}
		return this;
	}
	
	public Parameters add(String name, String ... values) {
		for(String value : values)
			add(name, value);
		return this;
	}
	
	public Parameters set(String name, String value) {
		remove(name);
		add(name, value);
		return this;
	}
	
	public Parameters add(String name, String value) {
		if(!parameterNames.containsKey(name)) {
			FormHttpParameter param = new FormHttpParameter(name, value);
			formParameters.add(param);
			parameterNames.put(name, param);
		}
		else
			parameterNames.get(name).addValue(value);
		return this;
	}
	
	public List<FormHttpParameter> getFormHParameters() {
		return formParameters;
	}
	
	public Iterator<FormHttpParameter> iterator() {
		return formParameters.iterator();
	}
	
	/** Returns the sum of all values within the parameters. */
	public int getValueCount() {
		int count = 0;
		for(FormHttpParameter parameter : this) 
			count += parameter.getValues().size();
		return count;
	}
	
	public static class FormHttpParameter {
		private final String name;
		private final List<String> values = new ArrayList<String>();
		
		public FormHttpParameter(String name, String value) {
			this.name = name;
			this.values.add(value);
		}
		
		public String getName() {
			return name;
		}
		
		public boolean isSingleValue() {
			return values.size() == 1;
		}
		
		public void addValue(String value) {
			if(!values.contains(value))
				values.add(value);
		}
		
		public String getValue() {
			return values.get(0);
		}
		
		public List<String> getValues() {
			return values;
		}
	}
}
