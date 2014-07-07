/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.node;

import java.util.List;

import com.gistlabs.mechanize.util.css_query.NodeSelector;

public abstract class AbstractNode implements Node {
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Node> T find(String query) {
		return (T) buildNodeSelector().find(query);
	}
	
	@Override
	public List<? extends Node> findAll(String query) {
		return buildNodeSelector().findAll(query);
	}

	protected NodeSelector<? extends Node> buildNodeSelector() {
		return new NodeSelector<Node>(new CssNodeHelper(this), this);
	}
	
	@Override
	public void visit(NodeVisitor visitor) {
		if(visitor.beginNode(this)) {
			for(Node child : getChildren())
				child.visit(visitor);
		}
		visitor.endNode(this);
	}
	
	@Override
	public List<? extends Node> getChildren() {
		return getChildren(new String[]{}); //TODO figure out why this arg is this way... why not just abstract this method here?
	}
}
