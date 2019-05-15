package com.scor.persistance.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONFIGURATION", schema = "dbo", catalog = "EXPAN")
public class ConfigurationEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -490067097063738336L;
	
	private int id;
	private int globalVersion;
	private int sasVersion;
	private int eaVersion;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EA_ID")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Basic
    @Column(name = "EA_GLOBAL_VERSION")	
	public int getGlobalVersion() {
		return globalVersion;
	}
	public void setGlobalVersion(int globalVersion) {
		this.globalVersion = globalVersion;
	}
	
	@Basic
    @Column(name = "SAS_VERSION")
	public int getSasVersion() {
		return sasVersion;
	}
	public void setSasVersion(int sasVersion) {
		this.sasVersion = sasVersion;
	}
	
	@Basic
    @Column(name = "EA_VERSION")
	public int getEaVersion() {
		return eaVersion;
	}
	public void setEaVersion(int eaVersion) {
		this.eaVersion = eaVersion;
	}
	
	
}
