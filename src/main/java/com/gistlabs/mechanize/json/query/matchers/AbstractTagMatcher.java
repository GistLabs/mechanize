package com.gistlabs.mechanize.json.query.matchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.Selector;
import se.fishtank.css.util.Assert;

import com.gistlabs.mechanize.json.query.Matcher;

public abstract class AbstractTagMatcher<Node> implements Matcher<Node> {
   
    /** The selector to check against. */
    protected final Selector selector;
    
    /** The set of nodes to check. */
    protected Collection<Node> nodes;
    
    /** The result of the checks. */
    protected Collection<Node> result;

    /** Whether the underlying DOM is case sensitive. */
    protected boolean caseSensitive = false;
    
    /**
     * Create a new instance.
     * 
     * @param selector The selector to check against.
     */
    public AbstractTagMatcher(Selector selector) {
        Assert.notNull(selector, "selector is null!");
        this.selector = selector;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Node> match(Collection<Node> nodes) {
        Assert.notNull(nodes, "nodes is null!");
        this.nodes = nodes;

        result = new ArrayList<Node>();
        switch (selector.getCombinator()) {
        case DESCENDANT:
            addDescendantElements();
            break;
        case CHILD:
            addChildElements();
            break;
        case ADJACENT_SIBLING:
            addAdjacentSiblingElements();
            break;
        case GENERAL_SIBLING:
            addGeneralSiblingElements();
            break;
        }
        
        return result;
    }
    
    /**
     * Add descendant elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#descendant-combinators">Descendant combinator</a>
     * 
     * @throws NodeSelectorException If one of the nodes have an illegal type.
     */
    private void addDescendantElements() {
        for (Node node : nodes) {
        	List<Node> nodes = new ArrayList<Node>();
        	nodes.add(node);
        	nodes.addAll(getDescendentNodes(node));
        	
        	for(Node n : nodes) {
        		if (matchTag(n))
        			result.add(n);
        	}
        }
    }

	/**
     * Add child elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#child-combinators">Child combinators</a>
     */
    private void addChildElements() {
        for (Node node : nodes) {
        	Collection<? extends Node> childNodes = getChildNodes(node);
        	for (Node child : childNodes) {
                if (matchTag(child))
                	result.add(child);	
			}
        }
    }

	private boolean matchTag(Node child) {
		String tag = selector.getTagName();
		return tagEquals(tag, getName(child)) || tag.equals(Selector.UNIVERSAL_TAG);
	}

	/**
     * Add adjacent sibling elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#adjacent-sibling-combinators">Adjacent sibling combinator</a>
     */
    private void addAdjacentSiblingElements() {
        for (Node node : nodes) {
            Node n = getNextSibling(node);
            if (n != null) {
                if (matchTag(n))
                	result.add(n);	
            }
        }
    }
    
    protected abstract Collection<? extends Node> getDescendentNodes(Node node);
    
    protected abstract Collection<? extends Node> getChildNodes(Node node);

	protected abstract String getName(Node n);
    
    protected abstract Node getNextSibling(Node node);

	/**
     * Add general sibling elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#general-sibling-combinators">General sibling combinator</a>
     */
    private void addGeneralSiblingElements() {
        for (Node node : nodes) {
            Node n = getNextSibling(node);
            while (n != null) {
                String tag = selector.getTagName();
                if (tagEquals(tag, getName(n)) || tag.equals(Selector.UNIVERSAL_TAG)) {
                    result.add(n);
                }
                
                n = getNextSibling(n);
            }
        }
    }

    /**
     * Determine if the two specified tag names are equal.
     *
     * @param tag1 A tag name.
     * @param tag2 A tag name.
     * @return <code>true</code> if the tag names are equal, <code>false</code> otherwise.
     */
    private boolean tagEquals(String tag1, String tag2) {
        if (caseSensitive) {
            return tag1.equals(tag2);
        }

        return tag1.equalsIgnoreCase(tag2);
    }

}
