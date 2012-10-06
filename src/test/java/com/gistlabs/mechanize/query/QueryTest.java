/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.query;

import static com.gistlabs.mechanize.query.QueryBuilder.inBrackets;
import static com.gistlabs.mechanize.query.QueryBuilder.not;
import static com.gistlabs.mechanize.query.QueryBuilder.byId;
import static com.gistlabs.mechanize.query.QueryBuilder.byName;
import static com.gistlabs.mechanize.query.QueryBuilder.caseInsensitive;
import static com.gistlabs.mechanize.query.QueryBuilder.everything;
import static com.gistlabs.mechanize.query.QueryBuilder.regEx;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import com.gistlabs.mechanize.query.QueryBuilder;
import com.gistlabs.mechanize.query.Query.Pattern;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class QueryTest {

	@Test
	public void testSinglePartQueryWithString() {
		assertEquals("(<value,[any]>)", QueryBuilder.inBrackets(QueryBuilder.byAny("value")).toString());
		assertEquals("<not<value,[any]>>", QueryBuilder.not(QueryBuilder.byAny("value")).toString());
		assertEquals("<value,[any]>", QueryBuilder.byAny("value").toString());
		assertEquals("<value,[name]>", QueryBuilder.byName("value").toString());
		assertEquals("<value,[id]>", QueryBuilder.byId("value").toString());
		assertEquals("<value,[name,id]>", QueryBuilder.byNameOrId("value").toString());
		assertEquals("<value,[tag]>", QueryBuilder.byTag("value").toString());
		assertEquals("<value,[class]>", QueryBuilder.byClass("value").toString());
		assertEquals("<value,[href]>", QueryBuilder.byHRef("value").toString());
		assertEquals("<value,[src]>", QueryBuilder.bySrc("value").toString());
		assertEquals("<value,[title]>", QueryBuilder.byTitle("value").toString());
		assertEquals("<value,[width]>", QueryBuilder.byWidth("value").toString());
		assertEquals("<value,[height]>", QueryBuilder.byHeight("value").toString());
		assertEquals("<value,[value]>", QueryBuilder.byValue("value").toString()); 
		assertEquals("<value,[type]>", QueryBuilder.byType("value").toString());
		assertEquals("<value,[text]>", QueryBuilder.byText("value").toString());
		assertEquals("<value,[innerHtml]>", QueryBuilder.byInnerHtml("value").toString());
		assertEquals("<value,[html]>", QueryBuilder.byHtml("value").toString());
	}
	
	@Test
	public void testSinglePartQueryWithPattern() {
		assertEquals("<regEx(pattern),[any]>", QueryBuilder.byAny(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[name]>", QueryBuilder.byName(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[id]>", QueryBuilder.byId(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[name,id]>", QueryBuilder.byNameOrId(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[tag]>", QueryBuilder.byTag(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[class]>", QueryBuilder.byClass(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[href]>", QueryBuilder.byHRef(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[src]>", QueryBuilder.bySrc(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[title]>", QueryBuilder.byTitle(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[width]>", QueryBuilder.byWidth(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[height]>", QueryBuilder.byHeight(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[value]>", QueryBuilder.byValue(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[type]>", QueryBuilder.byType(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[text]>", QueryBuilder.byText(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[html]>", QueryBuilder.byHtml(regEx("pattern")).toString());
	}
	
	@Test
	public void testComposition() {
		assertEquals("<name,[name]>or<regEx(id),[id]>or<text,[text]>", byName("name").or.byId(regEx("id")).or.byText("text").toString());
	}

	@Test
	public void testAndComposition() {
		assertEquals("<name,[name]>and<regEx(id),[id]>and<text,[text]>", byName("name").and.byId(regEx("id")).and.byText("text").toString());
	}
	
	@Test
	public void testOrIsDefaultComposition() {
		assertEquals("<name,[name]>or<regEx(id),[id]>or<text,[text]>", byName("name").byId(regEx("id")).byText("text").toString());
	}
	
	@Test
	public void testOrIsStillStandardCompositionAfterAnd() {
		assertEquals("<name,[name]>and<regEx(id),[id]>or<text,[text]>", byName("name").and.byId(regEx("id")).byText("text").toString());
	}
	
	@Test
	public void testSimpleQueryMatchingTrue() {
		assertTrue(QueryBuilder.byAny("value").matches(newElement("<a href='value'/>")));
		assertTrue(QueryBuilder.byName("value").matches(newElement("<a name='value'/>")));
		assertTrue(QueryBuilder.byId("value").matches(newElement("<a id='value'/>")));
		assertTrue(QueryBuilder.byNameOrId("value").matches(newElement("<a name='value'/>")));
		assertTrue(QueryBuilder.byNameOrId("value").matches(newElement("<a id='value'/>")));
		assertTrue(QueryBuilder.byTag("p").matches(newElement("<p/>")));
		assertTrue(QueryBuilder.byClass("value").matches(newElement("<a class='value'/>")));
		assertTrue(QueryBuilder.byHRef("value").matches(newElement("<a href='value'/>")));
		assertTrue(QueryBuilder.bySrc("value").matches(newElement("<a src='value'/>")));
		assertTrue(QueryBuilder.byTitle("value").matches(newElement("<a title='value'/>")));
		assertTrue(QueryBuilder.byWidth("value").matches(newElement("<a width='value'/>")));
		assertTrue(QueryBuilder.byHeight("value").matches(newElement("<a height='value'/>")));
		assertTrue(QueryBuilder.byValue("value").matches(newElement("<a value='value'/>")));
		assertTrue(QueryBuilder.byType("value").matches(newElement("<a type='value'/>")));
		assertTrue(QueryBuilder.byText("value").matches(
				newElement("<a><b>v</b><strong>a<i>l</i>u</strong><b>e</b>")));
		assertTrue(QueryBuilder.byInnerHtml("value").matches(newElement("<a>value</a>")));
		assertTrue(QueryBuilder.byHtml("<a></a>").matches(newElement("<a></a>")));
	}
	
	@Test
	public void testCombinationQueryMatchingTrue() {
		assertTrue(byId("myId").or.bySrc("test.png").matches(newElement("<img id='myId'/>")));
		assertTrue(byId("myId").or.bySrc("test.png").matches(newElement("<img src='test.png'/>")));
		assertFalse(byId("myId").or.bySrc("test.png").matches(newElement("<img/>")));
	}
	
	private Element newElement(String string) {
		Document document = Jsoup.parse("<html><body>" + string + "</body></html>");
		return document.getElementsByTag("body").get(0).child(0);
	}
	
	@Test
	public void testAndCombination() {
		assertFalse(byId("myId").and.bySrc("test.png").matches(newElement("<img id='myId'/>")));
		assertFalse(byId("myId").and.bySrc("test.png").matches(newElement("<img src='test.png'/>")));
		assertTrue(byId("myId").and.bySrc("test.png").matches(newElement("<img id='myId' src='test.png'/>")));
		assertFalse(byId("myId").and.bySrc("test.png").matches(newElement("<img/>")));
	}

	@Test
	public void testInBracketsNot() {
		assertTrue(inBrackets(byId("myId")).matches(newElement("<img id='myId'/>")));
		assertFalse(inBrackets(byId("myId")).matches(newElement("<img id='otherId'/>")));
	}

	@Test
	public void testNot() {
		assertFalse(not(byId("myId")).matches(newElement("<img id='myId'/>")));
		assertTrue(not(byId("myId")).matches(newElement("<img id='otherId'/>")));
	}
	
	@Test
	public void testEverything() {
		assertEquals("<everything>", QueryBuilder.everything().toString());
		assertTrue(everything().matches(newElement("<a/>")));
	}
	
	@Test
	public void testCaseInsensitivePattern() {
		assertEquals("caseInsensitive(regEx(pattern))", caseInsensitive(regEx("pattern")).toString());
		assertEquals("caseInsensitive(string)", caseInsensitive("string").toString());
	}
	
	@Test
	public void testCaseSensitiveStringPattern() {
		Pattern pattern = QueryBuilder.string("string");
		assertTrue(pattern.doesMatch("string"));
		assertFalse(pattern.doesMatch("String"));
		assertFalse(pattern.doesMatch("STRING"));
		assertFalse(pattern.doesMatch("NoString"));
		assertFalse(pattern.doesMatch(null));
	}

	@Test
	public void testCaseInsensitiveStringPattern() {
		Pattern pattern = QueryBuilder.caseInsensitive("string");
		assertTrue(pattern.doesMatch("string"));
		assertTrue(pattern.doesMatch("String"));
		assertTrue(pattern.doesMatch("STRING"));
		assertFalse(pattern.doesMatch("NoString"));
		assertFalse(pattern.doesMatch(null));
	}
	
	@Test
	public void testCaseSensitiveRegularExpression() {
		Pattern pattern = regEx("[Tt]rue");
		assertTrue(pattern.doesMatch("true"));
		assertTrue(pattern.doesMatch("True"));
		assertFalse(pattern.doesMatch("TRUE"));
		assertFalse(pattern.doesMatch("false"));
		assertFalse(pattern.doesMatch(null));
	}
	
	@Test
	public void testCaseInsensitiveRegularExpression() {
		Pattern pattern = caseInsensitive(regEx("[t]rue"));
		assertTrue(pattern.doesMatch("true"));
		assertTrue(pattern.doesMatch("True"));
		assertTrue(pattern.doesMatch("TRUE"));
		assertFalse(pattern.doesMatch("false"));
		assertFalse(pattern.doesMatch(null));
	}
}
