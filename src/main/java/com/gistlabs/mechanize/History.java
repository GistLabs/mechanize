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
	private Stack<Document> history = new Stack<Document>();
	
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
	
	public Document get(int index) {
		return history.get(index);
	}
	
	/** Returns the current document or null if none. */
	public Document getCurrent() {
		return !isEmpty() ? history.peek() : null;
	}
	
	/** Returns the removed current document from history stack or null if history is empty. */
	public Document pop() {
		return !isEmpty() ? history.pop() : null;
	}
	
	public boolean isEmpty() {
		return history.isEmpty();
	}
	
	/** Returns the new Document version from reloading the current document or null if history is empty. */
	public Document reload() {
		if(!isEmpty()) {
			Document toReload = pop();
			return agent.request(toReload.getRequest());
		}
		else
			return null;
	}
	
	public boolean contains(Document document) {
		return history.contains(document);
	}
	
	public int size() {
		return history.size();
	}

	/** Pushes the given page to the history making it current. */ 
	public void add(Document document) {
		if(history.size() == maximumSize)
			history.remove(0);
		history.push(document);
	}
}
