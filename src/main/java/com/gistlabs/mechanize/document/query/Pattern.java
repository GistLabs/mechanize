package com.gistlabs.mechanize.document.query;

/**
 * Represents a pattern being either a regular expression or a string and is matched towards a 
 * string or to the lower case version of the string.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class Pattern {
	private final String value;
	private final boolean isRegularExpression;
	private boolean isCompareLowerCase = false;
	
	public Pattern(String value, boolean isRegularExpression) {
		this.value = value;
		this.isRegularExpression = isRegularExpression;
	}
	
	public String getValue() {
		return value;
	}
	
	public boolean isRegularExpression() {
		return isRegularExpression;
	}
	
	public boolean isCompareLowerCase() {
		return isCompareLowerCase;
	}
	
	public Pattern setCompareLowerCase(boolean isCompareLowerCase) {
		this.isCompareLowerCase = isCompareLowerCase;
		return this;
	}
	
	public boolean doesMatch(String string) {
		if(string != null) {
			if(isRegularExpression) 
				return isCompareLowerCase ? string.toLowerCase().matches(value) : string.matches(value);
			else 
				return isCompareLowerCase ? string.equalsIgnoreCase(value) : string.equals(value);
		}
		else
			return false;
	}
	
	public String toString() {
		if(isRegularExpression) 
			return isCompareLowerCase ? "caseInsensitive(regEx(" + value + "))" : "regEx(" + value + ")"; 
		else 
			return isCompareLowerCase ? "caseInsensitive(" + value + ")" : value;
	}
}