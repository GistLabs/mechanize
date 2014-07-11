/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.interfaces;

import java.util.Map;

/**
 * Represents a named hyperlink.
 * 
 * Links can be follow()ed, and some links are parameterized (HTML Forms and URITemplates)
 */
public interface Link {

	/**
	 * Returns the Resource this Link came from
	 */
	public <T extends Resource> T resource();

	/**
	 * The identifying name for this link.
	 */
	public String name();

	/**
	 * The uri representation for this link (after resoloving and substituting tempaltes)
	 */
	public String uri();

	/**
	 * The uri representation for this link (this won't be resolved or template substituted)
	 */
	public String raw();

	/**
	 * Return list of String template variable names, if uri template 
	 */
	public String[] getVariables();
	
	/**
	 * Follows the link (using the Resource and Mechanize objects it came from) resolving URITemplates.
	 */
	public <T extends Resource> T follow();

	/**
	 * Set value for a template link. Can be Object, List<String> and Map<String,String>, objects need to have .toString() method.
	 * @param name
	 * @param value
	 */
	public void set(String name, Object value);

	/**
	 * Set multiple values for a template link. Values in Map can be List<Object> and Map<Object>, objects need to have .toString() method.
	 * @param values
	 */
	public void setAll(Map<String, Object> values);
}
