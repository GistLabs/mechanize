/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html;


/**
 * Defines supported special attributes for html elements.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public interface HtmlSpecialAttributes {

	/**
	 * The name of the node. In the HTML / XML domain this is the tag-name of the element represented by the node. 
	 * In the JSON domain the ${name} refers to the attribute name of the object node containing this node / attribute. 
	 */
	String SPECIAL_ATTRIBUTE_NODE_NAME = "${nodeName}"; 

	/** The value of the node. In HTML / XML the value of a node is the text representation of the node without tag information. */
	String SPECIAL_ATTRIBUTE_NODE_VALUE = "${nodeValue}"; //TODO check for a chance to find a better name to differenciate between name (tag) and name attribute

	/** A comma separated list of class names without white spaces (no trim needed). */
	String SPECIAL_ATTRIBUTE_CLASS_NAMES = "${classNames}";
	String SPECIAL_ATTRIBUTE_TAG_NAME = SPECIAL_ATTRIBUTE_NODE_NAME;
	String SPECIAL_ATTRIBUTE_INNER_HTML = "${innerHtml}";
	String SPECIAL_ATTRIBUTE_HTML = "${html}";
	String SPECIAL_ATTRIBUTE_TEXT = SPECIAL_ATTRIBUTE_NODE_VALUE;
}
