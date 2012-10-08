package com.gistlabs.mechanize.html;

import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.PageFactory;

public class HtmlPageFactory implements PageFactory {

	@Override
	public Collection<String> getContentMatches() {
		return HtmlPage.CONTENT_MATCHERS;
	}

	@Override
	public Page buildPage(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
		return new HtmlPage(agent, request, response);
	}

}
