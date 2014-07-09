/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.form;

import static com.gistlabs.mechanize.util.css.CSSHelper.*;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.AbstractDocument;
import com.gistlabs.mechanize.document.html.form.Select.Option;
import com.gistlabs.mechanize.util.apache.ContentType;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class FormTest extends MechanizeTestCase {

	protected String contentType() {
		return ContentType.TEXT_HTML.getMimeType();
	}
	
	protected String newHtml(final String title, final FormBuilder form) {
		return "<html><head><title>" + title + "</title></head><body>" + form.toString() + "</body></html>";
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

	@Test
	public void testEmptyFormWithGetMethod() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form")));
		addPageRequest("http://test.com/form", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		Resource response = form.submit();
		assertEquals("OK", response.getTitle());
		assertFalse(form.isDoPost());
	}

	@Test
	public void testEmptyFormWithPostMethod() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form", "post").id("form")));
		addPageRequest("POST", "http://test.com/form", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
		assertTrue(form.isDoPost());
	}

	@Test
	public void testSimpleInputNoText() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addText("text", null)));
		addPageRequest("http://test.com/form?text=", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	@Test
	public void testSimpleInputWithDefaultText() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addText("text", "Text")));
		addPageRequest("http://test.com/form?text=Text", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	@Test
	public void testSimpleInputSettingValueToValueBiggerThanMaxLengthWillGetAutomaticallyTruncated() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addText("text", null, 5)));
		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		form.find(byIdOrName("text")).set("123456789");
		assertEquals("12345", form.get("text").get());
	}

	@Test
	public void testEmailInputFieldWithNoText() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addInput("mail", "email", null)));
		addPageRequest("http://test.com/form?mail=", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		FormElement email = form.find(byIdOrName("mail"));
		assertTrue(email instanceof Email);
		assertSame(email, form.findEmail(byName("mail")));
		assertSame(email, form.findAll(byName("mail"), Email.class).get(0));
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	@Test
	public void testUnrecognizedInputFieldWithNoText() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addInput("unknown", "unknownType", null)));
		addPageRequest("http://test.com/form?unknown=test", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		FormElement unknown = form.find(byName("unknown"));
		assertNotNull(unknown);
		assertTrue(unknown instanceof FormElement);
		unknown.setValue("test");
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	@Test
	public void testInputTextWithName() {
		String newHtml = newHtml("Test Page", newForm("form").id("form")
				.addRaw("<input type=\"text\" class=\"txt\" name=\"login\" size=\"30\" onfocus=\"hlFF(this, true);\" onblur=\"hlFF(this);\">"));
		addPageRequest("http://test.com",
				newHtml);

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		FormElement login = form.find(byIdOrName("login"));
		assertNotNull(login);
		assertTrue(login instanceof FormElement);
		login.setValue("test");
	}

	@Test
	public void testTextAreaInputWithDefaultText() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addTextArea("text", "Text")));
		addPageRequest("http://test.com/form?text=Text", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	public void testTextAreaInputWithChangedValue() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addTextArea("text", "Text")));
		addPageRequest("http://test.com/form?text=differentText", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		form.findTextArea(byIdOrName("text")).setValue("differentText");
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	@Test
	public void testHiddenInput() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addHidden("hidden", "Text")));
		addPageRequest("http://test.com/form?hidden=Text", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSettingValueOfSubmitButtonFails() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addSubmitButton("button", "Text")));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		form.find(byIdOrName("button")).setValue("shouldFail");
	}

	@Test
	public void testSimpleLoginFormWithTextAndPasswordAndSubmitButtonSubmittingByButton() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addText("user", null).addPassword("pass", null).addSubmitButton("submit", "pressed")));
		addPageRequest("http://test.com/form?user=username&pass=password&submit=pressed", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		form.find(byIdOrName("user")).setValue("username");
		form.find(byIdOrName("pass")).setValue("password");
		AbstractDocument response = form.findSubmitButton(byIdOrName("submit")).submit();
		assertEquals("OK", response.getTitle());
	}

	@Test
	public void testSimpleLoginFormWithTextAndPasswordAndSubmitButtonSubmittingByPressingEnter() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addText("user", null).addPassword("pass", null).addSubmitButton("submit", "pressed")));
		addPageRequest("http://test.com/form?user=username&pass=password", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		form.find(byIdOrName("user")).setValue("username");
		form.find(byIdOrName("pass")).setValue("password");
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	@Test
	public void testCheckboxForm() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addCheckbox("box", "value1").addCheckedCheckbox("box", "value2").addCheckedCheckbox("box", "value3")));
		addPageRequest("http://test.com/form?box=value1&box=value3", newHtml("OK", ""));
		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		assertFalse(form.findCheckbox(byIdOrName("box"), "value1").isChecked());
		assertTrue(form.findCheckbox(byIdOrName("box"), "value2").isChecked());
		assertTrue(form.findCheckbox(byIdOrName("box"), "value3").isChecked());
		assertEquals(3, form.findAll(byIdOrName("box"), Checkbox.class).size());

		form.findCheckbox(byIdOrName("box"), "value1").check();
		form.findCheckbox(byIdOrName("box"), "value2").uncheck();
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSettingValueOfCheckboxFails() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addCheckbox("box", "value")));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		form.findCheckbox(byIdOrName("box")).setValue("shouldFail");
	}

	@Test
	public void testRadioButtonForm() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addRadioButton("button", "value1").
						addCheckedRadioButton("button", "value2").addRadioButton("button", "value3")));
		addPageRequest("http://test.com/form?button=value3", newHtml("OK", ""));
		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");

		assertFalse(form.findRadioButton(byIdOrName("button"), "value1").isChecked());
		assertTrue(form.findRadioButton(byIdOrName("button"), "value2").isChecked());
		assertFalse(form.findRadioButton(byIdOrName("button"), "value3").isChecked());
		
		assertEquals(3, form.findAll(byIdOrName("button"), RadioButton.class).size());

		form.findRadioButton(byIdOrName("button"), "value3").check();
		assertFalse(form.findRadioButton(byIdOrName("button"), "value1").isChecked());
		assertFalse(form.findRadioButton(byIdOrName("button"), "value2").isChecked());
		assertTrue(form.findRadioButton(byIdOrName("button"), "value3").isChecked());
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSettingValueOfRadioButtonFails() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addRadioButton("button", "value")));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		form.findRadioButton(byIdOrName("button")).setValue("shouldFail");
	}

	@Test
	public void testSingleElementSelect() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").beginSelect("person").addOption("Peter", "1").addOption("John", "2").addSelectedOption("Susanna", "3").end()));
		addPageRequest("http://test.com/form?person=1", newHtml("OK", ""));
		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		Select select = form.findSelect(byIdOrName("person"));
		assertFalse(select.isMultiple());
		assertEquals(3, select.getOptions().size());
		
		Option peter = select.getOption("Peter");
		assertEquals("Peter", peter.getText());
		assertEquals("1", peter.getValue());
		assertFalse(peter.isSelected());
		
		Option john = select.getOption("John");
		assertEquals("John", john.getText());
		assertEquals("2", john.getValue());
		assertFalse(john.isSelected());
		
		assertTrue(select.getOption("Susanna").isSelected());

		peter.select();
		assertTrue(peter.isSelected());
		assertFalse(john.isSelected());
		assertFalse(select.getOption("Susanna").isSelected());
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	@Test
	public void testMultipleElementSelect() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").beginMultiSelect("person").addOption("Peter", "1").addSelectedOption("John", "2").addOption("Susanna", "3").end()));
		addPageRequest("http://test.com/form?person=2&person=3", newHtml("OK", ""));
		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		Select select = form.findSelect(byIdOrName("person"));
		
		assertTrue(select.isMultiple());
		assertFalse(select.getOption("Peter").isSelected());
		assertTrue(select.getOption("John").isSelected());
		assertFalse(select.getOption("Susanna").isSelected());
		assertEquals(3, select.getOptions().size());

		select.getOption("Peter").select();
		assertTrue(select.getOption("Peter").isSelected());
		assertTrue(select.getOption("John").isSelected());
		assertFalse(select.getOption("Susanna").isSelected());

		select.getOption("Susanna").select();
		select.getOption("Peter").unselect();
		assertFalse(select.getOption("Peter").isSelected());
		assertTrue(select.getOption("John").isSelected());
		assertTrue(select.getOption("Susanna").isSelected());

		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSettingValueOfSelectFails() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").beginSelect("person").addOption("Peter", "1").addOption("John", "2").addSelectedOption("Susanna", "3").end()));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		form.findSelect(byIdOrName("person")).setValue("shouldFail");
	}

	@Test
	public void testImageToSubmitForm() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addSubmitImage("submitImage", "value")));
		addPageRequest("http://test.com/form?submitImage=value&submitImage.x=20&submitImage.y=10", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		AbstractDocument response = form.findSubmitImage(byIdOrName("submitImage")).submit(20, 10);
		assertEquals("OK", response.getTitle());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSettingValueOfSubmitImageFails() {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").id("form").addSubmitImage("submitImage", "value")));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		form.findSubmitImage(byIdOrName("submitImage")).setValue("shouldFail");
	}

	@Test
	public void testSimpleFileUpload() throws Exception {
		File tmpFile = File.createTempFile("mechanize", "tmp");

		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").method("post").id("form").enctype("multipart/form-data").addFileInput("fileUpload", "")));
		addPageRequest("POST", "http://test.com/form", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");
		form.findUpload(byIdOrName("fileUpload")).setValue(tmpFile);
		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testFileUploadWithNoFile() throws Exception {
		addPageRequest("http://test.com",
				newHtml("Test Page", newForm("form").method("post").id("form").enctype("multipart/form-data").addFileInput("fileUpload", null)));
		addPageRequest("POST", "http://test.com/form", newHtml("OK", ""));

		AbstractDocument page = agent().get("http://test.com");
		Form form = page.forms().find("#form");

		AbstractDocument response = form.submit();
		assertEquals("OK", response.getTitle());
	}


	//TODO Find a better test
	//	@Test
	//	public void testFileUploadingByUsingMegaFileUpload() {
	//		Mechanize agent = new Mechanize();
	//		Page page = agent.get("http://www.megafileupload.com/");
	//		Form form = page.forms().findByName("uploadform");
	//		Upload upload = form.getUpload("uploadfile_0");
	//		File file = new File(FormTest.class.getResource("test.html").getFile());
	//		upload.setValue(file);
	//		Page response = form.submit();
	//		assertTrue(response.getUri().startsWith("http://94.75.216.85/cgi-bin/upload.cgi"));
	//	}
}
