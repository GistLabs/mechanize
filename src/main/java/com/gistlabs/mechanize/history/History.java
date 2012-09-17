/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.history;

import java.util.Stack;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Page;

/** 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class History {
	
	public final static int DEFAULT_MAXIMUM_SIZE = 50;
	
	private final MechanizeAgent agent;
	private int maximumSize = DEFAULT_MAXIMUM_SIZE;
	private Stack<Page> history = new Stack<Page>();
	
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
	
	public Page get(int index) {
		return history.get(index);
	}
	
	/** Returns the current page or null if none. */
	public Page getCurrent() {
		return !isEmpty() ? history.peek() : null;
	}
	
	/** Returns the removed current page from history stack or null if history is empty. */
	public Page pop() {
		return !isEmpty() ? history.pop() : null;
	}
	
	public boolean isEmpty() {
		return history.isEmpty();
	}
	
	/** Returns the new Page version from reloading the current page or null if history is empty. */
	public Page reload() {
		if(!isEmpty()) {
			Page pageToReload = pop();
			return agent.get(pageToReload.getRequest());
		}
		else
			return null;
	}
	
	public boolean contains(Page page) {
		return history.contains(page);
	}
	
	public int size() {
		return history.size();
	}

	/** Pushes the given page to the history making it current. */ 
	public void add(Page page) {
		if(history.size() == maximumSize)
			history.remove(0);
		history.push(page);
	}
}
