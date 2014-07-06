/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import static com.gistlabs.mechanize.util.css.CSSHelper.*;
import static org.junit.Assert.*;

import org.junit.Before;
//import org.junit.Test;




import com.gistlabs.mechanize.Mechanize;
import com.gistlabs.mechanize.document.Document;
import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.html.form.Form;
import com.gistlabs.mechanize.impl.MechanizeAgent;
import com.gistlabs.mechanize.sequence.AbstractSequence;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class AmazonAddItemToCartAndUseASecondAgentToRemoveTheItemIT extends MozillaUserAgentTestClass {
	static final String firefoxUserAgent = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.1) Gecko/20100122 firefox/3.6.1";
	Mechanize agent;

	@Before
	public void setUp() throws Exception {
		agent = new MechanizeAgent().setUserAgent(null);
	}


	/**
	 * Adds the processor 'AMD FX 4100' to the shopping cart and using a second agent
	 * to remove it. This test also demonstrates how to copy session cookies (and other cookies)
	 * from one agent to another.
	 */
//	@Test // no longer working
	public void testAddingAndRemovingAItemToAndFromShoppingCartUsingTwoAgents() {
		AddItemToShoppingCartSequence addItemToShoppingCartSequence = new AddItemToShoppingCartSequence("B005UBNL0A");
		RemoveItemFromShoppingCartSequence removeItemFromShoppingCartSequence = new RemoveItemFromShoppingCartSequence();

		MechanizeAgent agentA = new MechanizeAgent();
		MechanizeAgent agentB = new MechanizeAgent();

		addItemToShoppingCartSequence.run(agentA);

		assertTrue("Ensure session cookie is used", agentA.cookies().getCount() > 0);
		agentB.cookies().addAllCloned(agentA.cookies().getAll());
		assertTrue("Ensure session cookies has been transfered", agentB.cookies().getCount() > 0);

		removeItemFromShoppingCartSequence.run(agentB);
		assertTrue(removeItemFromShoppingCartSequence.wasShoppingCartEmpty());
	}

	private static class AddItemToShoppingCartSequence extends AbstractSequence {

		private final String productCode;

		public AddItemToShoppingCartSequence(final String productCodeToBuy) {
			this.productCode = productCodeToBuy;
		}

		@Override
		protected void run() {
			agent.get("http://www.amazon.com");
			agent.idle(200);

			Document amdProcessorPage = agent.get("http://www.amazon.com/gp/product/" + productCode + "/");
			agent.idle(250);
			Form form = amdProcessorPage.forms().find(byIdOrName("handleBuy"));
			agent.idle(200);

			form.submit();
			agent.idle(200);
		}
	}

	private static class RemoveItemFromShoppingCartSequence extends AbstractSequence {
		private boolean wasShoppingCartEmpty;

		@Override
		protected void run() {
			Document page = agent.get("http://www.amazon.com");
			agent.idle(200);
			Document cart = page.links().find(byIdOrName("nav-cart")).click();
			Form cartForm = cart.forms().find(byIdOrName("cartViewForm"));
			cartForm.get("quantity.C35RMYTCMZTEKE").setValue("0");
			agent.idle(200);
			HtmlDocument response = (HtmlDocument)cartForm.submit();
			wasShoppingCartEmpty = response.htmlElements().getRoot().getHtml().contains("Your Shopping Cart is empty.");
		}

		public boolean wasShoppingCartEmpty() {
			return wasShoppingCartEmpty;
		}
	}
}