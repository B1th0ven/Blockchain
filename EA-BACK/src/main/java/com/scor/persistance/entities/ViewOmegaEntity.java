package com.scor.persistance.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Immutable
@Table(name = "OMEGA_EXTRACTION", schema = "dbo", catalog = "EXPAN")
public class ViewOmegaEntity implements Serializable {


    private String treatyNumberOmega;
    private String clientOmegaId;
    private String clientRiskCarrierName;
    private String commercialClientName;
    private String parentGroupCode;
    private String parentGroupname;
    private String clientCountry;
    private String portfolioOrigin;
    private String currency;
    private String legalEntity;
    private String legalEntityCode;




    public ViewOmegaEntity() {
    }

    @Id
    @Column(name = "Treaty_Number_Omega")
    public String getTreatyNumberOmega() {
        return treatyNumberOmega;
    }


    public void setTreatyNumberOmega(String treatyNumberOmega) {
        this.treatyNumberOmega = treatyNumberOmega;
    }

    @Basic
    @Column(name = "Client_Omega_Code")
    public String getClientOmegaId() {
        return clientOmegaId;
    }

    public void setClientOmegaId(String clientOmegaId) {
        this.clientOmegaId = clientOmegaId;
    }
    @Basic
    @Column(name = "Client_Risk_Carrier_Name")
    public String getClientRiskCarrierName() {
        return clientRiskCarrierName;
    }

    public void setClientRiskCarrierName(String clientRiskCarrierName) {
        this.clientRiskCarrierName = clientRiskCarrierName;
    }

    @Basic
    @Column(name = "Commercial_Client_Name")
    public String getCommercialClientName() {
        return commercialClientName;
    }

    public void setCommercialClientName(String commercialClientName) {
        this.commercialClientName = commercialClientName;
    }

    @Basic
    @Column(name = "PARENT_GROUP_OMEGA_CODE")
    public String getParentGroupCode() {
        return parentGroupCode;
    }

    public void setParentGroupCode(String parentGroupCode) {
        this.parentGroupCode = parentGroupCode;
    }

    @Basic
    @Column(name = "PARENT_GROUP_NAME")
    public String getParentGroupname() {
        return parentGroupname;
    }

    public void setParentGroupname(String parentGroupname) {
        this.parentGroupname = parentGroupname;
    }

    @Basic
    @Column(name = "Client_country")
    public String getClientCountry() {
        return clientCountry;
    }

    public void setClientCountry(String clientCountry) {
        this.clientCountry = clientCountry;
    }

    @Basic
    @Column(name = "Currency")
    public String getPortfolioOrigin() {
        return portfolioOrigin;
    }

    public void setPortfolioOrigin(String portfolioOrigin) {
        this.portfolioOrigin = portfolioOrigin;
    }

    @Basic
    @Column(name = "portfolio_origin")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    @Basic
    @Column(name = "LEGAL_ENTITY")
    public String getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(String legalEntity) {
        this.legalEntity = legalEntity;
    }

    @Basic
    @Column(name = "LEGAL_ENTITY_CODE")
    public String getLegalEntityCode() {
        return legalEntityCode;
    }

    public void setLegalEntityCode(String legalEntityCode) {
        this.legalEntityCode = legalEntityCode;
    }
}
