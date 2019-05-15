package com.scor.ref_treaty.schemas._1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour SCORTtyTermCond complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="SCORTtyTermCond"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ctrNf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="secNf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="treatyStatus" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ctrtypCt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="uwgrpCf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="uwyNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="uwNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="endNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="occBas" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sbjprmcurCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="estsbjprmM" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="sbjprmcmpCf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="unlliaB" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="liaridshaB" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="liaevtlimM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="liaannlimM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="liaanndedM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="liatdtlimM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="liatptlimM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="liafldlimM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="laycapM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="liadedM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="liaagrdedM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="reiunlB" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="participantList" type="{http://scor.com/ref-treaty/schemas/1.1}participantList" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SCORTtyTermCond", propOrder = {
    "ctrNf",
    "secNf",
    "treatyStatus",
    "ctrtypCt",
    "uwgrpCf",
    "uwyNf",
    "uwNt",
    "endNt",
    "occBas",
    "sbjprmcurCf",
    "estsbjprmM",
    "sbjprmcmpCf",
    "unlliaB",
    "liaridshaB",
    "liaevtlimM",
    "liaannlimM",
    "liaanndedM",
    "liatdtlimM",
    "liatptlimM",
    "liafldlimM",
    "laycapM",
    "liadedM",
    "liaagrdedM",
    "reiunlB",
    "participantList"
})
public class SCORTtyTermCond {

    @XmlElement(required = true)
    protected String ctrNf;
    protected int secNf;
    @XmlElement(defaultValue = "0")
    protected int treatyStatus;
    @XmlElement(defaultValue = "0")
    protected int ctrtypCt;
    protected Integer uwgrpCf;
    protected Integer uwyNf;
    @XmlElement(defaultValue = "0")
    protected int uwNt;
    @XmlElement(defaultValue = "0")
    protected int endNt;
    @XmlElement(required = true)
    protected String occBas;
    @XmlElement(required = true)
    protected String sbjprmcurCf;
    @XmlElement(required = true)
    protected BigDecimal estsbjprmM;
    protected Integer sbjprmcmpCf;
    @XmlElement(defaultValue = "true")
    protected boolean unlliaB;
    @XmlElement(required = true)
    protected String liaridshaB;
    protected BigDecimal liaevtlimM;
    protected BigDecimal liaannlimM;
    protected BigDecimal liaanndedM;
    protected BigDecimal liatdtlimM;
    protected BigDecimal liatptlimM;
    protected BigDecimal liafldlimM;
    protected BigDecimal laycapM;
    protected BigDecimal liadedM;
    protected BigDecimal liaagrdedM;
    @XmlElement(defaultValue = "true")
    protected boolean reiunlB;
    protected ParticipantList participantList;

    /**
     * Obtient la valeur de la propriété ctrNf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtrNf() {
        return ctrNf;
    }

    /**
     * Définit la valeur de la propriété ctrNf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtrNf(String value) {
        this.ctrNf = value;
    }

    /**
     * Obtient la valeur de la propriété secNf.
     * 
     */
    public int getSecNf() {
        return secNf;
    }

    /**
     * Définit la valeur de la propriété secNf.
     * 
     */
    public void setSecNf(int value) {
        this.secNf = value;
    }

    /**
     * Obtient la valeur de la propriété treatyStatus.
     * 
     */
    public int getTreatyStatus() {
        return treatyStatus;
    }

    /**
     * Définit la valeur de la propriété treatyStatus.
     * 
     */
    public void setTreatyStatus(int value) {
        this.treatyStatus = value;
    }

    /**
     * Obtient la valeur de la propriété ctrtypCt.
     * 
     */
    public int getCtrtypCt() {
        return ctrtypCt;
    }

    /**
     * Définit la valeur de la propriété ctrtypCt.
     * 
     */
    public void setCtrtypCt(int value) {
        this.ctrtypCt = value;
    }

    /**
     * Obtient la valeur de la propriété uwgrpCf.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUwgrpCf() {
        return uwgrpCf;
    }

    /**
     * Définit la valeur de la propriété uwgrpCf.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUwgrpCf(Integer value) {
        this.uwgrpCf = value;
    }

    /**
     * Obtient la valeur de la propriété uwyNf.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUwyNf() {
        return uwyNf;
    }

    /**
     * Définit la valeur de la propriété uwyNf.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUwyNf(Integer value) {
        this.uwyNf = value;
    }

    /**
     * Obtient la valeur de la propriété uwNt.
     * 
     */
    public int getUwNt() {
        return uwNt;
    }

    /**
     * Définit la valeur de la propriété uwNt.
     * 
     */
    public void setUwNt(int value) {
        this.uwNt = value;
    }

    /**
     * Obtient la valeur de la propriété endNt.
     * 
     */
    public int getEndNt() {
        return endNt;
    }

    /**
     * Définit la valeur de la propriété endNt.
     * 
     */
    public void setEndNt(int value) {
        this.endNt = value;
    }

    /**
     * Obtient la valeur de la propriété occBas.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOccBas() {
        return occBas;
    }

    /**
     * Définit la valeur de la propriété occBas.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOccBas(String value) {
        this.occBas = value;
    }

    /**
     * Obtient la valeur de la propriété sbjprmcurCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSbjprmcurCf() {
        return sbjprmcurCf;
    }

    /**
     * Définit la valeur de la propriété sbjprmcurCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSbjprmcurCf(String value) {
        this.sbjprmcurCf = value;
    }

    /**
     * Obtient la valeur de la propriété estsbjprmM.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEstsbjprmM() {
        return estsbjprmM;
    }

    /**
     * Définit la valeur de la propriété estsbjprmM.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEstsbjprmM(BigDecimal value) {
        this.estsbjprmM = value;
    }

    /**
     * Obtient la valeur de la propriété sbjprmcmpCf.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSbjprmcmpCf() {
        return sbjprmcmpCf;
    }

    /**
     * Définit la valeur de la propriété sbjprmcmpCf.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSbjprmcmpCf(Integer value) {
        this.sbjprmcmpCf = value;
    }

    /**
     * Obtient la valeur de la propriété unlliaB.
     * 
     */
    public boolean isUnlliaB() {
        return unlliaB;
    }

    /**
     * Définit la valeur de la propriété unlliaB.
     * 
     */
    public void setUnlliaB(boolean value) {
        this.unlliaB = value;
    }

    /**
     * Obtient la valeur de la propriété liaridshaB.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLiaridshaB() {
        return liaridshaB;
    }

    /**
     * Définit la valeur de la propriété liaridshaB.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLiaridshaB(String value) {
        this.liaridshaB = value;
    }

    /**
     * Obtient la valeur de la propriété liaevtlimM.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLiaevtlimM() {
        return liaevtlimM;
    }

    /**
     * Définit la valeur de la propriété liaevtlimM.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLiaevtlimM(BigDecimal value) {
        this.liaevtlimM = value;
    }

    /**
     * Obtient la valeur de la propriété liaannlimM.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLiaannlimM() {
        return liaannlimM;
    }

    /**
     * Définit la valeur de la propriété liaannlimM.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLiaannlimM(BigDecimal value) {
        this.liaannlimM = value;
    }

    /**
     * Obtient la valeur de la propriété liaanndedM.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLiaanndedM() {
        return liaanndedM;
    }

    /**
     * Définit la valeur de la propriété liaanndedM.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLiaanndedM(BigDecimal value) {
        this.liaanndedM = value;
    }

    /**
     * Obtient la valeur de la propriété liatdtlimM.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLiatdtlimM() {
        return liatdtlimM;
    }

    /**
     * Définit la valeur de la propriété liatdtlimM.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLiatdtlimM(BigDecimal value) {
        this.liatdtlimM = value;
    }

    /**
     * Obtient la valeur de la propriété liatptlimM.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLiatptlimM() {
        return liatptlimM;
    }

    /**
     * Définit la valeur de la propriété liatptlimM.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLiatptlimM(BigDecimal value) {
        this.liatptlimM = value;
    }

    /**
     * Obtient la valeur de la propriété liafldlimM.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLiafldlimM() {
        return liafldlimM;
    }

    /**
     * Définit la valeur de la propriété liafldlimM.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLiafldlimM(BigDecimal value) {
        this.liafldlimM = value;
    }

    /**
     * Obtient la valeur de la propriété laycapM.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLaycapM() {
        return laycapM;
    }

    /**
     * Définit la valeur de la propriété laycapM.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLaycapM(BigDecimal value) {
        this.laycapM = value;
    }

    /**
     * Obtient la valeur de la propriété liadedM.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLiadedM() {
        return liadedM;
    }

    /**
     * Définit la valeur de la propriété liadedM.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLiadedM(BigDecimal value) {
        this.liadedM = value;
    }

    /**
     * Obtient la valeur de la propriété liaagrdedM.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLiaagrdedM() {
        return liaagrdedM;
    }

    /**
     * Définit la valeur de la propriété liaagrdedM.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLiaagrdedM(BigDecimal value) {
        this.liaagrdedM = value;
    }

    /**
     * Obtient la valeur de la propriété reiunlB.
     * 
     */
    public boolean isReiunlB() {
        return reiunlB;
    }

    /**
     * Définit la valeur de la propriété reiunlB.
     * 
     */
    public void setReiunlB(boolean value) {
        this.reiunlB = value;
    }

    /**
     * Obtient la valeur de la propriété participantList.
     * 
     * @return
     *     possible object is
     *     {@link ParticipantList }
     *     
     */
    public ParticipantList getParticipantList() {
        return participantList;
    }

    /**
     * Définit la valeur de la propriété participantList.
     * 
     * @param value
     *     allowed object is
     *     {@link ParticipantList }
     *     
     */
    public void setParticipantList(ParticipantList value) {
        this.participantList = value;
    }

}
