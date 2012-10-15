/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.util.Stack;


/** 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class History {
	
	public final static int DEFAULT_MAXIMUM_SIZE = 50;
	
	private final MechanizeAgent agent;
	private int maximumSize = DEFAULT_MAXIMUM_SIZE;
	private Stack<Resource> history = new Stack<Resource>();
	
	public History(MechanizeAgent agent) {
		this.agent = agent;
	}
	
	public int getMaximumSize() {
		return maximumSize;
	}
	
	public void setMaximumSize(int maximumSize) {
		if(maximumSize <= 0)
			throw new IllegalArgumentException("Maximum size must be positive and not zero");
		this.maximumSize = maximumSize;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Resource> T get(int index) {
		return (T) history.get(index);
	}
	
	/** Returns the current document or null if none. */
	@SuppressWarnings("unchecked")
	public <T extends Resource> T getCurrent() {
		return (T) (!isEmpty() ? history.peek() : null);
	}
	
	/** Returns the removed current document from history stack or null if history is empty. */
	@SuppressWarnings("unchecked")
	public <T extends Resource> T pop() {
		return (T) (!isEmpty() ? history.pop() : null);
	}
	
	public boolean isEmpty() {
		return history.isEmpty();
	}
	
	/** Returns the new Document version from reloading the current document or null if history is empty. */
	public <T extends Resource> T reload() {
		if(!isEmpty()) {
			T toReload = pop();
			return agent.request(toReload.getRequest());
		}
		else
			return null;
	}
	
	public boolean contains(Resource document) {
		return history.contains(document);
	}
	
	public int size() {
		return history.size();
	}

	/** Pushes the given page to the history making it current. */ 
	public void add(Resource document) {
		if(history.size() == maximumSize)
			history.remove(0);
		history.push(document);
	}
}
