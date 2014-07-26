mechanize for java [![Build Status](https://gistlabs.ci.cloudbees.com/job/mechanize/badge/icon)](https://gistlabs.ci.cloudbees.com/job/mechanize/)
==========

Note: This branch is working under the code name "lawnchair", as in RESTing in a lawn chair is nice. :)

==========

mechanize is a stateful HTTP web services client library (RESTful) with support for HTML, JSON, and XML services.
Because mechanize is stateful and allow inspection of web resources, it naturally support GET-then-POST semantics.


* Project Home Page: http://gistlabs.com/software/mechanize-for-java/
* GitHub: https://github.com/GistLabs/mechanize
* Jenkins CI: https://gistlabs.ci.cloudbees.com/job/mechanize/
* Forum: https://groups.google.com/forum/?fromgroups#!forum/mechanize4j
* License: [MPL 2.0](http://mozilla.org/MPL/2.0/)

======
Releases:

* 1.x is the original port of mechanize to java. This will be maintained for bug fixes and possibly other enhancements.
* 2.x is the modularized version to separate HTML, JSON, and XML into separate a la carte packages. Also the JSON linking dev will be here.

========


Here is a testcase showing mechanize at work with HTML:
```java
	@Test public void testGooglePageSearchForm() {
		MechanizeAgent agent = new MechanizeAgent();
		Document page = agent.get("http://www.google.com");
		Form form = page.form("f");
		form.get("q").set("mechanize java");
		Resource response = form.submit();
		assertTrue(response.getTitle().startsWith("mechanize java"));
	}
```

Here is a testcase showing mechanize using a Google JSON web service:
```java
	@Test
	public void testGoogleApi() throws JSONException {
		MechanizeAgent agent = new MechanizeAgent();
		JsonDocument json = agent.doRequest(googleUrl).add("shortUrl", shortUrl).add("projection", "FULL").get();

		assertEquals(longUrl, json.getRoot().find("longUrl").getValue());

		String value = json.getRoot().find("analytics month countries#US count").getValue();
		assertTrue(value, Integer.valueOf(value)>=1);
	}
```

Release log:

* 0.11.0 on Nov 12th, 2012. Support for Request Headers, #18 Cache and Conditional loading, and chained filters. See https://github.com/GistLabs/mechanize/issues?milestone=8&state=closed
* 0.10.0 on Oct 22nd, 2012. Support for Android, JSON, and CSS Selectors (even for JSON). See https://github.com/GistLabs/mechanize/issues?milestone=7&page=1&state=closed
* 0.9.1 on Oct 3rd, 2012. See https://github.com/GistLabs/mechanize/issues?milestone=6&state=closed for list of resolved issues.
* 0.9.0 on Sept 28th, 2012. See https://github.com/GistLabs/mechanize/issues?milestone=2&page=1&state=closed for list of resolved issues.
* 0.8.0 on Sept 20th, 2012.

Include this library (or download) from the Maven repository: http://search.maven.org/#browse%7C687823805

See the [Mechanize Wiki](https://github.com/GistLabs/mechanize/wiki) for more details!
