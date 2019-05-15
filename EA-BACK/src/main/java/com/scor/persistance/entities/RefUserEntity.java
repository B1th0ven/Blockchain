package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "REF_USER", schema = "dbo", catalog = "EXPAN")
public class RefUserEntity  implements Serializable {
    private int ruId;
    private String ruFirstName;
    private String ruLastName;
    private Integer ruRmId;
    private String ruLogin;
    private String ruRole;
    private String ruFunction;
    private String ruMailAdresse ; 
    private RefScopeEntity scope;
    private RefCountryEntity country;
    private RefMarketEntity market;


    @Id
    @Column(name = "RU_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getRuId() {
        return ruId;
    }

    public void setRuId(int ruId) {
        this.ruId = ruId;
    }

    @Basic
    @Column(name = "RU_FIRST_NAME")
    public String getRuFirstName() {
        return ruFirstName;
    }

    public void setRuFirstName(String ruFirstName) {
        this.ruFirstName = ruFirstName;
    }

    @Basic
    @Column(name = "RU_LAST_NAME")
    public String getRuLastName() {
        return ruLastName;
    }

    public void setRuLastName(String ruLastName) {
        this.ruLastName = ruLastName;
    }

    @Basic
    @Column(name = "RU_RM_ID")
    public Integer getRuRmId() {
        return ruRmId;
    }

    public void setRuRmId(Integer ruRmId) {
        this.ruRmId = ruRmId;
    }

    @Basic
    @Column(name = "RU_LOGIN")
    public String getRuLogin() {
        return ruLogin;
    }

    public void setRuLogin(String ruLogin) {
        this.ruLogin = ruLogin;
    }

    @Basic
    @Column(name = "RU_ROLE")
    public String getRuRole() {
    	if(ruRole != null) return ruRole.toUpperCase();
        return ruRole;
    }

    public void setRuRole(String ruRole) {
        this.ruRole = ruRole;
    }

    @Basic
    @Column(name = "RU_FUNCTION")
    public String getRuFunction() {
        return ruFunction;
    }

    public void setRuFunction(String ruFunction) {
        this.ruFunction = ruFunction;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefUserEntity)) return false;
        RefUserEntity that = (RefUserEntity) o;
        return getRuId() == that.getRuId() &&
                Objects.equals(getRuFirstName(), that.getRuFirstName()) &&
                Objects.equals(getRuLastName(), that.getRuLastName()) &&
                Objects.equals(getRuRmId(), that.getRuRmId()) &&
                Objects.equals(getRuLogin(), that.getRuLogin()) &&
                Objects.equals(getRuRole(), that.getRuRole()) &&
                Objects.equals(getRuFunction(), that.getRuFunction()) &&
                Objects.equals(getScope(), that.getScope()) &&
                Objects.equals(getCountry(), that.getCountry()) &&
                Objects.equals(getMarket(), that.getMarket());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRuId(), getRuFirstName(), getRuLastName(), getRuRmId(), getRuLogin(), getRuRole(), getRuFunction(), getScope(), getCountry(), getMarket());
    }

    @ManyToOne
    @JoinColumn(name = "RU_RM_ID", referencedColumnName = "RM_ID" , insertable = false ,updatable = false)
    public RefMarketEntity getMarket() {
        return market;
    }

    public void setMarket(RefMarketEntity market) {
        this.market = market;
    }

    @ManyToOne
    @JoinColumn(name = "RU_RS_ID", referencedColumnName = "RS_ID")
    public RefScopeEntity getScope() {
        return scope;
    }

    public void setScope(RefScopeEntity scope) {
        this.scope = scope;
    }

    @ManyToOne
    @JoinColumn(name = "RU_RC_ID", referencedColumnName = "RC_ID" , insertable = false ,updatable = false)
    public RefCountryEntity getCountry() {
        return country;
    }

    public void setCountry(RefCountryEntity country) {
        this.country = country;
    }

    @Basic
    @Column(name = "RU_MAIL_ADRESSE")
	public String getRuMailAdresse() {
		return ruMailAdresse;
	}

	public void setRuMailAdresse(String ruMailAdresse) {
		this.ruMailAdresse = ruMailAdresse;
	}


}
