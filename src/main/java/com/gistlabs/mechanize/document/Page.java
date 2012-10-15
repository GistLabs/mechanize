package com.gistlabs.mechanize.document;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.gistlabs.mechanize.MechanizeAgent;

/**
 * Implements a single page document.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public abstract class Page extends Document {

	public Page(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
		super(agent, request, response);
	}

}
