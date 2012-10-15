package com.gistlabs.mechanize;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Implements a single page document.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public abstract class Page extends Document {

	public Page(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
		super(agent, request, response);
	}

}
