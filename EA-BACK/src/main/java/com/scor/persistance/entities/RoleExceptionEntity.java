package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_ROLE_EXCEPTION", schema = "dbo", catalog = "EXPAN")
public class RoleExceptionEntity implements Serializable{

    private int ureId;
    private RefUserEntity ureRuId;
    private StudyEntity ureStId;
    private String ureRole;
    private String ureDefaultRole;
    private boolean isDefault = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "URE_ID")
    public int getUreId() {
        return ureId;
    }

    public void setUreId(int ureId) {
        this.ureId = ureId;
    }



    @Basic
    @Column(name = "URE_ROLE")
    public String getUreRole() {
        return ureRole;
    }

    public void setUreRole(String ureRole) {
        this.ureRole = ureRole;
    }


    @OneToOne
    @JoinColumn(name = "URE_RU_ID", referencedColumnName = "RU_ID")
    public RefUserEntity getUreRuId() {
        return ureRuId;
    }

    public void setUreRuId(RefUserEntity ureRuId) {
        this.ureRuId = ureRuId;
    }

    @OneToOne
    @JoinColumn(name = "URE_ST_ID", referencedColumnName = "ST_ID")
    public StudyEntity getUreStId() {
        return ureStId;
    }

    public void setUreStId(StudyEntity ureStId) {
        this.ureStId = ureStId;
    }

    @Transient()
    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
    @Transient()
	public String getUreDefaultRole() {
		return ureDefaultRole;
	}

	public void setUreDefaultRole(String ureDefaultRole) {
		this.ureDefaultRole = ureDefaultRole;
	}

    



}
