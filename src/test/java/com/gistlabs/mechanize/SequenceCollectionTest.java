package com.gistlabs.mechanize;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * 
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class SequenceCollectionTest {

	Sequence sequenceA = newSequence();
	Sequence sequenceB = newSequence();
	Sequence sequenceC = newSequence();
	
	@Test
	public void testAddingSameSequenceResultsInStoringItTwice() {
		SequenceCollection collection = new SequenceCollection().add(sequenceA, sequenceB, sequenceC);
		assertEquals(3, collection.size());
		collection.add(sequenceA);
		assertEquals(4, collection.size());
	}
	
	@Test
	public void testRandomlyDrawASequence() {
		
		SequenceCollection collection = new SequenceCollection().add(sequenceA, sequenceB, sequenceC);
		
		Map<Sequence, Integer> histogram = new HashMap<Sequence, Integer>();
		histogram.put(sequenceA, 0);
		histogram.put(sequenceB, 0);
		histogram.put(sequenceC, 0);
		for(int index = 0; index < 1000; index++) {
			Sequence sequence = collection.getRandom();
			histogram.put(sequence, histogram.get(sequence) + 1);
		}
		
		assertTrue(histogram.get(sequenceA) >= 100);
		assertTrue(histogram.get(sequenceB) >= 100);
		assertTrue(histogram.get(sequenceC) >= 100);
	}

	private Sequence newSequence() {
		return new Sequence() {
			@Override
			public void run(MechanizeAgent agent) {
			}
		};
	}
}
