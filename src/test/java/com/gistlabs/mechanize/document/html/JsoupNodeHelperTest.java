/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gistlabs.mechanize.util.css_query.NodeSelector;

@RunWith(value = Parameterized.class)
public class JsoupNodeHelperTest {

	private final String query;
	private final int count;
	private static Document document;

	public JsoupNodeHelperTest(final String query, final int count) {
		this.query = query;
		this.count = count;
	}

	@BeforeClass
	public static void setUp() throws Exception {
		document = Jsoup.parse(JsoupNodeHelperTest.class.getResourceAsStream("test.html"), "utf-8", "");
	}

	@Test
	public void test() {
		NodeSelector<Node> selector = new NodeSelector<Node>(new JsoupNodeHelper(document), document);
		assertEquals("["+query+"]", count, selector.findAll(query).size());
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "*", 252 }, // this diverges from webkit tests now, see changes to TagChecker when initialPart
				{ ":root", 1 },
				{ ":empty", 2 },
				{ "div:first-child", 51 },
				{ "div:nth-child(even)", 106 },
				{ "div:nth-child(2n)", 106 },
				{ "div:nth-child(odd)", 137 },
				{ "div:nth-child(2n+1)", 137 },
				{ "div:nth-child(n)", 243 },
				{ "script:first-of-type", 1 },
				{ "div:last-child", 53 },
				{ "script:last-of-type", 1 },
				{ "script:nth-last-child(odd)", 1 },
				{ "script:nth-last-child(even)", 1 },
				{ "script:nth-last-child(5)", 0 },
				{ "script:nth-of-type(2)", 1 },
				{ "script:nth-last-of-type(n)", 2 },
				{ "div:only-child", 22 },
				{ "meta:only-of-type", 1 },
				{ "div > div", 242 },
				{ "div + div", 190 },
				{ "div ~ div", 190 },
				{ "body", 1 },
				{ "body div", 243 },
				{ "div", 243 },
				{ "div div", 242 },
				{ "div div div", 241 },
				{ "div, div, div", 243 },
				{ "div, a, span", 243 },
				{ ".dialog", 51 },
				{ "div.dialog", 51 },
				{ "div .dialog", 51 },
				{ "div.character, div.dialog", 99 },
				{ "#speech5", 1 },
				{ "div#speech5", 1 },
				{ "div #speech5", 1 },
				{ "div.scene div.dialog", 49 },
				{ "div#scene1 div.dialog div", 142 },
				{ "#scene1 #speech1", 1 },
				{ "div[class]", 103 },
				{ "div[class=dialog]", 50 },
				{ "div[class^=dia]", 51 },
				{ "div[class$=log]", 50 },
				{ "div[class*=sce]", 1 },
				{ "div[class|=dialog]", 50 },
				{ "div[class~=dialog]", 51 },
				{ "head > :not(meta)", 2 },
				{ "head > :not(:last-child)", 2 },
				{ "div:not(div.dialog)", 192 }
		};
		return Arrays.asList(data);
	}

}
