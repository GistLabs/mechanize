/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.form;

import static com.gistlabs.mechanize.util.css.CSSHelper.byIdOrName;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.documentElements.AbstractDocumentElement;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.parameters.Parameters;
import com.gistlabs.mechanize.requestor.RequestBuilder;

/** 
 * Represents a form. 
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @since 2012-09-12
 */
public class Form extends AbstractDocumentElement implements Iterable<FormElement> {
	private List<FormElement> elements = new ArrayList<FormElement>();
	
	public Form(Resource page, Node formNode) {
		super(page, formNode);
		analyse();
	}

	private void analyse() {
		List<? extends Node> nodes = getNode().findAll("input, textarea, select");
		for(Node node : nodes) {
			FormElement formElement = newFormElement(node);
			if(formElement != null)
				this.elements.add(formElement);
		}
	}
	
	private FormElement newFormElement(Node node) {
		if(node.getName().equalsIgnoreCase("input") && node.hasAttribute("type")) {
			String type = node.getAttribute("type");
			if(type.equalsIgnoreCase("text"))
				return new Text(this, node);
			else if(type.equalsIgnoreCase("search"))
				return new Search(this, node);
			else if(type.equalsIgnoreCase("email"))
				return new Email(this, node);
			else if(type.equalsIgnoreCase("password"))
				return new Password(this, node);
			else if(type.equalsIgnoreCase("file"))
				return new Upload(this, node);
			else if(type.equalsIgnoreCase("hidden"))
				return new Hidden(this, node);
			else if(type.equalsIgnoreCase("submit"))
				return new SubmitButton(this, node);
			else if(type.equalsIgnoreCase("image"))
				return new SubmitImage(this, node);
			else if(type.equalsIgnoreCase("checkbox"))
				return new Checkbox(this, node);
			else if(type.equalsIgnoreCase("radio"))
				return new RadioButton(this, node);
			else
				return new FormElement(this, node);
		}
		else if(node.getName().equalsIgnoreCase("input") && !node.hasAttribute("type")) {
			//Used to map input without type to text field (as required by google.com search form)
			return new Text(this, node);
		}
		else if(node.getName().equalsIgnoreCase("textarea"))
			return new TextArea(this, node);
		else if(node.getName().equalsIgnoreCase("select"))
			return new Select(this, node);
		else
			return null;
	}
	
	public boolean isDoPost() {
		return getNode().hasAttribute("method") && getNode().getAttribute("method").equalsIgnoreCase("post");
	}
	
	public boolean isMultiPart() {
		return getNode().hasAttribute("enctype") && getNode().getAttribute("enctype").equalsIgnoreCase("multipart/form-data");
	}
	
	public String getUri() {
		String uri = null;
		if(getNode().hasAttribute("action"))
			uri = absoluteUrl(getNode().getAttribute("action"));
		else 
			uri = getResource().getUri().toString();
		return uri;
	}
	
	/** Returns the element with the given name (case sensitive) or null. */
	public FormElement get(String nameOrId) {
		return find(byIdOrName(nameOrId));
	}
	
	public FormElement find(String csss) {
		for(FormElement element : elements)
			if(element.matches(csss))
				return element;
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T find(String csss, Class<T> clazz) {
		for(FormElement element : elements) {
			if ((clazz==null || clazz.isInstance(element)) && element.matches(csss)) {
				return (T)element;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> T find(String csss, String value, Class<T> clazz) {
		for(FormElement element : elements) {
			if ((clazz==null || clazz.isInstance(element)) && element.matches(csss)) {
				if (value==null || value.equals(element.getValue())) {
					return (T)element;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(String csss, Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		for(FormElement element : elements) {
			if ((clazz==null || clazz.isInstance(element)) && element.matches(csss)) {
				result.add((T)element);
			}
		}
		return result;
	}

	public Email findEmail(String csss) {
		return find(csss, Email.class);
	}

	public Select findSelect(String csss) {
		return find(csss, Select.class);
	}

	public Checkbox findCheckbox(String csss) {
		return find(csss, Checkbox.class);
	}

	public Search findSearch(String csss) {
		return find(csss, Search.class);
	}

	public SubmitImage findSubmitImage(String csss) {
		return find(csss, SubmitImage.class);
	}

	public Upload findUpload(String csss) {
		return find(csss, Upload.class);
	}

	public RadioButton findRadioButton(String csss) {
		return find(csss, RadioButton.class);
	}
	
	public Checkbox findCheckbox(String csss, String value) {
		return find(csss, value, Checkbox.class);
	}
	
	public RadioButton findRadioButton(String csss, String value) {
		return find(csss, value, RadioButton.class);
	}

	public TextArea findTextArea(String csss) {
		return find(csss, TextArea.class);
	}

	public SubmitButton findSubmitButton(String csss) {
		return find(csss, SubmitButton.class);
	}
	

	public Checkbox getCheckbox(String nameOrId, String value) {
		return get(nameOrId, value, Checkbox.class);
	}

	public RadioButton getRadioButton(String nameOrId) {
		return get(nameOrId, RadioButton.class);
	}
	
	public RadioButton getRadioButton(String nameOrId, String value) {
		return get(nameOrId, value, RadioButton.class);
	}
	
	public List<RadioButton> getRadioButtons(String nameOrId) {
		return getAll(nameOrId, RadioButton.class);
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
	
	public <T extends Resource> T submit() {
		return submit(this, composeParameters(null, null, 0, 0));
	}

	public <T extends Resource> T submit(SubmitButton button) {
		return submit(this, composeParameters(button, null, 0, 0));
	}

	public <T extends Resource> T submit(SubmitImage image, int x, int y) {
		return submit(this, composeParameters(null, image, x, y));
	} 

	/** Returns the page object received as response to the form submit action. */
	@SuppressWarnings("unchecked")
	private <T extends Resource> T submit(Form form, Parameters parameters) {
		RequestBuilder<Resource> request = doRequest(form.getUri()).set(parameters);
		boolean doPost = form.isDoPost();
		boolean multiPart = form.isMultiPart();
		if(doPost && multiPart) {
			request.multiPart();
			addFiles(request, form);
		}
		return (T) (doPost ? request.post() : request.get()); 
	}

	private void addFiles(RequestBuilder<Resource> request, Form form) {
		for(FormElement formElement : form) {
			if(formElement instanceof Upload) {
				Upload upload = (Upload)formElement;
				if (upload.hasValue()) {
					File file = upload.hasFileValue() ? upload.getFileValue() : new File(upload.getValue());
					request.set(upload.getName(), file);					
				}
			}
		}
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
