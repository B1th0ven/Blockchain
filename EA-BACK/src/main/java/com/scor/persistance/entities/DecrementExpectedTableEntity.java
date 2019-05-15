package com.scor.persistance.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "DECREMENT_EXPECTED_TABLE", schema = "dbo", catalog = "EXPAN")
public class DecrementExpectedTableEntity  implements Serializable {
    private int decetId;
//    private int retDpId;
    private Integer retBase;
    private Integer retTrend;
    private Integer retAdjustment;
    private Integer retPolicy;
    private Integer retBasis;
    private DecrementParametersEntity decrementParametersByRetBase;
    private RefExpectedTableEntity refExpectedTableByRetBase;
    private RefExpectedTableEntity refExpectedTableByRetTrend;
    private RefExpectedTableEntity refExpectedTableByRetAdjustment;
    private RefExpectedTableEntity refExpectedTableByRetPolicy;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DECET_ID")
    public int getDecetId() {
        return decetId;
    }

    public void setDecetId(int decetId) {
        this.decetId = decetId;
    }

    @Basic
    @Column(name = "RET_BASE")
    public Integer getRetBase() {
        return retBase;
    }

    public void setRetBase(Integer retBase) {
        this.retBase = retBase;
    }

    @Basic
    @Column(name = "RET_TREND")
    public Integer getRetTrend() {
        return retTrend;
    }

    public void setRetTrend(Integer retTrend) {
        this.retTrend = retTrend;
    }

    @Basic
    @Column(name = "RET_ADJUSTMENT")
    public Integer getRetAdjustment() {
        return retAdjustment;
    }

    public void setRetAdjustment(Integer retAdjustment) {
        this.retAdjustment = retAdjustment;
    }
    
    @Basic
    @Column(name = "RET_POLICY")
    public Integer getRetPolicy() {
        return retPolicy;
    }

    public void setRetPolicy(Integer retPolicy) {
        this.retPolicy = retPolicy;
    }
    
    @Basic
    @Column(name = "RET_BASIS")
    public Integer getRetBasis() {
        return retBasis;
    }

    public void setRetBasis(Integer retBasis) {
        this.retBasis = retBasis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DecrementExpectedTableEntity that = (DecrementExpectedTableEntity) o;
        return decetId == that.decetId &&
                Objects.equals(retBase, that.retBase) &&
                Objects.equals(retTrend, that.retTrend) &&
                Objects.equals(retAdjustment, that.retAdjustment) &&
                Objects.equals(decrementParametersByRetBase, that.decrementParametersByRetBase) &&
                Objects.equals(refExpectedTableByRetBase, that.refExpectedTableByRetBase) &&
                Objects.equals(refExpectedTableByRetTrend, that.refExpectedTableByRetTrend) &&
                Objects.equals(refExpectedTableByRetAdjustment, that.refExpectedTableByRetAdjustment);
    }

    @Override
    public int hashCode() {

        return Objects.hash(decetId, retBase, retTrend, retAdjustment, decrementParametersByRetBase, refExpectedTableByRetBase, refExpectedTableByRetTrend, refExpectedTableByRetAdjustment);
    }

    @ManyToOne()
    @JoinColumn(name = "RET_DP_ID", referencedColumnName = "DP_ID")
    @JsonBackReference
    public DecrementParametersEntity getDecrementParametersByRetBase() {
        return decrementParametersByRetBase;
    }

    public void setDecrementParametersByRetBase(DecrementParametersEntity decrementParametersByRetBase) {
        this.decrementParametersByRetBase = decrementParametersByRetBase;
    }

    @JsonProperty
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @Transient
    @OneToOne(fetch=FetchType.LAZY , orphanRemoval = false)
    @JoinColumn(name = "RET_BASE", referencedColumnName = "RET_ID" , insertable = false ,updatable = false)
    public RefExpectedTableEntity getRefExpectedTableByRetBase() {
        return refExpectedTableByRetBase;
    }

    public void setRefExpectedTableByRetBase(RefExpectedTableEntity refExpectedTableByRetBase) {
        this.refExpectedTableByRetBase = refExpectedTableByRetBase;
    }

    @JsonProperty
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @Transient
    @OneToOne(fetch=FetchType.LAZY  , orphanRemoval = false)
    @JoinColumn(name = "RET_TREND", referencedColumnName = "RET_ID" , insertable = false ,updatable = false)
    public RefExpectedTableEntity getRefExpectedTableByRetTrend() {
        return refExpectedTableByRetTrend;
    }

    public void setRefExpectedTableByRetTrend(RefExpectedTableEntity refExpectedTableByRetTrend) {
        this.refExpectedTableByRetTrend = refExpectedTableByRetTrend;
    }

    @JsonProperty
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @Transient
    @OneToOne(fetch=FetchType.LAZY  , orphanRemoval = false)
    @JoinColumn(name = "RET_ADJUSTMENT", referencedColumnName = "RET_ID" , insertable = false ,updatable = false)
    public RefExpectedTableEntity getRefExpectedTableByRetAdjustment() {
        return refExpectedTableByRetAdjustment;
    }

    public void setRefExpectedTableByRetAdjustment(RefExpectedTableEntity refExpectedTableByRetAdjustment ) {
        this.refExpectedTableByRetAdjustment = refExpectedTableByRetAdjustment;
    }
    
    @JsonProperty
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @Transient
    @OneToOne(fetch=FetchType.LAZY  , orphanRemoval = false)
    @JoinColumn(name = "RET_POLICY", referencedColumnName = "RET_ID" , insertable = false ,updatable = false)
    public RefExpectedTableEntity getRefExpectedTableByRetPolicy() {
        return refExpectedTableByRetPolicy;
    }

    public void setRefExpectedTableByRetPolicy(RefExpectedTableEntity refExpectedTableByRetPolicy) {
        this.refExpectedTableByRetPolicy = refExpectedTableByRetPolicy;
    }
}
