package com.scor.ref_treaty.schemas._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour SCORTtyCountryPeril complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="SCORTtyCountryPeril"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ctrNf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="secNf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="uwyNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="uwNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="endNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="catlinNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ctyCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="peril" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SCORTtyCountryPeril", propOrder = {
    "ctrNf",
    "secNf",
    "uwyNf",
    "uwNt",
    "endNt",
    "catlinNt",
    "ctyCf",
    "peril"
})
public class SCORTtyCountryPeril {

    @XmlElement(required = true)
    protected String ctrNf;
    protected int secNf;
    protected Integer uwyNf;
    @XmlElement(defaultValue = "0")
    protected int uwNt;
    @XmlElement(defaultValue = "0")
    protected int endNt;
    protected int catlinNt;
    @XmlElement(required = true)
    protected String ctyCf;
    @XmlElement(required = true)
    protected String peril;

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
     * Obtient la valeur de la propriété catlinNt.
     * 
     */
    public int getCatlinNt() {
        return catlinNt;
    }

    /**
     * Définit la valeur de la propriété catlinNt.
     * 
     */
    public void setCatlinNt(int value) {
        this.catlinNt = value;
    }

    /**
     * Obtient la valeur de la propriété ctyCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtyCf() {
        return ctyCf;
    }

    /**
     * Définit la valeur de la propriété ctyCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtyCf(String value) {
        this.ctyCf = value;
    }

    /**
     * Obtient la valeur de la propriété peril.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeril() {
        return peril;
    }

    /**
     * Définit la valeur de la propriété peril.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeril(String value) {
        this.peril = value;
    }

}
