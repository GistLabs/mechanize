mechanize for java
==========

mechanize is a stateful HTTP web services client library (RESTful) with support for HTML, JSON, and XML services.
Because mechanize is stateful and allow inspection of web resources, it naturally support GET-then-POST semantics and helps avoid hard-coding clients.


* Project Home Page: http://gistlabs.com/software/mechanize-for-java/
* GitHub: https://github.com/GistLabs/mechanize
* Jenkins CI: https://gistlabs.ci.cloudbees.com/job/mechanize/
* Forum: https://groups.google.com/forum/?fromgroups#!forum/mechanize4j
* License: [MPL 2.0](http://mozilla.org/MPL/2.0/)


See the [Mechanize Wiki](https://github.com/GistLabs/mechanize/wiki) for more details and opinions on how RESTful clients should behave.

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

=========

Include this library (or download) from the Maven repository: http://search.maven.org/#browse%7C687823805
