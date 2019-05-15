package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "REF_SCOPE", schema = "dbo", catalog = "EXPAN")
public class RefScopeEntity implements Serializable {
    private int rsId;
    private String rsName;
    private String rsType;

    @Id
    @Column(name = "RS_ID")
    public int getRsId() {
        return rsId;
    }

    public void setRsId(int rsId) {
        this.rsId = rsId;
    }

    @Basic
    @Column(name = "RS_NAME")
    public String getRsName() {
        return rsName;
    }

    public void setRsName(String rsName) {
        this.rsName = rsName;
    }

    @Basic
    @Column(name = "RS_TYPE")
    public String getRsType() {
        return rsType;
    }

    public void setRsType(String rsType) {
        this.rsType = rsType;
    }
}
