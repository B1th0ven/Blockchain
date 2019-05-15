package com.scor.dataProcessing.common;

public class RefCountryScope {

	private String countryCode;
	private String countryName;
	private String regionalScope;
	private String localScope;

	public RefCountryScope(String countryCode, String countryName, String localScope, String regionalScope) {
		super();
		this.countryCode = countryCode;
		this.countryName = countryName;
		this.regionalScope = regionalScope;
		this.localScope = localScope;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getRegionalScope() {
		return regionalScope;
	}

	public void setRegionalScope(String regionalScope) {
		this.regionalScope = regionalScope;
	}

	public String getLocalScope() {
		return localScope;
	}

	public void setLocalScope(String localScope) {
		this.localScope = localScope;
	}

}
