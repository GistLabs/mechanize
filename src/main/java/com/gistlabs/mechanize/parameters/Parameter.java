package com.gistlabs.mechanize.parameters;

import java.util.ArrayList;
import java.util.List;

public class Parameter {
	private final String name;
	private final List<String> values = new ArrayList<String>();
	
	public Parameter(String name, String value) {
		this.name = name;
		this.values.add(value);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isSingleValue() {
		return values.size() == 1;
	}
	
	public void addValue(String value) {
		if(!values.contains(value))
			values.add(value);
	}
	
	public String getValue() {
		return values.get(0);
	}
	
	public List<String> getValues() {
		return values;
	}
}