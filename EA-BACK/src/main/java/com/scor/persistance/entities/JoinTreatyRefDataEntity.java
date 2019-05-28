package com.scor.persistance.entities;


import com.scor.persistance.specifications.JoinTreatyId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "JOIN_TREATY_REF_DATA", schema = "dbo", catalog = "EXPAN")
public class JoinTreatyRefDataEntity implements Serializable {


    @EmbeddedId
    JoinTreatyId joinTreatyId;

    public JoinTreatyRefDataEntity() {
    }

    public JoinTreatyRefDataEntity(JoinTreatyId joinTreatyId) {
        this.joinTreatyId = joinTreatyId;
    }

    public JoinTreatyId getJoinTreatyId() {
        return joinTreatyId;
    }

    public void setJoinTreatyId(JoinTreatyId joinTreatyId) {
        this.joinTreatyId = joinTreatyId;
    }
}
