package com.gistlabs.mechanize.html;

import org.jsoup.nodes.Node;


/**
 * Represents a node.
 * 
 * <p>Note: The HtmlNode.toString() returns the HTML representation of this node and all its child nodes being the 
 *    exactly same as org.jsoup.nodes.Node.toString().</p>
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class HtmlNode {
	private final HtmlPage page;
	private final Node node;

	public HtmlNode(HtmlPage page, Node node) {
		this.page = page;
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}
	
	public HtmlPage getPage() {
		return page;
	}
	
	@Override
	public String toString() {
		return node.toString();
	}
}
