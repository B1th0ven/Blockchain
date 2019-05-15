package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "REF_LOB", schema = "dbo", catalog = "EXPAN")
public class RefLobEntity  implements Serializable {
    private int rlobId;
    private String rlobName;

    @Id
    @Column(name = "RLOB_ID")
    public int getRlobId() {
        return rlobId;
    }

    public void setRlobId(int rlobId) {
        this.rlobId = rlobId;
    }

    @Basic
    @Column(name = "RLOB_NAME")
    public String getRlobName() {
        return rlobName;
    }

    public void setRlobName(String rlobName) {
        this.rlobName = rlobName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefLobEntity that = (RefLobEntity) o;
        return rlobId == that.rlobId &&
                Objects.equals(rlobName, that.rlobName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rlobId, rlobName);
    }
}
