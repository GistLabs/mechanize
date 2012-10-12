package com.gistlabs.mechanize.query;

import com.gistlabs.mechanize.query.HtmlQuery.Pattern;

class QueryPart {
	private final boolean isAnd;
	private final Pattern pattern;
	private final Selector selector;
	
	public QueryPart(boolean isAnd, Pattern pattern, Selector selector) {
		this.isAnd = isAnd;
		this.pattern = pattern;
		this.selector = selector;
	}
	
	@Override
	public String toString() {
		return "<" + pattern.toString() + "," + selector.toString() + ">";
	}
	
	public boolean matches(QueryStrategy queryStrategy, Object object) {
		boolean isMatch = false;

		if(selector.includesAny()) {
			for(String attribute : queryStrategy.getAttributeNames(object))
				if(pattern.doesMatch(queryStrategy.getAttributeValue(object, attribute)))
					isMatch = true;
		}

		if(selector.includesClass() || selector.includesAny()) {
			String value = queryStrategy.getAttributeValue(object, QueryStrategy.SPECIAL_ATTRIBUTE_CLASS_NAMES);
			if(value != null) {
				for(String className : value.split("\\,"))
					if(pattern.doesMatch(className))
						isMatch = true;
			}
		}
		
		if(!isMatch && selector.includesId())
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, "id"));
		
		if(!isMatch && selector.includesName()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, "name"));

		if(!isMatch && selector.includesTag()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, QueryStrategy.SPECIAL_ATTRIBUTE_TAG_NAME));
		
		if(!isMatch && selector.includesAction()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, "action"));
		
		if(!isMatch && selector.includesHRef()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, "href"));
		
		if(!isMatch && selector.includesSrc()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, "src"));

		if(!isMatch && selector.includesTitle()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, "title"));
		
		if(!isMatch && selector.includesWidth()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, "width"));

		if(!isMatch && selector.includesHeight()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, "height"));

		if(!isMatch && selector.includesValue()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, "value"));
		
		if(!isMatch && selector.includesType()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, "type"));

		if(!isMatch && selector.includesText()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, QueryStrategy.SPECIAL_ATTRIBUTE_TEXT));

		if(!isMatch && selector.includesInnerHtml()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, QueryStrategy.SPECIAL_ATTRIBUTE_INNER_HTML));

		if(!isMatch && selector.includesHtml()) 
			isMatch = pattern.doesMatch(queryStrategy.getAttributeValue(object, QueryStrategy.SPECIAL_ATTRIBUTE_HTML));
		
		return isMatch; 
	}
	
	public boolean isAnd() {
		return isAnd;
	}
}