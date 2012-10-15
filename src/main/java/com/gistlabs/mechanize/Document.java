package com.gistlabs.mechanize;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Represents a single or multiple-page document having a root node.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public abstract class Document extends Resource {

	public Document(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
		super(agent, request, response);
	}

	/**
	 * Returns the root node of the document
	 * @return
	 */
	public abstract Node getRoot();
}
