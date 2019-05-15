package com.scor.dataProcessing.common;

public class RefMarketScope {

	private String scopeName;
	private String scopeType;
	private String regionalScope;

	public RefMarketScope(String scopeName, String scopeType, String regionalScope) {
		super();
		this.scopeName = scopeName;
		this.scopeType = scopeType;
		this.regionalScope = regionalScope;
	}

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public String getScopeType() {
		return scopeType;
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
	}

	public String getRegionalScope() {
		return regionalScope;
	}

	public void setRegionalScope(String regionalScope) {
		this.regionalScope = regionalScope;
	}

}
