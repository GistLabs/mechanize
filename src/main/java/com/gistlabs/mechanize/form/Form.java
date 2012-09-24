/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.PageElement;
import com.gistlabs.mechanize.Parameters;
import com.gistlabs.mechanize.Query;
import com.gistlabs.mechanize.QueryBuilder;
import com.gistlabs.mechanize.Util;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** 
 * Represents a form. 
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Form extends PageElement implements Iterable<FormElement> {
	private List<FormElement> elements = new ArrayList<FormElement>();
	
	public Form(Page page, Element formElement) {
		super(page, formElement);
		analyse();
	}

	private void analyse() {
		Elements elements = Util.findElementsByTag(getElement(), new String [] {"input", "textarea", "select"});
		for(Element element : elements) {
			FormElement formElement = newFormElement(element);
			if(formElement != null)
				this.elements.add(formElement);
		}
	}
	
	private FormElement newFormElement(Element element) {
		if(element.tagName().equalsIgnoreCase("input") && element.hasAttr("type")) {
			String type = element.attr("type");
			if(type.equalsIgnoreCase("text"))
				return new Text(this, element);
			else if(type.equalsIgnoreCase("search"))
				return new Search(this, element);
			else if(type.equalsIgnoreCase("password"))
				return new Password(this, element);
			else if(type.equalsIgnoreCase("file"))
				return new Upload(this, element);
			else if(type.equalsIgnoreCase("hidden"))
				return new Hidden(this, element);
			else if(type.equalsIgnoreCase("submit"))
				return new SubmitButton(this, element);
			else if(type.equalsIgnoreCase("image"))
				return new SubmitImage(this, element);
			else if(type.equalsIgnoreCase("checkbox"))
				return new Checkbox(this, element);
			else if(type.equalsIgnoreCase("radio"))
				return new RadioButton(this, element);
			else
				return null;
		}
		else if(element.tagName().equalsIgnoreCase("input") && !element.hasAttr("type")) {
			//Used to map input without type to text field (as required by google.com search form)
			return new Text(this, element);
		}
		else if(element.tagName().equalsIgnoreCase("textarea"))
			return new TextArea(this, element);
		else if(element.tagName().equalsIgnoreCase("select"))
			return new Select(this, element);
		else
			return null;
	}
	
	public boolean isDoPost() {
		return getElement().hasAttr("method") && getElement().attr("method").equalsIgnoreCase("post");
	}
	
	public boolean isMultiPart() {
		return getElement().hasAttr("enctype") && getElement().attr("enctype").equalsIgnoreCase("multipart/form-data");
	}
	
	public String getUri() {
		String uri = null;
		if(getElement().hasAttr("action"))
			uri = getElement().absUrl("action");
		else 
			uri = getPage().getUri().toString();
		return uri;
	}
	
	/** Returns the element with the given name (case sensitive) or null. */
	public FormElement get(String nameOrId) {
		return get(QueryBuilder.byNameOrId(nameOrId));
	}
	
	public FormElement get(Query query) {
		for(FormElement element : elements)
			if(query.matches(element.getElement()))
				return element;
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Query query, Class<T> clazz) {
		for(FormElement element : elements)
			if((clazz == null || clazz.isInstance(element)) && query.matches(element.getElement()))
				return (T)element;
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(Query query, Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		for(FormElement element : elements)
			if((clazz == null || clazz.isInstance(element)) && query.matches(element.getElement()))
				result.add((T)element);
		return result;
	}
	
	public List<FormElement> getAll(Query query) {
		List<FormElement> result = new ArrayList<FormElement>();
		for(FormElement element : elements)
			if(query.matches(element.getElement()))
				result.add(element);
		return result;
	}

	public Text getText(Query query) {
		return get(query, Text.class);
	}
	
	public List<Text> getTextFields(Query query) {
		return getAll(query, Text.class);
	}

	public Search getSearch(Query query) {
		return get(query, Search.class);
	}

	public List<Search> getSearchFields(Query query) {
		return getAll(query, Search.class);
	}

	public TextArea getTextArea(Query query) {
		return get(query, TextArea.class);
	}

	public List<TextArea> getTextAreas(Query query) {
		return getAll(query, TextArea.class);
	}
	
	public Password getPassword(Query query) {
		return get(query, Password.class);
	}
	
	public List<Search> getPasswords(Query query) {
		return getAll(query, Search.class);
	}
	
	public Upload getUpload(Query query) {
		return get(query, Upload.class);
	}

	public List<Search> getUploads(Query query) {
		return getAll(query, Search.class);
	}
	
	public Hidden getHidden(Query query) {
		return get(query, Hidden.class);
	}

	public List<Search> getHiddenFields(Query query) {
		return getAll(query, Search.class);
	}
	
	public SubmitButton getSubmitButton(Query query) {
		return get(query, SubmitButton.class);
	}

	public List<SubmitButton> getSubmitButtons(Query query) {
		return getAll(query, SubmitButton.class);
	}
	
	public SubmitImage getSubmitImage(Query query) {
		return get(query, SubmitImage.class);
	}

	public List<SubmitImage> getSubmitImages(Query query) {
		return getAll(query, SubmitImage.class);
	}
	
	public Checkbox getCheckbox(Query query) {
		return get(query, Checkbox.class);
	}

	public Checkbox getCheckbox(String nameOrId, String value) {
		return get(nameOrId, value, Checkbox.class);
	}

	public List<Checkbox> getCheckboxes(Query query) {
		return getAll(query, Checkbox.class);
	}

	public RadioButton getRadioButton(String nameOrId) {
		return get(nameOrId, RadioButton.class);
	}
	
	public List<RadioButton> getRadioButtons(Query query) {
		return getAll(query, RadioButton.class);
	}
	
	public RadioButton getRadioButton(String nameOrId, String value) {
		return get(nameOrId, value, RadioButton.class);
	}
	
	public List<RadioButton> getRadioButtons(String nameOrId) {
		return getAll(nameOrId, RadioButton.class);
	}
	
	public Select getSelect(Query query) {
		return get(query, Select.class);
	}

	public List<Select> getSelects(Query query) {
		return getAll(query, Select.class);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T get(String nameOrId, Class<T> clazz) {
		for(FormElement element : elements)
			if(clazz.isInstance(element) && (nameOrId.equals(element.getName()) || nameOrId.equals(element.getId())))
				return (T)element;
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> T get(String nameOrId, String value, Class<T> clazz) {
		for(FormElement element : elements)
			if(clazz.isInstance(element) && (nameOrId.equals(element.getName()) || nameOrId.equals(element.getId()))
					&& (value == element.getValue() || (value != null && value.equals(element.getValue()))))
				return (T)element;
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> getAll(String nameOrId, Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		for(FormElement element : elements)
			if(clazz.isInstance(element) && (nameOrId.equals(element.getName()) || nameOrId.equals(element.getId())))
				result.add((T)element);
		return result;
	}
	
	public Page submit() {
		return page.getAgent().submit(this, composeParameters(null, null, 0, 0));
	}

	public Page submit(SubmitButton button) {
		return page.getAgent().submit(this, composeParameters(button, null, 0, 0));
	}

	public Page submit(SubmitImage image, int x, int y) {
		return page.getAgent().submit(this, composeParameters(null, image, x, y));
	} 

	/** Returns the parameters containing all name value params beside submit button, image button, unchecked radio 
	 *  buttons and checkboxes and without Upload information. */
	private Parameters composeParameters(SubmitButton button, SubmitImage image, int x, int y) {
		Parameters params = new Parameters();
		for(FormElement element : this) {
			String name = element.getName();
			String value = element.getValue();
			if(element instanceof Checkable) {
				if(((Checkable)element).isChecked())
					params.add(name, value != null ? value : "");
			}
			else if(element instanceof Select) {
				Select select = (Select)element;
				for(Select.Option option : select.getOptions())
					if(option.isSelected())
						params.add(select.getName(), option.getValue() != null ? option.getValue() : "");
			}
			else if(!(element instanceof SubmitButton) && !(element instanceof SubmitImage) && !(element instanceof Upload)) {
				if(name != null)
					params.add(name, value != null ? value : "");
			}
		}
		if(button != null && button.getName() != null)
			params.add(button.getName(), button.getValue() != null ? button.getValue() : "");
		if(image != null) {
			if(image.getValue() != null && image.getValue().length() > 1)
				params.add(image.getName(), image.getValue());
			params.add(image.getName() + ".x", "" + x);
			params.add(image.getName() + ".y", "" + y);
		}
		return params;
	}
	
	public Iterator<FormElement> iterator() {
		return elements.iterator();
	}
}
