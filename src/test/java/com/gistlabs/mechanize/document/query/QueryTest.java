/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.query;

import static com.gistlabs.mechanize.document.query.AbstractQueryBuilder.*;
import static com.gistlabs.mechanize.document.query.QueryBuilder.*;
import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import com.gistlabs.mechanize.document.html.query.HtmlQueryStrategy;
import com.gistlabs.mechanize.document.query.Pattern;
import com.gistlabs.mechanize.document.query.QueryBuilder;
import com.gistlabs.mechanize.document.query.QueryStrategy;


/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class QueryTest {

	QueryStrategy strategy = new HtmlQueryStrategy();
	
	@Test
	public void testSinglePartQueryWithString() {
		assertEquals("(<value,[*]>)", QueryBuilder.inBrackets(QueryBuilder.byAny("value")).toString());
		assertEquals("<not<value,[*]>>", QueryBuilder.not(QueryBuilder.byAny("value")).toString());
		assertEquals("<value,[test]>", QueryBuilder.by("test", "value").toString());
		assertEquals("<value,[test, test2]>", QueryBuilder.by(attributes("test", "test2"), "value").toString());
		assertEquals("<value,[*]>", QueryBuilder.byAny("value").toString());
		assertEquals("<value,[${nodeName}]>", QueryBuilder.byNodeName("value").toString());
		assertEquals("<value,[${nodeValue}]>", QueryBuilder.byNodeValue("value").toString());
	}
	
	@Test
	public void testSinglePartQueryWithPattern() {
		assertEquals("<regEx(pattern),[test]>", QueryBuilder.by("test", regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[test, test2]>", QueryBuilder.by(attributes("test", "test2"), regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[*]>", QueryBuilder.byAny(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[${nodeName}]>", QueryBuilder.byNodeName(regEx("pattern")).toString());
		assertEquals("<regEx(pattern),[${nodeValue}]>", QueryBuilder.byNodeValue(regEx("pattern")).toString());
	}
	
	@Test
	public void testComposition() {
		assertEquals("<name,[${nodeName}]>or<regEx(value),[${nodeValue}]>or<text,[*]>", 
				byNodeName("name").or.byNodeValue(regEx("value")).or.byAny("text").toString());
	}

	@Test
	public void testAndComposition() {
		assertEquals("<name,[${nodeName}]>and<regEx(value),[${nodeValue}]>and<text,[*]>", 
				byNodeName("name").and.byNodeValue(regEx("value")).and.byAny("text").toString());
	}
	
	@Test
	public void testOrIsDefaultComposition() {
		assertEquals("<name,[${nodeName}]>or<regEx(value),[${nodeValue}]>or<text,[*]>", 
				byNodeName("name").or.byNodeValue(regEx("value")).or.byAny("text").toString());
	}
	
	@Test
	public void testOrIsStillStandardCompositionAfterAnd() {
		assertEquals("<name,[${nodeName}]>and<regEx(value),[${nodeValue}]>or<text,[*]>", 
				byNodeName("name").and.byNodeValue(regEx("value")).byAny("text").toString());
	}
	
	@Test
	public void testSimpleQueryMatchingTrue() {
		assertEquals("<value,[test]>", QueryBuilder.by("test", "value").toString());
		assertEquals("<value,[test, test2]>", QueryBuilder.by(attributes("test", "test2"), "value").toString());
		
		assertTrue(QueryBuilder.by("test", "value").matches(strategy, newElement("<a test='value'/>")));
		assertTrue(QueryBuilder.by(attributes("test", "test2"), "value").matches(strategy, newElement("<a test='value'/>")));
		assertTrue(QueryBuilder.by(attributes("test", "test2"), "value").matches(strategy, newElement("<a test2='value'/>")));

		assertTrue(QueryBuilder.byAny("value").matches(strategy, newElement("<a href='value'/>")));
		assertTrue(QueryBuilder.byNodeName("a").matches(strategy, newElement("<a name='value'/>")));
		assertTrue(QueryBuilder.byNodeValue("link").matches(strategy, newElement("<a id='value'>link</a>")));
	}
	
	@Test
	public void testOrCombinationQueryMatchingTrue() {
		assertTrue(byNodeName("a").or.byNodeValue(regEx("value")).matches(strategy, newElement("<a>value</a>")));
		assertTrue(byNodeName("a").or.byNodeValue(regEx("value")).matches(strategy, newElement("<a>value</a>")));
	}
	
	private Element newElement(String string) {
		Document document = Jsoup.parse("<html><body>" + string + "</body></html>");
		return document.getElementsByTag("body").get(0).child(0);
	}
	
	@Test
	public void testAndCombination() {
		assertTrue(byNodeName("a").and.byNodeValue(regEx("value")).matches(strategy, newElement("<a>value</a>")));
		assertFalse(byNodeName("a").and.byNodeValue(regEx("value")).matches(strategy, newElement("<b>value</b>")));
		assertFalse(byNodeName("a").and.byNodeValue(regEx("value")).matches(strategy, newElement("<a></a>")));
	}

	@Test
	public void testInBracketsNot() {
		assertTrue(inBrackets(byAny("myId")).matches(strategy, newElement("<img id='myId'/>")));
	}

	@Test
	public void testNot() {
		assertFalse(not(byAny("myId")).matches(strategy, newElement("<img id='myId'/>")));
		assertTrue(not(byAny("myId")).matches(strategy, newElement("<img id='otherId'/>")));
	}
	
	@Test
	public void testEverything() {
		assertEquals("<everything>", QueryBuilder.everything().toString());
		assertTrue(everything().matches(strategy, newElement("<a/>")));
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
