package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "REF_DATA_SOURCE", schema = "dbo", catalog = "EXPAN")
public class RefDataSourceEntity implements Serializable {
    private int rdsId;
    private String rdsName;
    private String rdsDescription;

    @Id
    @Column(name = "RDS_ID")
    public int getRdsId() {
        return rdsId;
    }

    public void setRdsId(int rdsId) {
        this.rdsId = rdsId;
    }

    @Basic
    @Column(name = "RDS_NAME")
    public String getRdsName() {
        return rdsName;
    }

    public void setRdsName(String rdsName) {
        this.rdsName = rdsName;
    }

    @Basic
    @Column(name = "RDS_DESCRIPTION")
    public String getRdsDescription() {
        return rdsDescription;
    }

    public void setRdsDescription(String rdsDescription) {
        this.rdsDescription = rdsDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefDataSourceEntity that = (RefDataSourceEntity) o;
        return rdsId == that.rdsId &&
                Objects.equals(rdsName, that.rdsName) &&
                Objects.equals(rdsDescription, that.rdsDescription);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rdsId, rdsName, rdsDescription);
    }
}
