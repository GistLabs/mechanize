/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.sequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A collection of Sequence objects that can be drawn from 
 * 
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class SequenceCollection implements Iterable<Sequence> {
	private final List<Sequence> sequences = new ArrayList<Sequence>();
	
	public SequenceCollection add(Sequence ... sequences) {
		add(Arrays.asList(sequences));
		return this;
	}
	
	public SequenceCollection add(Collection<Sequence> sequences) {
		this.sequences.addAll(sequences);
		return this;
	}
	
	public Sequence getRandom() {
		return sequences.get(new Random().nextInt(sequences.size()));
	}
	
	public Sequence get(int index) {
		return sequences.get(index);
	}
	
	public Sequence getByName(String name) {
		for(Sequence sequence : this)
			if(sequence.getName().equals(name))
				return sequence;
		return null;
	}
	
	public int size() {
		return sequences.size();
	}
	
	@Override
	public Iterator<Sequence> iterator() {
		return sequences.iterator();
	}
}
