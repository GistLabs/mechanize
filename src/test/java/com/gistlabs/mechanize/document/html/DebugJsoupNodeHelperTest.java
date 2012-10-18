package com.gistlabs.mechanize.document.html;

import static org.junit.Assert.*;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gistlabs.mechanize.util.css_query.NodeSelector;

public class DebugJsoupNodeHelperTest {
	private static Document document;

	public DebugJsoupNodeHelperTest() {
	}

	@BeforeClass
	public static void setUp() throws Exception {
		document = Jsoup.parse(DebugJsoupNodeHelperTest.class.getResourceAsStream("test.html"), "utf-8", "");
	}

	@Test
	public void debug() {
		String query = "div:first-child";
		int count = 51;

		NodeSelector<Node> selector = new NodeSelector<Node>(new JsoupNodeHelper(document), document);
		List<Node> nodes = selector.findAll(query);
		assertEquals("["+query+"]", count, nodes.size());
	}
}
