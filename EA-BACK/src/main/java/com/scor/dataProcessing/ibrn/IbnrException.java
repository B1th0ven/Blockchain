package com.scor.dataProcessing.ibrn;

public class IbnrException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2311421956594852018L;
	
	private int code;
	private String message;
	
	public IbnrException() {
		super();
	}
	
	public IbnrException(int code, String message) {
		super();
		this.setCode(code);
		this.setMessage(message);
	}
	
	public IbnrException(int code, String message, Throwable throwable) {
		super(throwable);
		this.setCode(code);
		this.setMessage(message);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
