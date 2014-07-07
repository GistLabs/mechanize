/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.gistlabs.mechanize.parameters.Parameters;


public class ParametersTest {
	
	@Test
	public void testConstructingParametersFromMap() {
		Map<String, Object> parameterValues = new HashMap<String, Object>();
		
		parameterValues.put("name", "value");
		parameterValues.put("array", new String [] {"value1", "value2"});
		
		Parameters parameters = new Parameters(parameterValues);
		assertEquals(3, parameters.getValueCount());
		assertEquals(1, parameters.get("name").length);
		assertEquals(2, parameters.get("array").length);
	}
	
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
	public void testSettingSameValueASecondTimeIsIgnored() {
		Parameters parameters = new Parameters().set("key", "value", "value");
		assertEquals(1, parameters.getValueCount());
	}

	@Test
	public void testAddingSameValueASecondTimeIsIgnored() {
		Parameters parameters = new Parameters().set("key", "value").add("key", "value");
		assertEquals(1, parameters.getValueCount());
	}
	
	@Test
	public void testGetNamesReturnsTheParametersInTheOrderTheyWhereFirstAdded() {
		Parameters parameters = new Parameters().add("name2", "value").add("name", "value");
		assertArrayEquals(new String [] {"name2", "name"}, parameters.getNames());
	}
}
