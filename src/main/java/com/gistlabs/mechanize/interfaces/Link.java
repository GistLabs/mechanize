/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
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
	 * The uri representation for this link (this won't be resolved or template substituted)
	 */
	public String uri();

	/**
	 * Follows the link (using the Resource and Mechanize objects it came from)
	 */
	public <T extends Resource> T follow();

	/**
	 * Set values for a template link.
	 * @param name
	 * @param value
	 */
	public void set(String name, Object value);

	/**
	 * Set multiple values for a template link
	 * @param values
	 */
	public void setAll(Map<String, Object> values);
}
