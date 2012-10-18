/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jsoup.nodes.Element;

import com.gistlabs.mechanize.document.node.AbstractNode;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.document.query.AbstractQuery;
import com.gistlabs.mechanize.util.Util;
import com.gistlabs.mechanize.util.css_query.NodeSelector;

/**
 * Represents a node.
 * 
 * <p>Note: The HtmlNode.toString() returns the HTML representation of this node and all its child nodes being the
 *    exactly same as org.jsoup.nodes.Node.toString().</p>
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class HtmlNode extends AbstractNode {

	private final HtmlPage page;
	private final org.jsoup.nodes.Node node;

	public HtmlNode(final HtmlPage page, final org.jsoup.nodes.Node node) {
		this.page = page;
		this.node = node;
	}

	public org.jsoup.nodes.Node getJsoupNode() {
		return node;
	}

	public HtmlPage getPage() {
		return page;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getValue() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Node> T find(final String query) {
		return (T) getPage().htmlElements().getHtmlNode(JsoupNodeHelper.find(node, query));
	}

	@Override
	public List<? extends Node> findAll(final String query) {
		return convert(JsoupNodeHelper.findAll(node, query));
	}

	@Override
	protected NodeSelector<? extends Node> buildNodeSelector() {
		throw new UnsupportedOperationException("must reimplment this");
	}

	@Override
	public boolean hasAttribute(final String attributeKey) {
		return false;
	}

	@Override
	public String getAttribute(final String attributeKey) {
		return null;
	}

	@Override
	public List<String> getAttributeNames() {
		return Util.newEmptyList();
	}

	@Override
	public List<HtmlNode> getChildren() {
		List<org.jsoup.nodes.Node> nodes = node.childNodes();
		return convert(nodes);
	}

	protected List<HtmlNode> convert(final List<org.jsoup.nodes.Node> nodes) {
		List<HtmlNode> result = new ArrayList<HtmlNode>();

		for(org.jsoup.nodes.Node child : nodes)
			result.add(getPage().htmlElements().getHtmlNode(child));

		return result;
	}

	@Override
	public List<HtmlNode> getChildren(final String... names) {
		Collection<String> namesColl = Arrays.asList(names);
		boolean emptyOrStar = namesColl.isEmpty() || namesColl.contains("*");

		List<HtmlNode> result = new ArrayList<HtmlNode>();

		for(org.jsoup.nodes.Node child : node.childNodes())
			if (emptyOrStar || namesColl.contains(maybeElementTag(node)))
				result.add(getPage().htmlElements().getHtmlNode(child));

		return result;
	}

	@Override
	public HtmlNode getParent() {
		org.jsoup.nodes.Node parent = getJsoupNode().parent();
		return parent != null ? getPage().htmlElements().getHtmlNode(parent) : null;
	}

	protected String maybeElementTag(final org.jsoup.nodes.Node node) {
		if (node instanceof Element)
			return ((Element)node).tagName();
		else
			return null;
	}

	@Override
	public HtmlElement get(final AbstractQuery<?> query) {
		return HtmlElements.get(getPage(), query, node.childNodes());
	}

	@Override
	public List<HtmlElement> getAll(final AbstractQuery<?> query) {
		List<HtmlElement> result = new ArrayList<HtmlElement>();
		HtmlElements.getAll(getPage(), result, query, node.childNodes());
		return result;
	}

	@Override
	public String toString() {
		return node.toString();
	}
}
