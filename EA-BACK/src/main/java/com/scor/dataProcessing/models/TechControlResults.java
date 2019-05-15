package com.scor.dataProcessing.models;

import java.util.List;

public class TechControlResults {
	private Long threshold;
	private Long number_of_errors;
	List<TechControlResult> controlResultsList;
	private String header;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

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

	public List<TechControlResult> getControlResultsList() {
		return controlResultsList;
	}

	public void setControlResultsList(List<TechControlResult> controlResultsList) {
		this.controlResultsList = controlResultsList;
	}

	public TechControlResults(Long threshold, Long number_of_errors, List<TechControlResult> controlResultsList, String header) {
		this.threshold = threshold;
		this.number_of_errors = number_of_errors;
		this.controlResultsList = controlResultsList;
		this.header = header;
	}
}
