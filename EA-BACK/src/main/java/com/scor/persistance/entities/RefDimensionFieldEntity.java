package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "REF_DIMENSION_FIELD", schema = "dbo", catalog = "EXPAN")
public class RefDimensionFieldEntity implements Serializable {
    private int rdfId;
    private String rdfCode;
    private String rdfName;

    @Id
    @Column(name = "RDF_ID")
    public int getRdfId() {
        return rdfId;
    }

    public void setRdfId(int rdfId) {
        this.rdfId = rdfId;
    }

    @Basic
    @Column(name = "RDF_CODE")
    public String getRdfCode() {
        return rdfCode;
    }

    public void setRdfCode(String rdfCode) {
        this.rdfCode = rdfCode;
    }

    @Basic
    @Column(name = "RDF_NAME")
    public String getRdfName() {
        return rdfName;
    }

    public void setRdfName(String rdfName) {
        this.rdfName = rdfName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefDimensionFieldEntity that = (RefDimensionFieldEntity) o;
        return rdfId == that.rdfId &&
                Objects.equals(rdfCode, that.rdfCode) &&
                Objects.equals(rdfName, that.rdfName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rdfId, rdfCode, rdfName);
    }
}
