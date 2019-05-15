package com.scor.taskManager.models;

import java.util.ArrayList;
import java.util.List;



public class Email {
	
	

	private List<String> to;
//	private List<String> cc ; 
	private String subject;
	private String body;
	


	public Email() {
		this.to= new ArrayList<String>() ; 
//		this.cc= new ArrayList<String>() ; 
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

//	public List<String> getCc() {
//		return cc;
//	}
//
//	public void setCc(List<String> cc) {
//		this.cc = cc;
//	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

//	public void addCc(String email) {
//		this.cc.add(email) ;
//	}
//	
	public void addTo(String email) {
		this.to.add(email);
	}


}
