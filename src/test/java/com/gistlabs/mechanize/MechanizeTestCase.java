/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
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

	protected String newHtml(final String title, final String bodyHtml) {
		return "<html><head><title>" + title + "</title></head><body>" + bodyHtml + "</body></html>";
	}

	protected String newHtml(final String title, final FormBuilder form) {
		return "<html><head><title>" + title + "</title></head><body>" + form.toString() + "</body></html>";
	}

	public List<NameValuePair> parameter(final String name, final String value) {
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		result.add(new BasicNameValuePair(name, value));
		return result;
	}

	protected FormBuilder newForm(final String action) {
		return new FormBuilder(action);
	}

	protected FormBuilder newForm(final String action, final String method) {
		return new FormBuilder(action, method);
	}

	public static class FormBuilder {

		private final StringBuilder content = new StringBuilder();

		private final String action;

		private String id;

		private String name;

		private String method;

		private String enctype;

		public FormBuilder(final String action) {
			this.action = action;
		}

		public FormBuilder(final String action, final String method) {
			this(action);
			method(method);
		}

		public String getAction() {
			return this.action;
		}

		public FormBuilder method(final String method) {
			this.method = method;
			return this;
		}

		public String getMethod() {
			return this.method;
		}

		public FormBuilder enctype(final String enctype) {
			this.enctype = enctype;
			return this;
		}

		public String getEnctype() {
			return this.enctype;
		}

		public FormBuilder id(final String id) {
			this.id = id;
			return this;
		}

		public FormBuilder name(final String name) {
			this.name = name;
			return this;
		}

		public FormBuilder addInput(final String name, final String type, final String defaultValue) {
			appendSimpleInput(type, name, defaultValue);
			return this;
		}

		public FormBuilder addText(final String name, final String defaultValue) {
			String type = "text";
			appendSimpleInput(type, name, defaultValue);
			return this;
		}

		public FormBuilder addText(final String name, final String defaultValue, final int maxLength) {
			String type = "text";
			appendSimpleInput(type, name, defaultValue, "maxlength='" + maxLength + "'");
			return this;
		}

		public FormBuilder addRaw(final String htmlFragment) {
			content.append(htmlFragment);
			return this;
		}

		private void appendSimpleInput(final String type, final String name, final String value, final String...additionals) {
			content.append("<input type='" + type + "'");
			appendIfSet(content, "name", name);
			appendIfSet(content, "value", value);
			for(String toAdd : additionals)
				content.append(" " + toAdd);
			content.append("/>");
		}

		public FormBuilder addFileInput(final String name, final String value) {
			appendSimpleInput("file", name, value);
			return this;
		}

		public FormBuilder addTextArea(final String name, final String value) {
			content.append("<textarea");
			appendIfSet(content, "name", name);
			content.append(">");
			if(value != null)
				content.append(value);
			content.append("</textarea>");
			return this;
		}

		public FormBuilder addPassword(final String name, final String defaultValue) {
			appendSimpleInput("password", name, defaultValue);
			return this;
		}

		public FormBuilder addHidden(final String name, final String value) {
			appendSimpleInput("hidden", name, value);
			return this;
		}

		public FormBuilder addSubmitButton(final String name, final String value) {
			appendSimpleInput("submit", name, value);
			return this;
		}

		public FormBuilder addSubmitImage(final String name, final String value) {
			appendSimpleInput("image", name, value);
			return this;
		}

		public FormBuilder addCheckbox(final String name, final String value) {
			appendSimpleInput("checkbox", name, value);
			return this;
		}

		public FormBuilder addCheckedCheckbox(final String name, final String value) {
			appendSimpleInput("checkbox", name, value, "checked");
			return this;
		}

		public FormBuilder addRadioButton(final String name, final String value) {
			appendSimpleInput("radio", name, value);
			return this;
		}

		public FormBuilder addCheckedRadioButton(final String name, final String value) {
			appendSimpleInput("radio", name, value, "checked");
			return this;
		}

		public SelectBuilder beginSelect(final String name) {
			return new SelectBuilder(name, false);
		}

		public SelectBuilder beginMultiSelect(final String name) {
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

		private void appendIfSet(final StringBuilder builder, final String name, final String value) {
			if(value != null)
				builder.append(" " + name + "='" + value + "'");
		}

		public class SelectBuilder {
			public SelectBuilder(final String name, final boolean isMultiple) {
				content.append("<select name='" + name + "'" + (isMultiple ? " multiple" : "") + ">");
			}

			public SelectBuilder addOption(final String text) {
				return addOption(text, text);
			}

			public SelectBuilder addOption(final String text, final String value) {
				return addOption(text, value, false);
			}

			public SelectBuilder addOption(final String text, final String value, final boolean isSelected) {
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

			public SelectBuilder addSelectedOption(final String text, final String value) {
				return addOption(text, value, true);
			}

			public FormBuilder end() {
				content.append("</select>");
				return FormBuilder.this;
			}
		}
	}
}
