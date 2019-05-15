package com.scor.dataProcessing.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AffectedColumn implements Serializable{

	private String name;
	private int errorsNumber;
	ArrayList<String> examples;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getErrorsNumber() {
		return errorsNumber;
	}
	public void setErrorsNumber(int errorsNumber) {
		this.errorsNumber = errorsNumber;
	}
	public ArrayList<String> getExamples() {
		return examples;
	}
	public void setExamples(ArrayList<String> examples) {
		this.examples = examples;
	}
	public AffectedColumn(String name, int errorsNumber, ArrayList<String> examples) {
		super();
		this.name = name;
		this.errorsNumber = errorsNumber;
		this.examples = examples;
	}
	public AffectedColumn() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}
