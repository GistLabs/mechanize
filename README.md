mechanize for java
==========

mechanize is a stateful HTTP/HTML client library. This implementation of mechanize is 
written in Java to be available on the JVM.

Project Home Page: http://gistlabs.com/software/mechanize
GitHub: https://github.com/GistLabs/mechanize
License: [MPL 2.0](http://mozilla.org/MPL/2.0/)

Here is a sample testcase showing the framework as work:
```java
	@Test public void testGooglePageSearchForm() {
		MechanizeAgent agent = new MechanizeAgent();
		Page page = agent.get("http://www.google.com");
		Form form = page.forms().get(byName("f"));
		form.get("q").set("mechanize java");
		Page response = form.submit();
		assertTrue(response.getTitle().startsWith("mechanize java"));
	}
```

Because mechanize is stateful if will by default support cookies and hidden 
form parameters (like Rails CSRF protection). This enables client code to more
easily follow links and behave like a RESTful hypermedia client more easily.