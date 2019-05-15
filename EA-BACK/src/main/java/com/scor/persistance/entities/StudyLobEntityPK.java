package com.scor.persistance.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class StudyLobEntityPK implements Serializable {
    private int slobStId;
    private int slobRlobId;

    @Column(name = "SLOB_ST_ID")
    @Id
    public int getSlobStId() {
        return slobStId;
    }

    public void setSlobStId(int slobStId) {
        this.slobStId = slobStId;
    }

    @Column(name = "SLOB_RLOB_ID")
    @Id
    public int getSlobRlobId() {
        return slobRlobId;
    }

    public void setSlobRlobId(int slobRlobId) {
        this.slobRlobId = slobRlobId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyLobEntityPK that = (StudyLobEntityPK) o;
        return slobStId == that.slobStId &&
                slobRlobId == that.slobRlobId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(slobStId, slobRlobId);
    }
}
