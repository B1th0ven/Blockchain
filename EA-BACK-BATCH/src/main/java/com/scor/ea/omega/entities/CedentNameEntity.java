package com.scor.ea.omega.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "REF_CEDENT_NAME", schema = "dbo", catalog = "EXPAN")
public class CedentNameEntity {


	@Id
	@Column(name="RCN_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rcnId;
	
	@ManyToOne
	@JoinColumn(name = "RCN_RPG_ID")
	private ParentGroupEntity parentGroup;
	
	@Column(name = "RCN_SHORT_NAME")
	private String name;
	
	@Column(name = "RCN_DESCRIPTION")
	private String description;
	
	@Column(name = "RCN_COMMERCIAL_CLIENT_NAME")
	private String commercialClientName;

	@Column(name = "RCN_CODE")
	private String code;


	@ManyToOne
	@JoinColumn(name = "RCN_RC_ID", referencedColumnName = "RC_ID")
	private CountryEntity country;



	public int getRcnId() {
		return rcnId;
	}

	public void setRcnId(int id) {
		this.rcnId = id;
	}

	public ParentGroupEntity getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(ParentGroupEntity parentGroup) {
		this.parentGroup = parentGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCommercialClientName() {
		return commercialClientName;
	}

	public void setCommercialClientName(String commercialClientName) {
		this.commercialClientName = commercialClientName;
	}

	public CountryEntity getCountry() {
		return country;
	}

	public void setCountry(CountryEntity country) {
		this.country = country;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
