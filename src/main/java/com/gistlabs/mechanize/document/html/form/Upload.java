/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.form;

import java.io.File;

import com.gistlabs.mechanize.document.node.Node;

/** Represents an input element of type 'file' used for uploading data. 
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Upload extends FormElement {
	
	private File fileValue = null; 
	
	public Upload(Form form, Node node) {
		super(form, node);
	}
	
	@Override
	public void setValue(String value) {
		super.setValue(value);
		fileValue = null;
	}
	
	public void setValue(File file) {
		setValue(file.getAbsolutePath());
		fileValue = file;
	}
	
	public boolean hasFileValue() {
		return fileValue != null;
	}
	
	public File getFileValue() {
		return fileValue;
	}
}