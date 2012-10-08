package com.gistlabs.mechanize.html;

import org.jsoup.nodes.TextNode;


/**
 * Implements a TextNode representation
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class HtmlTextNode extends HtmlNode {
	
	public HtmlTextNode(HtmlPage page, TextNode node) {
		super(page, node);
	}
	
	public TextNode getTextNode() {
		return (TextNode)getNode();
	}
}
