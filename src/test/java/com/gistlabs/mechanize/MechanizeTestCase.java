/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.After;

import com.gistlabs.mechanize.MechanizeMock.PageRequest;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class MechanizeTestCase {
	protected MechanizeMock agent = new MechanizeMock();
	protected boolean doAfterTest = true;
	
	public void disableAfterTest() {
		doAfterTest = false;
	}
	
	@After
	public void afterTest() {
		if(doAfterTest) {
			PageRequest next = agent.nextUnexecutedPageRequest();
			if(next != null) 
				Assert.fail("Unexecuted page request: " + next.toString());
		}
	}
	
	protected String newHtml(String title, String bodyHtml) {
		return "<html><head><title>" + title + "</title></head><body>" + bodyHtml + "</body></html>";
	}

	protected String newHtml(String title, FormBuilder form) {
		return "<html><head><title>" + title + "</title></head><body>" + form.toString() + "</body></html>";
	}
	
	public List<NameValuePair> parameter(String name, String value) {
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		result.add(new BasicNameValuePair(name, value));
		return result;
	}
	
	protected FormBuilder newForm(String action) {
		return new FormBuilder(action);
	}

	protected FormBuilder newForm(String action, String method) {
		return new FormBuilder(action, method);
	}
	
	public static class FormBuilder {
		StringBuilder content = new StringBuilder();
		String action;
		String id;
		String name;
		String method;
		private String enctype;
		 
		public FormBuilder(String action) {
			this.action = action;
		}

		public FormBuilder(String action, String method) {
			this(action);
			method(method);
		}
		
		public String getAction() {
			return this.action;
		}
		
		public FormBuilder method(String method) {
			this.method = method;
			return this;
		}
		
		public String getMethod() {
			return this.method;
		}

		public FormBuilder enctype(String enctype) {
			this.enctype = enctype;
			return this;
		}
		
		public String getEnctype() {
			return this.enctype;
		}
		
		public FormBuilder id(String id) {
			this.id = id;
			return this;
		}

		public FormBuilder name(String name) {
			this.name = name;
			return this;
		}

		public FormBuilder addText(String name, String defaultValue) {
			String type = "text";
			appendSimpleInput(type, name, defaultValue);
			return this;
		}

		public FormBuilder addText(String name, String defaultValue, int maxLength) {
			String type = "text";
			appendSimpleInput(type, name, defaultValue, "maxlength='" + maxLength + "'");
			return this;
		}

		private void appendSimpleInput(String type, String name, String value, String...additionals) {
			content.append("<input type='" + type + "'");
			appendIfSet(content, "name", name);
			appendIfSet(content, "value", value);
			for(String toAdd : additionals) 
				content.append(" " + toAdd);
			content.append("/>");
		}
		
		public FormBuilder addFileInput(String name, String value) {
			appendSimpleInput("file", name, value);
			return this;
		}

		public FormBuilder addTextArea(String name, String value) {
			content.append("<textarea");
			appendIfSet(content, "name", name);
			content.append(">");
			if(value != null) 
				content.append(value);
			content.append("</textarea>");
			return this;
		}

		public FormBuilder addPassword(String name, String defaultValue) {
			appendSimpleInput("password", name, defaultValue);
			return this;
		}
		
		public FormBuilder addHidden(String name, String value) {
			appendSimpleInput("hidden", name, value);
			return this;
		}

		public FormBuilder addSubmitButton(String name, String value) {
			appendSimpleInput("submit", name, value);
			return this;
		}

		public FormBuilder addSubmitImage(String name, String value) {
			appendSimpleInput("image", name, value);
			return this;
		}

		public FormBuilder addCheckbox(String name, String value) {
			appendSimpleInput("checkbox", name, value);
			return this;
		}

		public FormBuilder addCheckedCheckbox(String name, String value) {
			appendSimpleInput("checkbox", name, value, "checked"); 
			return this;
		}
		
		public FormBuilder addRadioButton(String name, String value) {
			appendSimpleInput("radio", name, value);
			return this;
		}

		public FormBuilder addCheckedRadioButton(String name, String value) {
			appendSimpleInput("radio", name, value, "checked"); 
			return this;
		}
		
		public SelectBuilder beginSelect(String name) {
			return new SelectBuilder(name, false);
		}
		
		public SelectBuilder beginMultiSelect(String name) {
			return new SelectBuilder(name, true);
		}
		
		@Override
		public String toString() {
			StringBuilder form = new StringBuilder();
			form.append("<form");
			appendIfSet(form, "action", action);
			appendIfSet(form, "id", id);
			appendIfSet(form, "name", name);
			appendIfSet(form, "enctype", enctype);
			appendIfSet(form, "method", method);
			form.append(">");
			form.append(content.toString());
			form.append("</form>");
			return form.toString();
		}
		
		private void appendIfSet(StringBuilder builder, String name, String value) {
			if(value != null)
				builder.append(" " + name + "='" + value + "'");
		}
		
		public class SelectBuilder {
			public SelectBuilder(String name, boolean isMultiple) {
				content.append("<select name='" + name + "'" + (isMultiple ? " multiple" : "") + ">");
			}
			
			public SelectBuilder addOption(String text) {
				return addOption(text, text);
			}

			public SelectBuilder addOption(String text, String value) {
				return addOption(text, value, false);
			}
			
			public SelectBuilder addOption(String text, String value, boolean isSelected) {
				content.append("<option");
				if(text != value)
					content.append(" value='" + value + "'");
				if(isSelected)
					content.append(" selected");
				content.append(">");
				content.append(text);
				content.append("</option>");
				return this;
			}

			public SelectBuilder addSelectedOption(String text, String value) {
				return addOption(text, value, true);
			}

			public FormBuilder end() {
				content.append("</select>");
				return FormBuilder.this;
			}
		}
	}
}
