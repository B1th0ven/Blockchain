package com.scor.dataProcessing.common;

import java.io.Serializable;

public class RefResultColumn implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1015023361980110120L;
	private String column;
	private String type;
	private int index;
	public RefResultColumn(String column, String type, int index) {
		super();
		this.column = column;
		this.type = type;
		this.index = index;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	
	
	
	
}
