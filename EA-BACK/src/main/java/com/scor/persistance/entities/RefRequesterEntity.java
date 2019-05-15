package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "REF_REQUESTER", schema = "dbo", catalog = "EXPAN")
public class RefRequesterEntity implements Serializable {
    private int stcId;
    private String stcName;
    private String stcCode;
    private String stcDescription;

    @Id
    @Column(name = "STC_ID")
    public int getStcId() {
        return stcId;
    }

    public void setStcId(int stcId) {
        this.stcId = stcId;
    }

    @Basic
    @Column(name = "STC_NAME")
    public String getStcName() {
        return stcName;
    }

    public void setStcName(String stcName) {
        this.stcName = stcName;
    }

    @Basic
    @Column(name = "STC_CODE")
    public String getStcCode() {
        return stcCode;
    }

    public void setStcCode(String stcCode) {
        this.stcCode = stcCode;
    }

    @Basic
    @Column(name = "STC_DESCRIPTION")
    public String getStcDescription() {
        return stcDescription;
    }

    public void setStcDescription(String stcDescription) {
        this.stcDescription = stcDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefRequesterEntity that = (RefRequesterEntity) o;
        return stcId == that.stcId &&
                Objects.equals(stcName, that.stcName) &&
                Objects.equals(stcCode, that.stcCode) &&
                Objects.equals(stcDescription, that.stcDescription);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stcId, stcName, stcCode, stcDescription);
    }
}
