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
