/*
 * Copyright (C) 2011 Gist Labs, LLC.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.gistlabs.mechanize.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gistlabs.mechanize.form.FormParams.FormHttpParameter;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class FormParams implements Iterable<FormHttpParameter> {

	private final List<FormHttpParameter> formParameters = new ArrayList<FormHttpParameter>();
	private final Map<String, FormHttpParameter> parameterNames = new HashMap<String, FormHttpParameter>();
	
	public void setFormParameter(String name, String value) {
		if(!parameterNames.containsKey(name)) {
			FormHttpParameter param = new FormHttpParameter(name, value);
			formParameters.add(param);
			parameterNames.put(name, param);
		}
		else
			parameterNames.get(name).addValue(value);
	}
	
	public List<FormHttpParameter> getFormParameters() {
		return formParameters;
	}
	
	public Iterator<FormHttpParameter> iterator() {
		return formParameters.iterator();
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
