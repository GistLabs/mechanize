package com.gistlabs.mechanize.json.query.matchers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import se.fishtank.css.selectors.specifier.PseudoClassSpecifier;

import com.gistlabs.mechanize.json.Node;
import com.gistlabs.mechanize.json.query.Matcher;

public class PseudoClassSpecifierMatcher extends AbstractPseudoClassSpecifierMatcher<Node> implements Matcher<Node> {
    /**
     * Create a new instance.
     * 
     * @param specifier The pseudo-class specifier to check against.
     */
    public PseudoClassSpecifierMatcher(PseudoClassSpecifier specifier) {
    	super(specifier);
    }

    protected Collection<? extends Node> getChildNodes(Node node) {
		return node.getChildren();
	}

	@SuppressWarnings("unchecked")
	protected Index getIndexInParent(Node node, boolean byType) {
		String type = byType ? node.getName() : "*";
		
		List<Node> children;
		Node parent = node.getParent();
		if (parent==null)
			children = Collections.EMPTY_LIST;
		else
			children = parent.getChildren(type);

		return new Index(children.indexOf(node), children.size());
	}

	protected Node getRoot(Node node) {
		Node root = node;
		while (root.getParent()!=null)
			root = root.getParent();
		return root;
	}
}
