package com.scor.ea.omega.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "REF_COUNTRY", schema = "dbo", catalog = "EXPAN")
public class CountryEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name ="RC_ID")
	private int rcId;
	
	@Column(name ="RC_CODE")
	private String code;
	
	@Column(name ="RC_NAME")
	private String name;
	
	public int getRcId() {
		return rcId;
	}
	public void setRcId(int id) {
		this.rcId = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
