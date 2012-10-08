package com.gistlabs.mechanize;

import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * This is a page factory, that can be registered 
 * 
 * 
 * @author jheintz
 *
 */
public interface PageFactory {
	Collection<String> getContentMatches();
	Page buildPage(MechanizeAgent agent, HttpRequestBase request, HttpResponse response);
}
