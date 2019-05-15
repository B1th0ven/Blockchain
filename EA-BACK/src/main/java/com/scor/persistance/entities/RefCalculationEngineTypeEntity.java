package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "REF_CALCULATION_ENGINE_TYPE", schema = "dbo", catalog = "EXPAN")
public class RefCalculationEngineTypeEntity implements Serializable {
    private int rcetId;
    private String rcetName;
    private String rcetDescription;

    @Id
    @Column(name = "RCET_ID")
    public int getRcetId() {
        return rcetId;
    }

    public void setRcetId(int rcetId) {
        this.rcetId = rcetId;
    }

    @Basic
    @Column(name = "RCET_NAME")
    public String getRcetName() {
        return rcetName;
    }

    public void setRcetName(String rcetName) {
        this.rcetName = rcetName;
    }

    @Basic
    @Column(name = "RCET_DESCRIPTION")
    public String getRcetDescription() {
        return rcetDescription;
    }

    public void setRcetDescription(String rcetDescription) {
        this.rcetDescription = rcetDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefCalculationEngineTypeEntity that = (RefCalculationEngineTypeEntity) o;
        return rcetId == that.rcetId &&
                Objects.equals(rcetName, that.rcetName) &&
                Objects.equals(rcetDescription, that.rcetDescription);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rcetId, rcetName, rcetDescription);
    }
}
