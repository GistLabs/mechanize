package com.gistlabs.mechanize.document.query;

/** Query part in brackets. */
class InBracketsQueryPart extends QueryPart {
	private final AbstractQuery<?> query;
	
	public InBracketsQueryPart(boolean isAnd, AbstractQuery<?> query) {
		super(isAnd, (String [])null, null);
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