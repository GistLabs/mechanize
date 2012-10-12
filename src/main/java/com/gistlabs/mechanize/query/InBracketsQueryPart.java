package com.gistlabs.mechanize.query;

/** Query part in brackets. */
class InBracketsQueryPart extends QueryPart {
	private final HtmlQuery query;
	
	public InBracketsQueryPart(boolean isAnd, HtmlQuery query) {
		super(isAnd, null, null);
		this.query = query;
	}
	
	@Override
	public boolean matches(QueryStrategy queryStrategy, Object object) {
		return query.matches(queryStrategy, object);
	}
	
	@Override
	public String toString() {
		return "(" + query.toString() + ")";
	}
}