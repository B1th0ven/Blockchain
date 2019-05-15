package com.scor.dataProcessing.models;

import java.util.List;
import java.util.Map;

public class ControlResults {
	private Long threshold;
	private Long number_of_errors;
	List<ControlResult> controlResultsList;
	private String header ;
	private Map<String, List<String>> fileValues;
	
	public Long getThreshold() {
		return threshold;
	}
	public void setThreshold(Long threshold) {
		this.threshold = threshold;
	}
	public Long getNumber_of_errors() {
		return number_of_errors;
	}
	public void setNumber_of_errors(Long number_of_errors) {
		this.number_of_errors = number_of_errors;
	}
	public List<ControlResult> getControlResultsList() {
		return controlResultsList;
	}
	public void setControlResultsList(List<ControlResult> controlResultsList) {
		this.controlResultsList = controlResultsList;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public ControlResults(Long threshold, Long number_of_errors, List<ControlResult> controlResultsList,
			String header,Map<String, List<String>> fileValues) {
		super();
		this.threshold = threshold;
		this.number_of_errors = number_of_errors;
		this.controlResultsList = controlResultsList;
		this.header = header;
		this.fileValues = fileValues;
	}
	public Map<String, List<String>> getFileValues() {
		return fileValues;
	}
	public void setFileValues(Map<String, List<String>> fileValues) {
		this.fileValues = fileValues;
	}
	
	

	

	
	
}
