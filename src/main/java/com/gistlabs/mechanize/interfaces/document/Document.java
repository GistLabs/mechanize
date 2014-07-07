/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.interfaces.document;

import java.util.List;

import com.gistlabs.mechanize.interfaces.Resource;


/**
 * Represents a single or multiple-page document having a root node.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public interface Document extends Resource {

	/**
	 * Returns the root node of the document
	 * @return
	 */
	public Node getRoot();

	/**
	 *  Query for a matching link, find first match by either id or by class attributes.
	 * 
	 * @param query wrapped with byIdOrClass()
	 * @return first Link found
	 */
	public NodeLink link(final String query);

	public List<NodeLink> links();


	/**
	 * Returns the only child node matching the CSS Selector search query
	 * See http://www.w3.org/TR/css3-selectors/ for syntax
	 * @param query
	 * @return matching single Node, null, or exception if multiple
	 */
	public <T extends Node> T find(String query);

	/**
	 * Return the matching nodes for the CSS Selector search query
	 * See http://www.w3.org/TR/css3-selectors/ for syntax

	 * @param query
	 * @return a non-null list of matching nodes, may be empty
	 */
	public List<? extends Node> findAll(String query);

}
