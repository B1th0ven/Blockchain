package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "STUDY_LOB", schema = "dbo", catalog = "EXPAN")
@IdClass(StudyLobEntityPK.class)
public class StudyLobEntity implements Serializable {
    private int slobStId;
    private int slobRlobId;
    private StudyEntity studyBySlobStId;
    private RefLobEntity refLobBySlobRlobId;

    @Id
    @Column(name = "SLOB_ST_ID")
    public int getSlobStId() {
        return slobStId;
    }

    public void setSlobStId(int slobStId) {
        this.slobStId = slobStId;
    }

    @Id
    @Column(name = "SLOB_RLOB_ID")
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
        StudyLobEntity that = (StudyLobEntity) o;
        return slobStId == that.slobStId &&
                slobRlobId == that.slobRlobId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(slobStId, slobRlobId);
    }

    @ManyToOne
    @JoinColumn(name = "SLOB_ST_ID", referencedColumnName = "ST_ID", nullable = false , insertable = false ,updatable = false)
    public StudyEntity getStudyBySlobStId() {
        return studyBySlobStId;
    }

    public void setStudyBySlobStId(StudyEntity studyBySlobStId) {
        this.studyBySlobStId = studyBySlobStId;
    }

    @ManyToOne
    @JoinColumn(name = "SLOB_RLOB_ID", referencedColumnName = "RLOB_ID", nullable = false , insertable = false ,updatable = false)
    public RefLobEntity getRefLobBySlobRlobId() {
        return refLobBySlobRlobId;
    }

    public void setRefLobBySlobRlobId(RefLobEntity refLobBySlobRlobId) {
        this.refLobBySlobRlobId = refLobBySlobRlobId;
    }
}
