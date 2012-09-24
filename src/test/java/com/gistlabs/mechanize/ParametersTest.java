package com.gistlabs.mechanize;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


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
}
