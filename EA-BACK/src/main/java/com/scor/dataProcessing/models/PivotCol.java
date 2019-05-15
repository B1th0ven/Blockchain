package com.scor.dataProcessing.models;

import java.io.Serializable;
import java.util.List;

public class PivotCol implements Serializable{

	private String name;
	private String type;
	private boolean mandatory;
	private List<String> possiblesValues;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public List<String> getPossiblesValues() {
		return possiblesValues;
	}
	public void setPossiblesValues(List<String> possiblesValues) {
		this.possiblesValues = possiblesValues;
	}
	public PivotCol(String name, String type, boolean mandatory, List<String> possiblesValues) {
		super();
		this.name = name;
		this.type = type;
		this.mandatory = mandatory;
		this.possiblesValues = possiblesValues;
	}

	

}
