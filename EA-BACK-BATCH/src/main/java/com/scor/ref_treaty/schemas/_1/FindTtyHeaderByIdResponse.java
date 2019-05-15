package com.scor.ref_treaty.schemas._1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour findTtyHeaderByIdResponse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="findTtyHeaderByIdResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ctrNf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ctrstsCt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ctrtypCt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="uwgrpCf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="grpLl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="uwyNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="uwNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="endNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ctrpcpnamLl" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ssdCf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ssdLl" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="accesbCf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="esbLl" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ctrincD" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="ctrexpD" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="prgNf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="prgnamLl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="boqNf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="boqnamLl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cedNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="clishonamLd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="clictyCf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="uwrspusrCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="usrfnmeLm" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="usrnmeLm" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="liacurCf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="lialimM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="liaevtlimM" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="reitypCf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="orgcedNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="liftrttypCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="manretB" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="prdNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="prdclishoNamLd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findTtyHeaderByIdResponse", propOrder = {
    "ctrNf",
    "ctrstsCt",
    "ctrtypCt",
    "uwgrpCf",
    "grpLl",
    "uwyNf",
    "uwNt",
    "endNt",
    "ctrpcpnamLl",
    "ssdCf",
    "ssdLl",
    "accesbCf",
    "esbLl",
    "ctrincD",
    "ctrexpD",
    "prgNf",
    "prgnamLl",
    "boqNf",
    "boqnamLl",
    "cedNf",
    "clishonamLd",
    "clictyCf",
    "uwrspusrCf",
    "usrfnmeLm",
    "usrnmeLm",
    "liacurCf",
    "lialimM",
    "liaevtlimM",
    "reitypCf",
    "orgcedNf",
    "liftrttypCf",
    "manretB",
    "prdNf",
    "prdclishoNamLd"
})
@XmlRootElement(name = "FindTtyHeaderByIdResponse")
public class FindTtyHeaderByIdResponse {

    @XmlElement(required = true)
    protected String ctrNf;
    @XmlElement(defaultValue = "0")
    protected int ctrstsCt;
    @XmlElement(defaultValue = "0")
    protected int ctrtypCt;
    protected Integer uwgrpCf;
    protected String grpLl;
    protected Integer uwyNf;
    @XmlElement(defaultValue = "0")
    protected int uwNt;
    @XmlElement(defaultValue = "0")
    protected int endNt;
    @XmlElement(required = true)
    protected String ctrpcpnamLl;
    protected int ssdCf;
    @XmlElement(required = true)
    protected String ssdLl;
    protected int accesbCf;
    @XmlElement(required = true)
    protected String esbLl;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar ctrincD;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar ctrexpD;
    @XmlElement(required = true)
    protected String prgNf;
    protected String prgnamLl;
    @XmlElement(required = true)
    protected String boqNf;
    protected String boqnamLl;
    protected Integer cedNf;
    protected String clishonamLd;
    protected String clictyCf;
    @XmlElement(required = true)
    protected String uwrspusrCf;
    @XmlElement(required = true)
    protected String usrfnmeLm;
    @XmlElement(required = true)
    protected String usrnmeLm;
    protected String liacurCf;
    protected BigDecimal lialimM;
    protected BigDecimal liaevtlimM;
    protected Integer reitypCf;
    protected Integer orgcedNf;
    @XmlElement(required = true)
    protected String liftrttypCf;
    protected boolean manretB;
    protected Integer prdNf;
    protected String prdclishoNamLd;

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
     * Obtient la valeur de la propriété ctrstsCt.
     * 
     */
    public int getCtrstsCt() {
        return ctrstsCt;
    }

    /**
     * Définit la valeur de la propriété ctrstsCt.
     * 
     */
    public void setCtrstsCt(int value) {
        this.ctrstsCt = value;
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
     * Obtient la valeur de la propriété grpLl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrpLl() {
        return grpLl;
    }

    /**
     * Définit la valeur de la propriété grpLl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrpLl(String value) {
        this.grpLl = value;
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
     * Obtient la valeur de la propriété ctrpcpnamLl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtrpcpnamLl() {
        return ctrpcpnamLl;
    }

    /**
     * Définit la valeur de la propriété ctrpcpnamLl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtrpcpnamLl(String value) {
        this.ctrpcpnamLl = value;
    }

    /**
     * Obtient la valeur de la propriété ssdCf.
     * 
     */
    public int getSsdCf() {
        return ssdCf;
    }

    /**
     * Définit la valeur de la propriété ssdCf.
     * 
     */
    public void setSsdCf(int value) {
        this.ssdCf = value;
    }

    /**
     * Obtient la valeur de la propriété ssdLl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSsdLl() {
        return ssdLl;
    }

    /**
     * Définit la valeur de la propriété ssdLl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSsdLl(String value) {
        this.ssdLl = value;
    }

    /**
     * Obtient la valeur de la propriété accesbCf.
     * 
     */
    public int getAccesbCf() {
        return accesbCf;
    }

    /**
     * Définit la valeur de la propriété accesbCf.
     * 
     */
    public void setAccesbCf(int value) {
        this.accesbCf = value;
    }

    /**
     * Obtient la valeur de la propriété esbLl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsbLl() {
        return esbLl;
    }

    /**
     * Définit la valeur de la propriété esbLl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsbLl(String value) {
        this.esbLl = value;
    }

    /**
     * Obtient la valeur de la propriété ctrincD.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCtrincD() {
        return ctrincD;
    }

    /**
     * Définit la valeur de la propriété ctrincD.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCtrincD(XMLGregorianCalendar value) {
        this.ctrincD = value;
    }

    /**
     * Obtient la valeur de la propriété ctrexpD.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCtrexpD() {
        return ctrexpD;
    }

    /**
     * Définit la valeur de la propriété ctrexpD.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCtrexpD(XMLGregorianCalendar value) {
        this.ctrexpD = value;
    }

    /**
     * Obtient la valeur de la propriété prgNf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrgNf() {
        return prgNf;
    }

    /**
     * Définit la valeur de la propriété prgNf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrgNf(String value) {
        this.prgNf = value;
    }

    /**
     * Obtient la valeur de la propriété prgnamLl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrgnamLl() {
        return prgnamLl;
    }

    /**
     * Définit la valeur de la propriété prgnamLl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrgnamLl(String value) {
        this.prgnamLl = value;
    }

    /**
     * Obtient la valeur de la propriété boqNf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBoqNf() {
        return boqNf;
    }

    /**
     * Définit la valeur de la propriété boqNf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBoqNf(String value) {
        this.boqNf = value;
    }

    /**
     * Obtient la valeur de la propriété boqnamLl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBoqnamLl() {
        return boqnamLl;
    }

    /**
     * Définit la valeur de la propriété boqnamLl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBoqnamLl(String value) {
        this.boqnamLl = value;
    }

    /**
     * Obtient la valeur de la propriété cedNf.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCedNf() {
        return cedNf;
    }

    /**
     * Définit la valeur de la propriété cedNf.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCedNf(Integer value) {
        this.cedNf = value;
    }

    /**
     * Obtient la valeur de la propriété clishonamLd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClishonamLd() {
        return clishonamLd;
    }

    /**
     * Définit la valeur de la propriété clishonamLd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClishonamLd(String value) {
        this.clishonamLd = value;
    }

    /**
     * Obtient la valeur de la propriété clictyCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClictyCf() {
        return clictyCf;
    }

    /**
     * Définit la valeur de la propriété clictyCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClictyCf(String value) {
        this.clictyCf = value;
    }

    /**
     * Obtient la valeur de la propriété uwrspusrCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUwrspusrCf() {
        return uwrspusrCf;
    }

    /**
     * Définit la valeur de la propriété uwrspusrCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUwrspusrCf(String value) {
        this.uwrspusrCf = value;
    }

    /**
     * Obtient la valeur de la propriété usrfnmeLm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsrfnmeLm() {
        return usrfnmeLm;
    }

    /**
     * Définit la valeur de la propriété usrfnmeLm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsrfnmeLm(String value) {
        this.usrfnmeLm = value;
    }

    /**
     * Obtient la valeur de la propriété usrnmeLm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsrnmeLm() {
        return usrnmeLm;
    }

    /**
     * Définit la valeur de la propriété usrnmeLm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsrnmeLm(String value) {
        this.usrnmeLm = value;
    }

    /**
     * Obtient la valeur de la propriété liacurCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLiacurCf() {
        return liacurCf;
    }

    /**
     * Définit la valeur de la propriété liacurCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLiacurCf(String value) {
        this.liacurCf = value;
    }

    /**
     * Obtient la valeur de la propriété lialimM.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLialimM() {
        return lialimM;
    }

    /**
     * Définit la valeur de la propriété lialimM.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLialimM(BigDecimal value) {
        this.lialimM = value;
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
     * Obtient la valeur de la propriété reitypCf.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReitypCf() {
        return reitypCf;
    }

    /**
     * Définit la valeur de la propriété reitypCf.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReitypCf(Integer value) {
        this.reitypCf = value;
    }

    /**
     * Obtient la valeur de la propriété orgcedNf.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOrgcedNf() {
        return orgcedNf;
    }

    /**
     * Définit la valeur de la propriété orgcedNf.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOrgcedNf(Integer value) {
        this.orgcedNf = value;
    }

    /**
     * Obtient la valeur de la propriété liftrttypCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLiftrttypCf() {
        return liftrttypCf;
    }

    /**
     * Définit la valeur de la propriété liftrttypCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLiftrttypCf(String value) {
        this.liftrttypCf = value;
    }

    /**
     * Obtient la valeur de la propriété manretB.
     * 
     */
    public boolean isManretB() {
        return manretB;
    }

    /**
     * Définit la valeur de la propriété manretB.
     * 
     */
    public void setManretB(boolean value) {
        this.manretB = value;
    }

    /**
     * Obtient la valeur de la propriété prdNf.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPrdNf() {
        return prdNf;
    }

    /**
     * Définit la valeur de la propriété prdNf.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPrdNf(Integer value) {
        this.prdNf = value;
    }

    /**
     * Obtient la valeur de la propriété prdclishoNamLd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrdclishoNamLd() {
        return prdclishoNamLd;
    }

    /**
     * Définit la valeur de la propriété prdclishoNamLd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrdclishoNamLd(String value) {
        this.prdclishoNamLd = value;
    }

}
