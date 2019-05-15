package com.scor.ref_treaty.schemas._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour SCORTtySectionLabel complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="SCORTtySectionLabel"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ctrNf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="secNf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="seclabLm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="uwyNf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="uwNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="endNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SCORTtySectionLabel", propOrder = {
    "ctrNf",
    "secNf",
    "seclabLm",
    "uwyNf",
    "uwNt",
    "endNt"
})
public class SCORTtySectionLabel {

    @XmlElement(required = true)
    protected String ctrNf;
    protected int secNf;
    protected String seclabLm;
    protected int uwyNf;
    protected int uwNt;
    protected int endNt;

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
     * Obtient la valeur de la propriété uwyNf.
     * 
     */
    public int getUwyNf() {
        return uwyNf;
    }

    /**
     * Définit la valeur de la propriété uwyNf.
     * 
     */
    public void setUwyNf(int value) {
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

}
