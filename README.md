mechanize for java [![Build Status](https://gistlabs.ci.cloudbees.com/job/mechanize/badge/icon)](https://gistlabs.ci.cloudbees.com/job/mechanize/)
==========

mechanize is a stateful HTTP/HTML client library. This implementation of mechanize is 
written in Java to be available on the JVM. It utilizes 
[HttpClient](http://hc.apache.org/httpcomponents-client-ga/index.html) for HTTP handling 
and [JSoup](http://jsoup.org) for HTML parsing. Because mechanize is stateful, it will by 
default support cookies and hidden form parameters (like Rails CSRF protection). 
This enables client code to follow links  and behave like a RESTful hypermedia client more easily.


* Project Home Page: http://gistlabs.com/software/mechanize-for-java/
* GitHub: https://github.com/GistLabs/mechanize
* Jenkins CI: https://gistlabs.ci.cloudbees.com/job/mechanize/
* License: [MPL 2.0](http://mozilla.org/MPL/2.0/)

Here is a sample testcase showing mechanize at work:
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

Release log:

* 0.10.0 (pending). Support GETing multiple content types, not just HTML. See https://github.com/GistLabs/mechanize/issues?milestone=7&page=1&state=closed
* 0.9.1 on Oct 3rd, 2012. See https://github.com/GistLabs/mechanize/issues?milestone=6&state=closed for list of resolved issues.
* 0.9.0 on Sept 28th, 2012. See https://github.com/GistLabs/mechanize/issues?milestone=2&page=1&state=closed for list of resolved issues.
* 0.8.0 on Sept 20th, 2012.

Download the code from https://github.com/GistLabs/mechanize/downloads, or use a Maven repository client:
```
  <dependency>
    <groupId>com.gistlabs</groupId>
    <artifactId>mechanize</artifactId>
    <version>0.9.1</version>
  </dependency>
```

Other implementations of mechanize include:
* Perl http://search.cpan.org/dist/WWW-Mechanize/
* Python http://pypi.python.org/pypi/mechanize/
* Ruby http://mechanize.rubyforge.org/
