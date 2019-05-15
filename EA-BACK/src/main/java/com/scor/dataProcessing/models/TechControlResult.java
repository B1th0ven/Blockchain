package com.scor.dataProcessing.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TechControlResult implements Serializable{


	private String control;
	List<AffectedColumn> affectedColumns;

	public TechControlResult() {
		this.control="" ;
		this.affectedColumns = new ArrayList<>() ;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public List<AffectedColumn> getAffectedColumns() {
		return affectedColumns;
	}

	public void setAffectedColumns(List<AffectedColumn> affectedColumns) {
		this.affectedColumns = affectedColumns;
	}

	public TechControlResult(String control, List<AffectedColumn> affectedColumns) {
		super();
		this.control = control;
		this.affectedColumns = affectedColumns;
	}

}
