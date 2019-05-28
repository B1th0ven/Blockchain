package com.scor.persistance.specifications;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class JoinTreatyId implements Serializable {

    @Column(name = "JTR_RT_ID")
    Integer  rtId;

    @Column(name = "JTR_ED_ID")
    Integer  edId;

    public JoinTreatyId() {
    }

    public JoinTreatyId(Integer rtId, Integer edId) {
        this.rtId = rtId;
        this.edId = edId;
    }

    public Integer getRtId() {
        return rtId;
    }

    public void setRtId(Integer rtId) {
        this.rtId = rtId;
    }

    public Integer getEdId() {
        return edId;
    }

    public void setEdId(Integer edId) {
        this.edId = edId;
    }
}
