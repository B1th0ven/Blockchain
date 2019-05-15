package com.scor.dataProcessing.models;

import java.io.Serializable;
import java.util.List;

public class NotExecutedDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5899027445797516141L;

	private String control;
	private List<String> field;

	public NotExecutedDto(String control, List<String> field) {
		super();
		this.control = control;
		this.field = field;
	}

	public NotExecutedDto() {
		// TODO Auto-generated constructor stub
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public List<String> getField() {
		return field;
	}

	public void setField(List<String> field) {
		this.field = field;
	}

}
