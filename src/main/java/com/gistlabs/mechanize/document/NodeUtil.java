package com.gistlabs.mechanize.document;


public class NodeUtil {

	public static <T extends Node> void visit(T node, NodeVisitor visitor) {
		if(node==null)
			return;
		
		if (visitor.beginNode(node))
			for(Node child : node.getChildren())
				visit(child, visitor);
		visitor.endNode(node);
	}
}
