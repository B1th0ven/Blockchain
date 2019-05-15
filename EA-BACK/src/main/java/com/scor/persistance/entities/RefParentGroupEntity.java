package com.scor.persistance.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "REF_PARENT_GROUP", schema = "dbo", catalog = "EXPAN")
public class RefParentGroupEntity implements Serializable {
    private Integer rpgId;
    private String rpgName;
    private String rpgDescription;
    private String rpgCode;
    private RefParentGroupEntity rpgParentId;
    private RefUltimateGroupEntity ultimateGroup;
    private String flag;

    @Id
    @Column(name = "RPG_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getRpgId() {
        return rpgId;
    }

    public void setRpgId(Integer rpgId) {
        this.rpgId = rpgId;
    }

    @Basic
    @Column(name = "RPG_NAME")
    public String getRpgName() {
        return rpgName;
    }

    public void setRpgName(String rpgName) {
        this.rpgName = rpgName;
    }

    @Basic
    @Column(name = "RPG_DESCRIPTION")
    public String getRpgDescription() {
        return rpgDescription;
    }

    public void setRpgDescription(String rpgDescription) {
        this.rpgDescription = rpgDescription;
    }
    
    @Basic
    @Column(name = "RPG_CODE")
    public String getRpgCode() {
		return rpgCode;
	}

	public void setRpgCode(String rpgCode) {
		this.rpgCode = rpgCode;
	}

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RPG_RUG_ID")
    public RefUltimateGroupEntity getUltimateGroup() {
		return ultimateGroup;
	}

	public void setUltimateGroup(RefUltimateGroupEntity ultimateGroup) {
		this.ultimateGroup = ultimateGroup;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefParentGroupEntity that = (RefParentGroupEntity) o;
        return rpgId == that.rpgId &&
                Objects.equals(rpgName, that.rpgName) &&
                Objects.equals(rpgDescription, that.rpgDescription);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rpgId, rpgName, rpgDescription);
    }

    @Transient
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RPG_PARENT_ID")
	public RefParentGroupEntity getRpgParentId() {
		return rpgParentId;
	}

	public void setRpgParentId(RefParentGroupEntity rpgParentId) {
		this.rpgParentId = rpgParentId;
	}
	
	
    
    
}
