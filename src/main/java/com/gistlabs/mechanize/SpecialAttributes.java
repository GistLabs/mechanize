/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

/**
 * Defines special attributes common to different domains.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public interface SpecialAttributes {

	/**
	 * The name of the node. In the HTML / XML domain this is the tag-name of the element represented by the node. 
	 * In the JSON domain the ${name} refers to the attribute name of the object node containing this node / attribute. 
	 */
	String SPECIAL_ATTRIBUTE_NODE_NAME = "${nodeName}"; 

	/** The value of the node. In HTML / XML the value of a node is the text representation of the node without tag information. */
	String SPECIAL_ATTRIBUTE_NODE_VALUE = "${nodeValue}"; //TODO check for a chance to find a better name to differenciate between name (tag) and name attribute
}
