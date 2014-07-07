/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.interfaces.document;

import java.util.List;


/**
 * Describes a node of a document having attributes and child nodes.
 * 
 * <p>To support additional interpreted attributes special attribute names can be used following the notation ${attributeName}.
 *    Refer to the current node implementation for supported special attributes (e.g <code>HtmlElement.getAttribute("${text}")</code>)</p>
 * 
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public interface Node {

	/** Returns the name of the node or null if none (similar to getAttribute(SpecialAttribute.SPECIAL_ATTRIBUTE_NAME)). */
	String getName();

	/** Returns the name of the node or null if none (similar to getAttribute(SpecialAttribute.SPECIAL_ATTRIBUTE_VALUE)). */
	String getValue();

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

	/** Returns the child elements. */
	List<? extends Node> getChildren();

	/** Returns the child elements. */
	List<? extends Node> getChildren(String... names);

	/** Returns the parent node or null in case of a root node. */
	Node getParent();

	/** Returns true if the attribute is set and has a value of if a special attribute (${name}) is supported. */
	boolean hasAttribute(String attributeKey);

	/** Returns the value of the attribute. */
	String getAttribute(String attributeKey);

	/** Returns all attribute names being present including any supported special attribute. */
	List<String> getAttributeNames();
}