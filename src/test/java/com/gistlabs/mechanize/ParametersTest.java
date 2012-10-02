package com.gistlabs.mechanize;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gistlabs.mechanize.parameters.Parameters;


public class ParametersTest {
	
	@Test
	public void testAddAndSet() {
		Parameters parameters = new Parameters();
		assertEquals(0, parameters.getValueCount());
		
		parameters.add("name", "value");
		assertEquals(1, parameters.getValueCount());
		
		parameters.add("name", "value");
		assertEquals(1, parameters.getValueCount());
		
		parameters.add("name", "value2");
		assertEquals(2, parameters.getValueCount());

		parameters.set("name", "value");
		assertEquals(1, parameters.getValueCount());

		parameters.add("name", "value", "value2", "value3");
		assertEquals(3, parameters.getValueCount());

		parameters.set("name", "value4", "value5");
		assertEquals(2, parameters.getValueCount());
		
		parameters.remove("name");
		assertEquals(0, parameters.getValueCount());
	}
	
	@Test
	public void testHasAndGet() {
		Parameters parameters = new Parameters();
		String name = "name";
		assertFalse(parameters.has(name));
		assertNull(parameters.get(name));
		
		parameters.add("name", "value2");
		assertTrue(parameters.has(name));
		assertArrayEquals(new String [] {"value2"}, parameters.get(name));

		parameters.add("name", "value1");
		assertTrue(parameters.has(name));
		assertArrayEquals(new String [] {"value1", "value2"}, parameters.get(name));
	}

	@Test
	public void testGetNamesReturnsTheParametersInTheOrderTheyWhereFirstAdded() {
		Parameters parameters = new Parameters().add("name2", "value").add("name", "value");
		assertArrayEquals(new String [] {"name2", "name"}, parameters.getNames());
	}
}
