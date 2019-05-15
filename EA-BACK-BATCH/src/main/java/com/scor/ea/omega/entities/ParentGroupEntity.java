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
@Table(name = "REF_PARENT_GROUP", schema = "dbo", catalog = "EXPAN")
public class ParentGroupEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RPG_ID")
	private int id;

	@Column(name = "RPG_NAME")
	private String name;

	@Column(name = "RPG_DESCRIPTION")
	private String description;
	
	@Column(name = "RPG_CODE")
	private String code;
	
	@ManyToOne
	@JoinColumn(name = "RPG_PARENT_ID")
	private ParentGroupEntity ultimateGroup;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ParentGroupEntity getUltimateGroup() {
		return ultimateGroup;
	}

	public void setUltimateGroup(ParentGroupEntity ultimateGroup) {
		this.ultimateGroup = ultimateGroup;
	}
	
	

}
