package com.gistlabs.mechanize.document.query;

/** Query part to match every element. */
class EverythingQueryPart extends QueryPart {
	public EverythingQueryPart(boolean isAnd) {
		super(isAnd, (String [])null, null);
	}
	
	@Override
	public boolean matches(QueryStrategy queryStrategy, Object object) {
		return true;
	}
	
	@Override
	public String toString() {
		return "<everything>";
	}
}