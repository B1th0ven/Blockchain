package com.scor.ref_treaty.schemas._1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour SCORTtySection complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="SCORTtySection"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ctrNf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="secNf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="uwyNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="uwNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="endNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="seclabLm" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="secstsCt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="usgaapCt" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="garCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="secaccstsCt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="lobCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="lobGl" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sobCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sobGl" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="topCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="topGl" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="natCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ctrnatGd" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ridshaR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="actshaR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="sigshaR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="expshaR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="earexpB" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="strexpB" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="fldexpB" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="speaccprmcurCf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="egpcurCf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="speacclimcurCf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="seccanD" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="prmrRunOffB" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="clmrRunOffB" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="prmrCutOffB" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="clmfCutOffB" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="runOffYear" type="{http://www.w3.org/2001/XMLSchema}short" minOccurs="0"/&gt;
 *         &lt;element name="liaRskB" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="liaEvtB" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="accAdmTypCt" type="{http://www.w3.org/2001/XMLSchema}short" minOccurs="0"/&gt;
 *         &lt;element name="parSecNf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="workingOrCat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SCORTtySection", propOrder = {
    "ctrNf",
    "secNf",
    "uwyNf",
    "uwNt",
    "endNt",
    "seclabLm",
    "secstsCt",
    "usgaapCt",
    "garCf",
    "secaccstsCt",
    "lobCf",
    "lobGl",
    "sobCf",
    "sobGl",
    "topCf",
    "topGl",
    "natCf",
    "ctrnatGd",
    "ridshaR",
    "actshaR",
    "sigshaR",
    "expshaR",
    "earexpB",
    "strexpB",
    "fldexpB",
    "speaccprmcurCf",
    "egpcurCf",
    "speacclimcurCf",
    "seccanD",
    "prmrRunOffB",
    "clmrRunOffB",
    "prmrCutOffB",
    "clmfCutOffB",
    "runOffYear",
    "liaRskB",
    "liaEvtB",
    "accAdmTypCt",
    "parSecNf",
    "workingOrCat"
})
public class SCORTtySection {

    @XmlElement(required = true)
    protected String ctrNf;
    protected int secNf;
    protected Integer uwyNf;
    @XmlElement(defaultValue = "0")
    protected int uwNt;
    @XmlElement(defaultValue = "0")
    protected int endNt;
    @XmlElement(required = true)
    protected String seclabLm;
    protected int secstsCt;
    protected Integer usgaapCt;
    @XmlElement(required = true)
    protected String garCf;
    protected int secaccstsCt;
    @XmlElement(required = true)
    protected String lobCf;
    @XmlElement(required = true)
    protected String lobGl;
    @XmlElement(required = true)
    protected String sobCf;
    @XmlElement(required = true)
    protected String sobGl;
    @XmlElement(required = true)
    protected String topCf;
    @XmlElement(required = true)
    protected String topGl;
    @XmlElement(required = true)
    protected String natCf;
    @XmlElement(required = true)
    protected String ctrnatGd;
    protected BigDecimal ridshaR;
    protected BigDecimal actshaR;
    protected BigDecimal sigshaR;
    protected BigDecimal expshaR;
    @XmlElement(defaultValue = "0")
    protected int earexpB;
    @XmlElement(defaultValue = "0")
    protected int strexpB;
    @XmlElement(defaultValue = "0")
    protected int fldexpB;
    protected String speaccprmcurCf;
    protected String egpcurCf;
    protected String speacclimcurCf;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar seccanD;
    protected Boolean prmrRunOffB;
    protected Boolean clmrRunOffB;
    protected Boolean prmrCutOffB;
    protected Boolean clmfCutOffB;
    protected Short runOffYear;
    protected Boolean liaRskB;
    protected Boolean liaEvtB;
    protected Short accAdmTypCt;
    @XmlElement(required = true)
    protected String parSecNf;
    protected String workingOrCat;

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
     * Obtient la valeur de la propriété seclabLm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeclabLm() {
        return seclabLm;
    }

    /**
     * Définit la valeur de la propriété seclabLm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeclabLm(String value) {
        this.seclabLm = value;
    }

    /**
     * Obtient la valeur de la propriété secstsCt.
     * 
     */
    public int getSecstsCt() {
        return secstsCt;
    }

    /**
     * Définit la valeur de la propriété secstsCt.
     * 
     */
    public void setSecstsCt(int value) {
        this.secstsCt = value;
    }

    /**
     * Obtient la valeur de la propriété usgaapCt.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUsgaapCt() {
        return usgaapCt;
    }

    /**
     * Définit la valeur de la propriété usgaapCt.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUsgaapCt(Integer value) {
        this.usgaapCt = value;
    }

    /**
     * Obtient la valeur de la propriété garCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGarCf() {
        return garCf;
    }

    /**
     * Définit la valeur de la propriété garCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGarCf(String value) {
        this.garCf = value;
    }

    /**
     * Obtient la valeur de la propriété secaccstsCt.
     * 
     */
    public int getSecaccstsCt() {
        return secaccstsCt;
    }

    /**
     * Définit la valeur de la propriété secaccstsCt.
     * 
     */
    public void setSecaccstsCt(int value) {
        this.secaccstsCt = value;
    }

    /**
     * Obtient la valeur de la propriété lobCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLobCf() {
        return lobCf;
    }

    /**
     * Définit la valeur de la propriété lobCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLobCf(String value) {
        this.lobCf = value;
    }

    /**
     * Obtient la valeur de la propriété lobGl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLobGl() {
        return lobGl;
    }

    /**
     * Définit la valeur de la propriété lobGl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLobGl(String value) {
        this.lobGl = value;
    }

    /**
     * Obtient la valeur de la propriété sobCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSobCf() {
        return sobCf;
    }

    /**
     * Définit la valeur de la propriété sobCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSobCf(String value) {
        this.sobCf = value;
    }

    /**
     * Obtient la valeur de la propriété sobGl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSobGl() {
        return sobGl;
    }

    /**
     * Définit la valeur de la propriété sobGl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSobGl(String value) {
        this.sobGl = value;
    }

    /**
     * Obtient la valeur de la propriété topCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopCf() {
        return topCf;
    }

    /**
     * Définit la valeur de la propriété topCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopCf(String value) {
        this.topCf = value;
    }

    /**
     * Obtient la valeur de la propriété topGl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopGl() {
        return topGl;
    }

    /**
     * Définit la valeur de la propriété topGl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopGl(String value) {
        this.topGl = value;
    }

    /**
     * Obtient la valeur de la propriété natCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatCf() {
        return natCf;
    }

    /**
     * Définit la valeur de la propriété natCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatCf(String value) {
        this.natCf = value;
    }

    /**
     * Obtient la valeur de la propriété ctrnatGd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtrnatGd() {
        return ctrnatGd;
    }

    /**
     * Définit la valeur de la propriété ctrnatGd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtrnatGd(String value) {
        this.ctrnatGd = value;
    }

    /**
     * Obtient la valeur de la propriété ridshaR.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRidshaR() {
        return ridshaR;
    }

    /**
     * Définit la valeur de la propriété ridshaR.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRidshaR(BigDecimal value) {
        this.ridshaR = value;
    }

    /**
     * Obtient la valeur de la propriété actshaR.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getActshaR() {
        return actshaR;
    }

    /**
     * Définit la valeur de la propriété actshaR.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setActshaR(BigDecimal value) {
        this.actshaR = value;
    }

    /**
     * Obtient la valeur de la propriété sigshaR.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSigshaR() {
        return sigshaR;
    }

    /**
     * Définit la valeur de la propriété sigshaR.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSigshaR(BigDecimal value) {
        this.sigshaR = value;
    }

    /**
     * Obtient la valeur de la propriété expshaR.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getExpshaR() {
        return expshaR;
    }

    /**
     * Définit la valeur de la propriété expshaR.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setExpshaR(BigDecimal value) {
        this.expshaR = value;
    }

    /**
     * Obtient la valeur de la propriété earexpB.
     * 
     */
    public int getEarexpB() {
        return earexpB;
    }

    /**
     * Définit la valeur de la propriété earexpB.
     * 
     */
    public void setEarexpB(int value) {
        this.earexpB = value;
    }

    /**
     * Obtient la valeur de la propriété strexpB.
     * 
     */
    public int getStrexpB() {
        return strexpB;
    }

    /**
     * Définit la valeur de la propriété strexpB.
     * 
     */
    public void setStrexpB(int value) {
        this.strexpB = value;
    }

    /**
     * Obtient la valeur de la propriété fldexpB.
     * 
     */
    public int getFldexpB() {
        return fldexpB;
    }

    /**
     * Définit la valeur de la propriété fldexpB.
     * 
     */
    public void setFldexpB(int value) {
        this.fldexpB = value;
    }

    /**
     * Obtient la valeur de la propriété speaccprmcurCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpeaccprmcurCf() {
        return speaccprmcurCf;
    }

    /**
     * Définit la valeur de la propriété speaccprmcurCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpeaccprmcurCf(String value) {
        this.speaccprmcurCf = value;
    }

    /**
     * Obtient la valeur de la propriété egpcurCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEgpcurCf() {
        return egpcurCf;
    }

    /**
     * Définit la valeur de la propriété egpcurCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEgpcurCf(String value) {
        this.egpcurCf = value;
    }

    /**
     * Obtient la valeur de la propriété speacclimcurCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpeacclimcurCf() {
        return speacclimcurCf;
    }

    /**
     * Définit la valeur de la propriété speacclimcurCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpeacclimcurCf(String value) {
        this.speacclimcurCf = value;
    }

    /**
     * Obtient la valeur de la propriété seccanD.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSeccanD() {
        return seccanD;
    }

    /**
     * Définit la valeur de la propriété seccanD.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSeccanD(XMLGregorianCalendar value) {
        this.seccanD = value;
    }

    /**
     * Obtient la valeur de la propriété prmrRunOffB.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPrmrRunOffB() {
        return prmrRunOffB;
    }

    /**
     * Définit la valeur de la propriété prmrRunOffB.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPrmrRunOffB(Boolean value) {
        this.prmrRunOffB = value;
    }

    /**
     * Obtient la valeur de la propriété clmrRunOffB.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isClmrRunOffB() {
        return clmrRunOffB;
    }

    /**
     * Définit la valeur de la propriété clmrRunOffB.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setClmrRunOffB(Boolean value) {
        this.clmrRunOffB = value;
    }

    /**
     * Obtient la valeur de la propriété prmrCutOffB.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPrmrCutOffB() {
        return prmrCutOffB;
    }

    /**
     * Définit la valeur de la propriété prmrCutOffB.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPrmrCutOffB(Boolean value) {
        this.prmrCutOffB = value;
    }

    /**
     * Obtient la valeur de la propriété clmfCutOffB.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isClmfCutOffB() {
        return clmfCutOffB;
    }

    /**
     * Définit la valeur de la propriété clmfCutOffB.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setClmfCutOffB(Boolean value) {
        this.clmfCutOffB = value;
    }

    /**
     * Obtient la valeur de la propriété runOffYear.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getRunOffYear() {
        return runOffYear;
    }

    /**
     * Définit la valeur de la propriété runOffYear.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setRunOffYear(Short value) {
        this.runOffYear = value;
    }

    /**
     * Obtient la valeur de la propriété liaRskB.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLiaRskB() {
        return liaRskB;
    }

    /**
     * Définit la valeur de la propriété liaRskB.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLiaRskB(Boolean value) {
        this.liaRskB = value;
    }

    /**
     * Obtient la valeur de la propriété liaEvtB.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLiaEvtB() {
        return liaEvtB;
    }

    /**
     * Définit la valeur de la propriété liaEvtB.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLiaEvtB(Boolean value) {
        this.liaEvtB = value;
    }

    /**
     * Obtient la valeur de la propriété accAdmTypCt.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getAccAdmTypCt() {
        return accAdmTypCt;
    }

    /**
     * Définit la valeur de la propriété accAdmTypCt.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setAccAdmTypCt(Short value) {
        this.accAdmTypCt = value;
    }

    /**
     * Obtient la valeur de la propriété parSecNf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParSecNf() {
        return parSecNf;
    }

    /**
     * Définit la valeur de la propriété parSecNf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParSecNf(String value) {
        this.parSecNf = value;
    }

    /**
     * Obtient la valeur de la propriété workingOrCat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkingOrCat() {
        return workingOrCat;
    }

    /**
     * Définit la valeur de la propriété workingOrCat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkingOrCat(String value) {
        this.workingOrCat = value;
    }

}
