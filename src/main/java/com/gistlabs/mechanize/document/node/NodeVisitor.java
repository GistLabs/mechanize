/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.node;

/**
 * Describes a node visitor for visiting nodes following the Visitor pattern.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public interface NodeVisitor {
	
	/** Returns true if the child node of this node should also be visited. */
	boolean beginNode(Node node);
	
	void endNode(Node node);
}
