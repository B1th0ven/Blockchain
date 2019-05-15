
package com.scor.ref_treaty.schemas._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour SCORTty complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="SCORTty">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ssdCf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="esbCf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="cedNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="clishonamLd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ctrNf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ctrpcpnamLl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uwyNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="uwgrpCf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="admgrpCF" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="accgrpCF" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ctrstsCt" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ctrlckB" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SCORTty", propOrder = {
    "ssdCf",
    "esbCf",
    "cedNf",
    "clishonamLd",
    "ctrNf",
    "ctrpcpnamLl",
    "uwyNf",
    "uwgrpCf",
    "admgrpCF",
    "accgrpCF",
    "ctrstsCt",
    "ctrlckB"
})
@XmlRootElement(name = "SCORTty")
public class SCORTty {

    protected Integer ssdCf;
    protected Integer esbCf;
    protected Integer cedNf;
    protected String clishonamLd;
    protected String ctrNf;
    protected String ctrpcpnamLl;
    protected Integer uwyNf;
    protected Integer uwgrpCf;
    protected Integer admgrpCF;
    protected Integer accgrpCF;
    protected Integer ctrstsCt;
    protected Boolean ctrlckB;

    /**
     * Obtient la valeur de la propriété ssdCf.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSsdCf() {
        return ssdCf;
    }

    /**
     * Définit la valeur de la propriété ssdCf.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSsdCf(Integer value) {
        this.ssdCf = value;
    }

    /**
     * Obtient la valeur de la propriété esbCf.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEsbCf() {
        return esbCf;
    }

    /**
     * Définit la valeur de la propriété esbCf.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEsbCf(Integer value) {
        this.esbCf = value;
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
     * Obtient la valeur de la propriété admgrpCF.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAdmgrpCF() {
        return admgrpCF;
    }

    /**
     * Définit la valeur de la propriété admgrpCF.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAdmgrpCF(Integer value) {
        this.admgrpCF = value;
    }

    /**
     * Obtient la valeur de la propriété accgrpCF.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAccgrpCF() {
        return accgrpCF;
    }

    /**
     * Définit la valeur de la propriété accgrpCF.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAccgrpCF(Integer value) {
        this.accgrpCF = value;
    }

    /**
     * Obtient la valeur de la propriété ctrstsCt.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCtrstsCt() {
        return ctrstsCt;
    }

    /**
     * Définit la valeur de la propriété ctrstsCt.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCtrstsCt(Integer value) {
        this.ctrstsCt = value;
    }

    /**
     * Obtient la valeur de la propriété ctrlckB.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCtrlckB() {
        return ctrlckB;
    }

    /**
     * Définit la valeur de la propriété ctrlckB.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCtrlckB(Boolean value) {
        this.ctrlckB = value;
    }

}
