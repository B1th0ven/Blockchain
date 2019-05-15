package com.scor.persistance.entities;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "EA_FILES", schema = "dbo", catalog = "EXPAN")
public class EaFilesEntity implements Serializable {
	private int eafId;
	private String eafFileType;
	private String eafName;
	private String eafLink;
	private String eafHeader;
	private String eafIgnored;
	private RefUserEntity eafSubmitter;
	private Date eafprivacyDate;
	private String eafDataRestriction;
	private Date eafDataDeletion;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EAF_ID")
	public int getEafId() {
		return eafId;
	}

	public void setEafId(int eafId) {
		this.eafId = eafId;
	}

	@Basic
	@Column(name = "EAF_FILE_TYPE")
	public String getEafFileType() {
		return eafFileType;
	}

	public void setEafFileType(String eafFileType) {
		this.eafFileType = eafFileType;
	}

	@Basic
	@Column(name = "EAF_NAME")
	public String getEafName() {
		return eafName;
	}

	public void setEafName(String eafName) {
		this.eafName = eafName;
	}

	@Basic
	@Column(name = "EAF_LINK")
	public String getEafLink() {
		return eafLink;
	}

	public void setEafLink(String eafLink) {
		this.eafLink = eafLink;
	}

	@Basic
	@Column(name = "EAF_HEADER")
	public String getEafHeader() {
		return eafHeader;
	}

	public void setEafHeader(String eafHeader) {
		this.eafHeader = eafHeader;
	}

	@Basic
	@Column(name = "EAF_IGNORED")
	public String getEafIgnored() {
		return eafIgnored;
	}

	public void setEafIgnored(String eafIgnored) {
		this.eafIgnored = eafIgnored;
	}

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "EAF_PRIVACY_SUBMITTER", referencedColumnName = "RU_ID")
	public RefUserEntity getEafSubmitter() {
		return eafSubmitter;
	}

	public void setEafSubmitter(RefUserEntity eafSubmitter) {
		this.eafSubmitter = eafSubmitter;
	}

	@Basic
	@Column(name = "EAF_PRIVACY_DATE")
	public Date getEafprivacyDate() {
		return eafprivacyDate;
	}

	public void setEafprivacyDate(Date eafprivacyDate) {
		this.eafprivacyDate = eafprivacyDate;
	}

	@Basic
	@Column(name = "EAF_DATA_RESTRICTION")
	public String getEafDataRestriction() {
		return eafDataRestriction;
	}

	public void setEafDataRestriction(String eafDataRestriction) {
		this.eafDataRestriction = eafDataRestriction;
	}

	@Basic
	@Column(name = "EAF_DATE_DELETION")
	public Date getEafDataDeletion() {
		return eafDataDeletion;
	}

	public void setEafDataDeletion(Date eafDataDeletion) {
		this.eafDataDeletion = eafDataDeletion;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EaFilesEntity that = (EaFilesEntity) o;
		return eafId == that.eafId && Objects.equals(eafFileType, that.eafFileType)
				&& Objects.equals(eafName, that.eafName) && Objects.equals(eafLink, that.eafLink)
				&& Objects.equals(eafHeader, that.eafHeader) && Objects.equals(eafIgnored, that.eafIgnored);
	}

	@Override
	public int hashCode() {

		return Objects.hash(eafId, eafFileType, eafName, eafLink, eafHeader, eafIgnored);
	}
}
