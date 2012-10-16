/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gistlabs.mechanize.Resource;
//import com.gistlabs.mechanize.html.query.HtmlQueryStrategy;
import com.gistlabs.mechanize.document.query.AbstractQuery;
import com.gistlabs.mechanize.document.query.QueryStrategy;
import com.gistlabs.mechanize.exceptions.MechanizeException;
import com.gistlabs.mechanize.util.Assert;

/**
 * Abstract implementation stub for page elements implementing dynamic caching of representations of DOM elements. 
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public abstract class PageElements<T> implements Iterable<T> {
	protected final Resource page;
	protected final List<? extends Node> nodes;
	protected final QueryStrategy queryStrategy;
	
	private final Map<Node, T> representations = new HashMap<Node, T>();
	
	public PageElements(Resource page, List<? extends Node> nodes, QueryStrategy queryStrategy) {
		Assert.notNull(nodes, "Nodes may not be null");
		
		this.page = page;
		this.nodes = nodes;
		this.queryStrategy = queryStrategy;
	}
	
	public Resource getPage() {
		return page;
	}
	
	protected T getCachedOrNewRepresentation(Node node) {
		if(node != null) {
			if(!representations.containsKey(node))
				representations.put(node, newRepresentation(node));
			
			return representations.get(node);
		}
		else
			return null;
	}

	protected abstract T newRepresentation(Node element);
	
	public T get(AbstractQuery<?> query) {
		if (this.queryStrategy==null)
			throw new MechanizeException("No Query implementation set!!");
		
		for(Node node : nodes) 
			if(query.matches(queryStrategy, node))
				return getCachedOrNewRepresentation(node);

		return null;
	}
	
	public List<T> getAll(AbstractQuery<?> query) {
		if (this.queryStrategy==null)
			throw new MechanizeException("No Query implementation set!!");
		
		List<T> result = new ArrayList<T>();
		if (nodes != null)
			for(Node element : nodes)
				if(query.matches(queryStrategy, element))
					result.add(getCachedOrNewRepresentation(element));
		return result;
	}
	
	/** Returns a list with all representations of all elements. */
	public List<T> getAll() {
		List<T> result = new ArrayList<T>();
		if (nodes != null)
			for(Node element : nodes) 
				result.add(getCachedOrNewRepresentation(element));
		return result;
	}
	
	public int size() {
		return nodes.size();
	}
	
	public T get(int index) {
		return getCachedOrNewRepresentation(nodes.get(index));
	}
		
	public Iterator<T> iterator() {
		return getAll().iterator();
	}
}
