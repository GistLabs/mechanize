package com.gistlabs.mechanize;

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
	
	public void add(Sequence ... sequences) {
		add(Arrays.asList(sequences));
	}
	
	public void add(Collection<Sequence> sequences) {
		this.sequences.addAll(sequences);
	}
	
	public Sequence getRandom() {
		return sequences.get(new Random().nextInt(sequences.size()));
	}
	
	public Sequence get(int index) {
		return sequences.get(index);
	}
	
	public int size() {
		return sequences.size();
	}
	
	@Override
	public Iterator<Sequence> iterator() {
		return sequences.iterator();
	}
}
