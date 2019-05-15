package com.scor.ea.omega.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "REF_TREATY", schema = "dbo", catalog = "EXPAN")
public class RefTreatyEntity {
    @Id
    @Column(name = "RT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rtId;

    @Basic
    @Column(name = "RT_NAME")
    private String rtName;

    @Basic
    @Column(name = "RT_RCN_ID")
    private Integer rtRcnId;



    public Integer getRtId() {
        return rtId;
    }

    public void setRtId(Integer rtId) {
        this.rtId = rtId;
    }


    public String getRtName() {
        return rtName;
    }

    public void setRtName(String rtName) {
        this.rtName = rtName;
    }


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



}
