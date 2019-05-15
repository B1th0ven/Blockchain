package com.scor.ref_treaty.schemas._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour findTtySectionListByIdRequest complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="findTtySectionListByIdRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ctrNf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="secNf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="lagCf" type="{http://scor.com/ref-treaty/schemas/1.1}character"/&gt;
 *         &lt;element name="uwyNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="uwNt" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="endNt" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findTtySectionListByIdRequest", propOrder = {
    "ctrNf",
    "secNf",
    "lagCf",
    "uwyNf",
    "uwNt",
    "endNt"
})
@XmlRootElement(name = "FindTtySectionListByIdRequest")
public class FindTtySectionListByIdRequest {

    @XmlElement(required = true)
    protected String ctrNf;
    protected int secNf;
    @XmlElement(required = true)
    protected String lagCf;
    protected Integer uwyNf;
    protected Integer uwNt;
    protected Integer endNt;

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
     * Obtient la valeur de la propriété lagCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLagCf() {
        return lagCf;
    }

    /**
     * Définit la valeur de la propriété lagCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLagCf(String value) {
        this.lagCf = value;
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
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUwNt() {
        return uwNt;
    }

    /**
     * Définit la valeur de la propriété uwNt.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUwNt(Integer value) {
        this.uwNt = value;
    }

    /**
     * Obtient la valeur de la propriété endNt.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEndNt() {
        return endNt;
    }

    /**
     * Définit la valeur de la propriété endNt.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEndNt(Integer value) {
        this.endNt = value;
    }

}
