package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "REF_COUNTRY", schema = "dbo", catalog = "EXPAN")
public class RefCountryEntity implements Serializable {
    private int rcId;
    private String rcCode;
    private String rcName;

    @Id
    @Column(name = "RC_ID")
    public int getRcId() {
        return rcId;
    }

    public void setRcId(int rcId) {
        this.rcId = rcId;
    }

    @Basic
    @Column(name = "RC_CODE")
    public String getRcCode() {
        return rcCode;
    }

    public void setRcCode(String rcCode) {
        this.rcCode = rcCode;
    }

    @Basic
    @Column(name = "RC_NAME")
    public String getRcName() {
        return rcName;
    }

    public void setRcName(String rcName) {
        this.rcName = rcName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefCountryEntity that = (RefCountryEntity) o;
        return rcId == that.rcId &&
                Objects.equals(rcCode, that.rcCode) &&
                Objects.equals(rcName, that.rcName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rcId, rcCode, rcName);
    }
}
