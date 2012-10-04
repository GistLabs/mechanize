/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import com.gistlabs.mechanize.HtmlPage;
import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.cookie.Cookie;
import com.gistlabs.mechanize.form.Form;

import org.junit.Test;

import static com.gistlabs.mechanize.query.QueryBuilder.*;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class AmazonAddItemToCartAndUseASecondAgentToRemoveTheItemIT {

	/**
	 * Adds the processor 'AMD FX 4100' to the shopping cart and using a second agent 
	 * to remove it. This test also demonstrates how to copy session cookies (and other cookies)
	 * from one agent to another.
	 */
	@Test
	public void testAddingAndRemovingAItemToAndFromShoppingCartUsingTwoAgents() {
		MechanizeAgent agentA = new MechanizeAgent();
		MechanizeAgent agentB = new MechanizeAgent();
		
		agentA.get("http://www.amazon.com");
		assertTrue("Ensure session cookie is used", agentA.cookies().getCount() > 0);
		agentA.idle(200);
		
		Page amdProcessorPage = agentA.get("http://www.amazon.com/gp/product/B005UBNL0A/");
		agentA.idle(250);
		Form form = amdProcessorPage.forms().get(byName("handleBuy"));
		agentA.idle(200);

		form.submit();
		agentA.idle(200);
		
		List<Cookie> cookies = agentA.cookies().getAll();
		agentB.cookies().addAllCloned(cookies);
		Page page = agentB.get("http://www.amazon.com");
		agentB.idle(200);
		Page cart = page.links().get(byId("nav-cart")).click();
		Form cartForm = cart.forms().get(byName("cartViewForm"));
		cartForm.get("quantity.C35RMYTCMZTEKE").setValue("0");
		agentB.idle(200);
		HtmlPage response = (HtmlPage)cartForm.submit();
		assertTrue(response.getDocument().outerHtml().contains("Your Shopping Cart is empty."));
	}
}