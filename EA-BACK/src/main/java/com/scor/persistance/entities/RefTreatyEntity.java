package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "REF_TREATY", schema = "dbo", catalog = "EXPAN")
public class RefTreatyEntity implements Serializable {
    private Integer rtId;
    private String rtName;
    private Integer rtRcnId;
    private RefCedentNameEntity refCedentNameByRtRcnId;

    @Id
    @Column(name = "RT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getRtId() {
        return rtId;
    }

    public void setRtId(Integer rtId) {
        this.rtId = rtId;
    }

    @Basic
    @Column(name = "RT_NAME")
    public String getRtName() {
        return rtName;
    }

    public void setRtName(String rtName) {
        this.rtName = rtName;
    }

    @Basic
    @Column(name = "RT_RCN_ID")
    public Integer getRtRcnId() {
        return rtRcnId;
    }

    public void setRtRcnId(Integer rtRcnId) {
        this.rtRcnId = rtRcnId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefTreatyEntity that = (RefTreatyEntity) o;
        return rtId == that.rtId &&
                Objects.equals(rtName, that.rtName) &&
                Objects.equals(rtRcnId, that.rtRcnId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rtId, rtName, rtRcnId);
    }

    @ManyToOne
    @JoinColumn(name = "RT_RCN_ID", referencedColumnName = "RCN_ID" , insertable = false ,updatable = false)
    public RefCedentNameEntity getRefCedentNameByRtRcnId() {
        return refCedentNameByRtRcnId;
    }

    public void setRefCedentNameByRtRcnId(RefCedentNameEntity refCedentNameByRtRcnId) {
        this.refCedentNameByRtRcnId = refCedentNameByRtRcnId;
    }
}
