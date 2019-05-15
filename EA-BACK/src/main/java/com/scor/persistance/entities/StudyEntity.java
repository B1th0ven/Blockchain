package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "STUDY", schema = "dbo", catalog = "EXPAN")
public class StudyEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1835051438602811701L;
	private int stId;
    private String stCode;
    private Integer stRdsId;
    private Integer stRpgId;
    private Integer stRcnId;
    private Integer stRcId;
    private Integer stRlobId;
    private Integer stRcetId;
    private Integer stStcId;
    private Integer stRtId;
    private String stStatus;
    private Integer stLastStatusModifiedBy;
    private Date stLastStatusModificationDate;
    private Date stStartObservationDate;
    private Date stEndObservationDate;
    private Boolean stMultiParentGroup;
    private Boolean stMultiCedent;
    private Boolean stMultiTreaty;
    private Boolean stOtherParentGroup;
    private Boolean stOtherCedent;
    private Boolean stOtheriTreaty;
    private String stShortName;
    private Integer stQualityDataProvider;
    private String stDistributionBrand;
    private String stAttachedFilePath;
    private Integer stCreatedById;
    private Date stCreatedDate;
    private String stComment;
    private String stFlag;
    //to update when the status change ( not the flag)!
    private Timestamp stLastFlagModificationDate;
    private RefDataSourceEntity refDataSourceByStRdsId;
    private RefParentGroupEntity refParentGroupByStRpgId;
    private RefCedentNameEntity refCedentNameByStRcnId;
    private RefCalculationEngineTypeEntity refCalculationEngineTypeByStRcetId;
    private RefRequesterEntity refRequesterByStStcId;
    private RefTreatyEntity refTreatyByStRtId;
    private RefUserEntity refUserByStLastStatusModifiedBy;
    private RefUserEntity refUserByStCreatedById;
    private RefCountryEntity refCountryById;

    //Not Generated
    private Collection<RefLobEntity> refLobsById;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ST_ID")
    public int getStId() {
        return stId;
    }

    public void setStId(int stId) {
        this.stId = stId;
    }

    @Basic
    @Column(name = "ST_CODE")
    public String getStCode() {
        return stCode;
    }

    public void setStCode(String stCode) {
        this.stCode = stCode;
    }

    @Basic
    @Column(name = "ST_RDS_ID")
    public Integer getStRdsId() {
        return stRdsId;
    }

    public void setStRdsId(Integer stRdsId) {
        this.stRdsId = stRdsId;
    }

    @Basic
    @Column(name = "ST_RPG_ID")
    public Integer getStRpgId() {
        return stRpgId;
    }

    public void setStRpgId(Integer stRpgId) {
        this.stRpgId = stRpgId;
    }

    @Basic
    @Column(name = "ST_RCN_ID")
    public Integer getStRcnId() {
        return stRcnId;
    }

    public void setStRcnId(Integer stRcnId) {
        this.stRcnId = stRcnId;
    }

    @Basic
    @Column(name = "ST_RC_ID")
    public Integer getStRcId() {
        return stRcId;
    }

    public void setStRcId(Integer stRcId) {
        this.stRcId = stRcId;
    }

    @Basic
    @Column(name = "ST_RLOB_ID")
    public Integer getStRlobId() {
        return stRlobId;
    }

    public void setStRlobId(Integer stRlobId) {
        this.stRlobId = stRlobId;
    }

    @Basic
    @Column(name = "ST_RCET_ID")
    public Integer getStRcetId() {
        return stRcetId;
    }

    public void setStRcetId(Integer stRcetId) {
        this.stRcetId = stRcetId;
    }

    @Basic
    @Column(name = "ST_STC_ID")
    public Integer getStStcId() {
        return stStcId;
    }

    public void setStStcId(Integer stStcId) {
        this.stStcId = stStcId;
    }

    @Basic
    @Column(name = "ST_RT_ID")
    public Integer getStRtId() {
        return stRtId;
    }

    public void setStRtId(Integer stRtId) {
        this.stRtId = stRtId;
    }

    @Basic
    @Column(name = "ST_ATTACHED_FILE_PATH")
    public String getStAttachedFilePath() {
        return stAttachedFilePath;
    }

    public void setStAttachedFilePath(String stAttachedFilePath) {
        this.stAttachedFilePath = stAttachedFilePath;
    }

    @Basic
    @Column(name = "ST_STATUS")
    public String getStStatus() {
        return stStatus;
    }

    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    @Basic
    @Column(name = "ST_LAST_STATUS_MODIFIED_BY")
    public Integer getStLastStatusModifiedBy() {
        return stLastStatusModifiedBy;
    }

    public void setStLastStatusModifiedBy(Integer stLastStatusModifiedBy) {
        this.stLastStatusModifiedBy = stLastStatusModifiedBy;
    }

    @Basic
    @Column(name = "ST_LAST_STATUS_MODIFICATION_DATE")
    public Date getStLastStatusModificationDate() {
        return stLastStatusModificationDate;
    }

    public void setStLastStatusModificationDate(Date stLastStatusModificationDate) {
        this.stLastStatusModificationDate = stLastStatusModificationDate;
    }

    @Basic
    @Column(name = "ST_START_OBSERVATION_DATE")
    public Date getStStartObservationDate() {
        return stStartObservationDate;
    }

    public void setStStartObservationDate(Date stStartObservationDate) {
        this.stStartObservationDate = stStartObservationDate;
    }

    @Basic
    @Column(name = "ST_END_OBSERVATION_DATE")
    public Date getStEndObservationDate() {
        return stEndObservationDate;
    }

    public void setStEndObservationDate(Date stEndObservationDate) {
        this.stEndObservationDate = stEndObservationDate;
    }

    @Basic
    @Column(name = "ST_MULTI_PARENT_GROUP")
    public Boolean getStMultiParentGroup() {
        return stMultiParentGroup;
    }

    public void setStMultiParentGroup(Boolean stMultiParentGroup) {
        this.stMultiParentGroup = stMultiParentGroup;
    }

    @Basic
    @Column(name = "ST_MULTI_CEDENT")
    public Boolean getStMultiCedent() {
        return stMultiCedent;
    }

    public void setStMultiCedent(Boolean stMultiCedent) {
        this.stMultiCedent = stMultiCedent;
    }

    @Basic
    @Column(name = "ST_MULTI_TREATY")
    public Boolean getStMultiTreaty() {
        return stMultiTreaty;
    }

    public void setStMultiTreaty(Boolean stMultiTreaty) {
        this.stMultiTreaty = stMultiTreaty;
    }

    @Basic
    @Column(name = "ST_OTHER_PARENT_GROUP")
    public Boolean getStOtherParentGroup() {
        return stOtherParentGroup;
    }

    public void setStOtherParentGroup(Boolean stOtherParentGroup) {
        this.stOtherParentGroup = stOtherParentGroup;
    }

    @Basic
    @Column(name = "ST_OTHER_CEDENT")
    public Boolean getStOtherCedent() {
        return stOtherCedent;
    }

    public void setStOtherCedent(Boolean stOtherCedent) {
        this.stOtherCedent = stOtherCedent;
    }

    @Basic
    @Column(name = "ST_OTHERI_TREATY")
    public Boolean getStOtheriTreaty() {
        return stOtheriTreaty;
    }

    public void setStOtheriTreaty(Boolean stOtheriTreaty) {
        this.stOtheriTreaty = stOtheriTreaty;
    }

    @Basic
    @Column(name = "ST_SHORT_NAME")
    public String getStShortName() {
        return stShortName;
    }

    public void setStShortName(String stShortName) {
        this.stShortName = stShortName;
    }

    @Basic
    @Column(name = "ST_QUALITY_DATA_PROVIDER")
    public Integer getStQualityDataProvider() {
        return stQualityDataProvider;
    }

    public void setStQualityDataProvider(Integer stQualityDataProvider) {
        this.stQualityDataProvider = stQualityDataProvider;
    }

    @Basic
    @Column(name = "ST_DISTRIBUTION_BRAND")
    public String getStDistributionBrand() {
        return stDistributionBrand;
    }

    public void setStDistributionBrand(String stDistributionBrand) {
        this.stDistributionBrand = stDistributionBrand;
    }

    @Basic
    @Column(name = "ST_CREATED_BY_ID")
    public Integer getStCreatedById() {
        return stCreatedById;
    }

    public void setStCreatedById(Integer stCreatedById) {
        this.stCreatedById = stCreatedById;
    }

    @Basic
    @Column(name = "ST_CREATED_DATE")
    public Date getStCreatedDate() {
        return stCreatedDate;
    }

    public void setStCreatedDate(Date stCreatedDate) {
        this.stCreatedDate = stCreatedDate;
    }

    @Basic
    @Column(name = "ST_COMMENT")
    public String getStComment() {
        return stComment;
    }

    public void setStComment(String stComment) {
        this.stComment = stComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyEntity that = (StudyEntity) o;
        return stId == that.stId &&
                Objects.equals(stCode, that.stCode) &&
                Objects.equals(stRdsId, that.stRdsId) &&
                Objects.equals(stRpgId, that.stRpgId) &&
                Objects.equals(stRcnId, that.stRcnId) &&
                Objects.equals(stRcId, that.stRcId) &&
                Objects.equals(stRlobId, that.stRlobId) &&
                Objects.equals(stRcetId, that.stRcetId) &&
                Objects.equals(stStcId, that.stStcId) &&
                Objects.equals(stRtId, that.stRtId) &&
                Objects.equals(stStatus, that.stStatus) &&
                Objects.equals(stLastStatusModifiedBy, that.stLastStatusModifiedBy) &&
                Objects.equals(stLastStatusModificationDate, that.stLastStatusModificationDate) &&
                Objects.equals(stStartObservationDate, that.stStartObservationDate) &&
                Objects.equals(stEndObservationDate, that.stEndObservationDate) &&
                Objects.equals(stMultiParentGroup, that.stMultiParentGroup) &&
                Objects.equals(stMultiCedent, that.stMultiCedent) &&
                Objects.equals(stMultiTreaty, that.stMultiTreaty) &&
                Objects.equals(stOtherParentGroup, that.stOtherParentGroup) &&
                Objects.equals(stOtherCedent, that.stOtherCedent) &&
                Objects.equals(stOtheriTreaty, that.stOtheriTreaty) &&
                Objects.equals(stShortName, that.stShortName) &&
                Objects.equals(stQualityDataProvider, that.stQualityDataProvider) &&
                Objects.equals(stDistributionBrand, that.stDistributionBrand) &&
                Objects.equals(stCreatedById, that.stCreatedById) &&
                Objects.equals(stCreatedDate, that.stCreatedDate) &&
                Objects.equals(stComment, that.stComment);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stId, stCode, stRdsId, stRpgId, stRcnId, stRcId, stRlobId, stRcetId, stStcId, stRtId, stStatus, stLastStatusModifiedBy, stLastStatusModificationDate, stStartObservationDate, stEndObservationDate, stMultiParentGroup, stMultiCedent, stMultiTreaty, stOtherParentGroup, stOtherCedent, stOtheriTreaty, stShortName, stQualityDataProvider, stDistributionBrand, stCreatedById, stCreatedDate, stComment);
    }

    @ManyToOne
    @JoinColumn(name = "ST_RDS_ID", referencedColumnName = "RDS_ID" , insertable = false ,updatable = false)
    public RefDataSourceEntity getRefDataSourceByStRdsId() {
        return refDataSourceByStRdsId;
    }

    public void setRefDataSourceByStRdsId(RefDataSourceEntity refDataSourceByStRdsId) {
        this.refDataSourceByStRdsId = refDataSourceByStRdsId;
    }

    @ManyToOne
    @JoinColumn(name = "ST_RPG_ID", referencedColumnName = "RPG_ID" , insertable = false ,updatable = false)
    public RefParentGroupEntity getRefParentGroupByStRpgId() {
        return refParentGroupByStRpgId;
    }

    public void setRefParentGroupByStRpgId(RefParentGroupEntity refParentGroupByStRpgId) {
        this.refParentGroupByStRpgId = refParentGroupByStRpgId;
    }

    @ManyToOne
    @JoinColumn(name = "ST_RCN_ID", referencedColumnName = "RCN_ID" , insertable = false ,updatable = false)
    public RefCedentNameEntity getRefCedentNameByStRcnId() {
        return refCedentNameByStRcnId;
    }

    public void setRefCedentNameByStRcnId(RefCedentNameEntity refCedentNameByStRcnId) {
        this.refCedentNameByStRcnId = refCedentNameByStRcnId;
    }

    @ManyToOne
    @JoinColumn(name = "ST_RCET_ID", referencedColumnName = "RCET_ID" , insertable = false ,updatable = false)
    public RefCalculationEngineTypeEntity getRefCalculationEngineTypeByStRcetId() {
        return refCalculationEngineTypeByStRcetId;
    }

    public void setRefCalculationEngineTypeByStRcetId(RefCalculationEngineTypeEntity refCalculationEngineTypeByStRcetId) {
        this.refCalculationEngineTypeByStRcetId = refCalculationEngineTypeByStRcetId;
    }

    @ManyToOne
    @JoinColumn(name = "ST_STC_ID", referencedColumnName = "STC_ID" , insertable = false ,updatable = false)
    public RefRequesterEntity getRefRequesterByStStcId() {
        return refRequesterByStStcId;
    }

    public void setRefRequesterByStStcId(RefRequesterEntity refRequesterByStStcId) {
        this.refRequesterByStStcId = refRequesterByStStcId;
    }

    @ManyToOne
    @JoinColumn(name = "ST_RT_ID", referencedColumnName = "RT_ID" , insertable = false ,updatable = false)
    public RefTreatyEntity getRefTreatyByStRtId() {
        return refTreatyByStRtId;
    }

    public void setRefTreatyByStRtId(RefTreatyEntity refTreatyByStRtId) {
        this.refTreatyByStRtId = refTreatyByStRtId;
    }

    @ManyToOne
    @JoinColumn(name = "ST_LAST_STATUS_MODIFIED_BY", referencedColumnName = "RU_ID" , insertable = false ,updatable = false)
    public RefUserEntity getRefUserByStLastStatusModifiedBy() {
        return refUserByStLastStatusModifiedBy;
    }

    public void setRefUserByStLastStatusModifiedBy(RefUserEntity refUserByStLastStatusModifiedBy) {
        this.refUserByStLastStatusModifiedBy = refUserByStLastStatusModifiedBy;
    }

    @ManyToOne
    @JoinColumn(name = "ST_CREATED_BY_ID", referencedColumnName = "RU_ID" , insertable = false ,updatable = false)
    public RefUserEntity getRefUserByStCreatedById() {
        return refUserByStCreatedById;
    }

    public void setRefUserByStCreatedById(RefUserEntity refUserByStCreatedById) {
        this.refUserByStCreatedById = refUserByStCreatedById;
    }

    @OneToMany
    @JoinTable(
            name = "STUDY_LOB",
            joinColumns = @JoinColumn( name = "SLOB_ST_ID" ),
            inverseJoinColumns = @JoinColumn( name = "SLOB_RLOB_ID")
    )
    public Collection<RefLobEntity> getRefLobsById() {
        return refLobsById;
    }

    public void setRefLobsById(Collection<RefLobEntity> refLobsById) {
        this.refLobsById = refLobsById;
    }

    @ManyToOne
    @JoinColumn( name = "ST_RC_ID" , insertable = false , updatable = false )
    public RefCountryEntity getRefCountryById() {
        return refCountryById;
    }

    public void setRefCountryById(RefCountryEntity refCountryById) {
        this.refCountryById = refCountryById;
    }

    @Basic
    @Column(name = "ST_FLAG")
    public String getStFlag() {
        return stFlag;
    }

    public void setStFlag(String stFlag) {
        this.stFlag = stFlag;
    }

    @Basic
    @Column(name = "ST_LAST_FLAG_MODIFICATION_DATE")
    public Timestamp getStLastFlagModificationDate() {
        return stLastFlagModificationDate;
    }

    public void setStLastFlagModificationDate(Timestamp stLastFlagModificationDate) {
        this.stLastFlagModificationDate = stLastFlagModificationDate;
    }
}
