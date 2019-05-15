package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "REF_MARKET", schema = "dbo", catalog = "EXPAN")
public class RefMarketEntity  implements Serializable {
    private int rmId;
    private String rmName;
    private String rmDescription;

    @Id
    @Column(name = "RM_ID")
    public int getRmId() {
        return rmId;
    }

    public void setRmId(int rmId) {
        this.rmId = rmId;
    }

    @Basic
    @Column(name = "RM_NAME")
    public String getRmName() {
        return rmName;
    }

    public void setRmName(String rmName) {
        this.rmName = rmName;
    }

    @Basic
    @Column(name = "RM_DESCRIPTION")
    public String getRmDescription() {
        return rmDescription;
    }

    public void setRmDescription(String rmDescription) {
        this.rmDescription = rmDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefMarketEntity that = (RefMarketEntity) o;
        return rmId == that.rmId &&
                Objects.equals(rmName, that.rmName) &&
                Objects.equals(rmDescription, that.rmDescription);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rmId, rmName, rmDescription);
    }
}
