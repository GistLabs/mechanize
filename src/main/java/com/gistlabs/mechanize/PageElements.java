/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gistlabs.mechanize.query.Query;

/**
 * Abstract implementation stub for page elements implementing dynamic caching of representations of DOM elements. 
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public abstract class PageElements<T> implements Iterable<T> {
	protected final Page page;
	protected final Elements elements;
	
	private final Map<Element, T> representations = new HashMap<Element, T>();
	
	public PageElements(Page page, Elements elements) {
		this.page = page;
		this.elements = elements;
	}

	public Elements getElements() {
		return elements;
	}
	
	public Page getPage() {
		return page;
	}
	
	protected T getCachedOrNewRepresentation(Element element) {
		if(element != null) {
			if(!representations.containsKey(element))
				representations.put(element, newRepresentation(element));
			
			return representations.get(element);
		}
		else
			return null;
	}

	protected abstract T newRepresentation(Element element);
	
	public T get(Query query) {
		if (elements==null)
			return null;

		for(Element element : elements)
			if(query.matches(element))
				return getCachedOrNewRepresentation(element);
		return null;
	}
	
	public List<T> getAll(Query query) {
		List<T> result = new ArrayList<T>();
		if (elements!=null)
			for(Element element : elements)
				if(query.matches(element))
					result.add(getCachedOrNewRepresentation(element));
		return result;
	}
	
	/** Returns a list with all representations of all elements. */
	public List<T> getAll() {
		List<T> result = new ArrayList<T>();
		if (elements!=null)
			for(Element element : elements) 
				result.add(getCachedOrNewRepresentation(element));
		return result;
	}
	
	public int size() {
		return elements==null ? 0 : elements.size();
	}
	
	public T get(int index) {
		if (elements==null)
			return null;

		return getCachedOrNewRepresentation(elements.get(index));
	}
		
	public Iterator<T> iterator() {
		return getAll().iterator();
	}
}
