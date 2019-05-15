package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "REF_CEDENT_NAME", schema = "dbo", catalog = "EXPAN")
public class RefCedentNameEntity  implements Serializable {
	private Integer rcnId;
	private Integer rcnRpgId;
	private Integer rcnRcId;
	private RefCountryEntity refCountryById;

	private String rcnShortName;
	private String rcnLongName;
	private String rcnCode;

	@Id
	@Column(name = "RCN_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getRcnId() {
		return rcnId;
	}

	public void setRcnId(Integer rcnId) {
		this.rcnId = rcnId;
	}

	@Basic
	@Column(name = "RCN_RPG_ID")
	public Integer getRcnRpgId() {
		return rcnRpgId;
	}

	public void setRcnRpgId(Integer rcnRpgId) {
		this.rcnRpgId = rcnRpgId;
	}

	@Basic
	@Column(name = "RCN_RC_ID")
	public Integer getRcnRcId() {
		return rcnRcId;
	}

	public void setRcnRcId(Integer rcnRcId) {
		this.rcnRcId = rcnRcId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RefCedentNameEntity that = (RefCedentNameEntity) o;
		return rcnId == that.rcnId && Objects.equals(rcnRpgId, that.rcnRpgId)
				&& Objects.equals(rcnShortName, that.rcnShortName) && Objects.equals(rcnRcId, that.rcnRcId);
	}

	@Override
	public int hashCode() {

		return Objects.hash(rcnId, rcnRpgId, rcnShortName, rcnRcId);
	}

	@ManyToOne
	@JoinColumn(name = "RCN_RC_ID", insertable = false, updatable = false)
	public RefCountryEntity getRefCountryById() {
		return refCountryById;
	}

	public void setRefCountryById(RefCountryEntity refCountryById) {
		this.refCountryById = refCountryById;
	}

	@Basic
	@Column(name = "RCN_SHORT_NAME")
	public String getRcnShortName() {
		return rcnShortName;
	}

	public void setRcnShortName(String rcnShortName) {
		this.rcnShortName = rcnShortName;
	}

	@Basic
	@Column(name = "RCN_LONG_NAME")
	public String getRcnLongName() {
		return rcnLongName;
	}

	public void setRcnLongName(String rcnLongName) {
		this.rcnLongName = rcnLongName;
	}

	@Basic
	@Column(name = "RCN_CODE")
	public String getRcnCode() {
		return rcnCode;
	}

	public void setRcnCode(String rcnCode) {
		this.rcnCode = rcnCode;
	}

}
