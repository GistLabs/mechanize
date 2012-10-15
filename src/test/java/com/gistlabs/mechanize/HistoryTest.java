/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gistlabs.mechanize.document.Document;

/** 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class HistoryTest {
	@Test
	public void testHistory() {
		MechanizeAgent agent = new MechanizeAgent();
		assertEquals(50, agent.history().getMaximumSize());
		agent.history().setMaximumSize(3);
		assertEquals(3, agent.history().getMaximumSize());
		
		Document page1 = agent.get("http://www.wikipedia.org");
		Document page2 = agent.get("http://www.google.com");
		Document page3 = agent.get("http://www.amazon.com");
		Document page4 = agent.get("http://www.twitter.com");
		
		assertEquals(3, agent.history().size());
		assertFalse(agent.history().contains(page1));
		assertSame(page2, agent.history().get(0));
		assertSame(page3, agent.history().get(1));
		assertSame(page4, agent.history().get(2));
		assertSame(page4, agent.history().getCurrent());
		
		assertSame(page4, agent.history().pop());

		assertEquals(2, agent.history().size());
		assertSame(page3, agent.history().getCurrent());
		
	}
	
	@Test
	public void testReload() {
		MechanizeAgent agent = new MechanizeAgent();
		assertNull("Reloading empty history results in null", agent.history().reload());
		Document page1 = agent.get("http://www.wikipedia.org");
		Document page2 = agent.history().reload();
		assertEquals(1, agent.history().size());
		assertFalse(agent.history().contains(page1));
		assertSame(page2, agent.history().get(0));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSettingMaximumSizeToZeroFails() {
		new MechanizeAgent().history().setMaximumSize(0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSettingMaximumSizeToNegativeFails() {
		new MechanizeAgent().history().setMaximumSize(-1);
	}
}
