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

import com.gistlabs.mechanize.html.HtmlElement;
import com.gistlabs.mechanize.html.HtmlElements;
import com.gistlabs.mechanize.query.Query;
import com.gistlabs.mechanize.util.Assert;

/**
 * Abstract implementation stub for page elements implementing dynamic caching of representations of DOM elements. 
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public abstract class PageElements<T> implements Iterable<T> {
	protected final Page page;
	protected final List<HtmlElement> elements;
	
	private final Map<HtmlElement, T> representations = new HashMap<HtmlElement, T>();
	
	public PageElements(Page page, List<HtmlElement> elements) {
		Assert.notNull(elements, "Elements may not be null");
		
		this.page = page;
		this.elements = elements;
	}
	
	public Page getPage() {
		return page;
	}
	
	protected T getCachedOrNewRepresentation(HtmlElement element) {
		if(element != null) {
			if(!representations.containsKey(element))
				representations.put(element, newRepresentation(element));
			
			return representations.get(element);
		}
		else
			return null;
	}

	protected abstract T newRepresentation(HtmlElement element);
	
	public T get(Query query) {
		HtmlElements.HtmlQueryStrategy queryStrategy = new HtmlElements.HtmlQueryStrategy();
		
		for(HtmlElement element : elements) 
			if(query.matches(queryStrategy, element))
				return getCachedOrNewRepresentation(element);

		return null;
	}
	
	public List<T> getAll(Query query) {
		HtmlElements.HtmlQueryStrategy queryStrategy = new HtmlElements.HtmlQueryStrategy();

		List<T> result = new ArrayList<T>();
		if (elements != null)
			for(HtmlElement element : elements)
				if(query.matches(queryStrategy, element))
					result.add(getCachedOrNewRepresentation(element));
		return result;
	}
	
	/** Returns a list with all representations of all elements. */
	public List<T> getAll() {
		List<T> result = new ArrayList<T>();
		if (elements != null)
			for(HtmlElement element : elements) 
				result.add(getCachedOrNewRepresentation(element));
		return result;
	}
	
	public int size() {
		return elements.size();
	}
	
	public T get(int index) {
		return getCachedOrNewRepresentation(elements.get(index));
	}
		
	public Iterator<T> iterator() {
		return getAll().iterator();
	}
}
