package com.gistlabs.mechanize.document.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import se.fishtank.css.selectors.Selector;
import se.fishtank.css.util.Assert;

import com.gistlabs.mechanize.util.css_query.NodeHelper;
import com.gistlabs.mechanize.util.css_query.NodeSelector;

public class JsoupNodeHelper implements NodeHelper<Node> {

	public static Node find(final Node node, final String selector) {
		return new NodeSelector<Node>(new JsoupNodeHelper(node), node).find(selector);
	}

	public static List<Node> findAll(final Node node, final String selector) {
		return new NodeSelector<Node>(new JsoupNodeHelper(node), node).findAll(selector);
	}


	/** The root node (document or element). */
	private final Node root;

	/**
	 * Create a new instance.
	 * 
	 * @param root The root node. Must be a document or element node.
	 */
	public JsoupNodeHelper(final Node root) {
		Assert.notNull(root, "root is null!");
		Assert.isTrue(root instanceof Document || root instanceof Element, "root must be a document or element node!");
		this.root = root;
	}

	@Override
	public String getValue(final Node node) {
		if (node instanceof Element)
			return ((Element)node).text();
		else
			return null;
	}

	@Override
	public boolean hasAttribute(final Node node, final String name) {
		return node.attributes().hasKey(name);
	}

	@Override
	public String getAttribute(final Node node, final String name) {
		return node.attributes().get(name).trim();
	}

	@Override
	public Index getIndexInParent(final Node node, final boolean byType) {
		String type = byType ? getName(node) : Selector.UNIVERSAL_TAG;

		List<? extends Node> children;
		Node parent = node.parent();
		if (parent==null)
			children = Collections.emptyList();
		else
			children = getChildNodes(parent, type);

		return new Index(children.indexOf(node), children.size());
	}

	@Override
	public Collection<? extends Node> getDescendentNodes(final Node node) {
		Elements descendents;
		if (node instanceof Document)
			descendents = ((Document)node).getAllElements();
		else
			descendents = ((Element)node).getAllElements();

		descendents.remove(node); // Jsoup includes the target of getAllElements() in the result...
		return descendents;
	}

	@Override
	public List<? extends Node> getChildNodes(final Node node) {
		return getChildNodes(node, "*");
	}

	@SuppressWarnings("unchecked")
	protected List<? extends Node> getChildNodes(final Node node, final String withName) {
		if (node==null)
			return Collections.EMPTY_LIST;

		if (Selector.UNIVERSAL_TAG.equals(withName))
			return filter(node.childNodes());

		ArrayList<Node> result = new ArrayList<Node>();

		List<Node> children = node.childNodes();
		for(Node child : children)
			if (nameMatches(child, withName))
				result.add(child);

		return result;
	}

	private List<? extends Node> filter(final List<Node> nodes) {
		ArrayList<Node> result = new ArrayList<Node>();
		for(Node node : nodes)
			if (node instanceof Element)
				result.add(node);
		return result;
	}

	@Override
	public boolean isEmpty(final Node node) {
		return node.childNodes().isEmpty();
	}

	@Override
	public String getName(final Node n) {
		return n.nodeName();
	}

	@Override
	public Node getNextSibling(final Node node) {
		Index index = getIndexInParent(node, false);
		if (index.index < (index.size-1))
			return getChildNodes(node.parent()).get(index.index+1);
		else
			return null;
	}

	@Override
	public boolean nameMatches(final Node n, final String name) {
		if (name.equals(Selector.UNIVERSAL_TAG))
			return true;

		return name.equalsIgnoreCase(getName(n));
	}

	@Override
	public Node getRoot() {
		if (root instanceof Document) {
			// Get the single element child of the document node.
			// There could be a doctype node and comment nodes that we must skip.
			List<Node> children = root.childNodes();
			for(Node child : children)
				if (child instanceof Element)
					return child;
			Assert.isTrue(false, "there should be a root element!");
			return null;
		} else {
			Assert.isTrue(root instanceof Element, "root must be a document or element node!");
			return root;
		}
	}
}
