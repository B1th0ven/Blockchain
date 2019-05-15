package com.scor.ref_treaty.schemas._1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour SCORTtyReinstatement complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="SCORTtyReinstatement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ctrNf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="secNf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="uwyNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="uwNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="endNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="reilinNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="reirnkN" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="premium" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="proportionCeded" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="reiprotmpB" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SCORTtyReinstatement", propOrder = {
    "ctrNf",
    "secNf",
    "uwyNf",
    "uwNt",
    "endNt",
    "reilinNt",
    "reirnkN",
    "premium",
    "proportionCeded",
    "reiprotmpB"
})
public class SCORTtyReinstatement {

    @XmlElement(required = true)
    protected String ctrNf;
    protected int secNf;
    protected Integer uwyNf;
    @XmlElement(defaultValue = "0")
    protected int uwNt;
    @XmlElement(defaultValue = "0")
    protected int endNt;
    protected int reilinNt;
    protected int reirnkN;
    @XmlElement(required = true)
    protected BigDecimal premium;
    protected BigDecimal proportionCeded;
    @XmlElement(defaultValue = "0")
    protected int reiprotmpB;

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
     * Obtient la valeur de la propriété reilinNt.
     * 
     */
    public int getReilinNt() {
        return reilinNt;
    }

    /**
     * Définit la valeur de la propriété reilinNt.
     * 
     */
    public void setReilinNt(int value) {
        this.reilinNt = value;
    }

    /**
     * Obtient la valeur de la propriété reirnkN.
     * 
     */
    public int getReirnkN() {
        return reirnkN;
    }

    /**
     * Définit la valeur de la propriété reirnkN.
     * 
     */
    public void setReirnkN(int value) {
        this.reirnkN = value;
    }

    /**
     * Obtient la valeur de la propriété premium.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPremium() {
        return premium;
    }

    /**
     * Définit la valeur de la propriété premium.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPremium(BigDecimal value) {
        this.premium = value;
    }

    /**
     * Obtient la valeur de la propriété proportionCeded.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getProportionCeded() {
        return proportionCeded;
    }

    /**
     * Définit la valeur de la propriété proportionCeded.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setProportionCeded(BigDecimal value) {
        this.proportionCeded = value;
    }

    /**
     * Obtient la valeur de la propriété reiprotmpB.
     * 
     */
    public int getReiprotmpB() {
        return reiprotmpB;
    }

    /**
     * Définit la valeur de la propriété reiprotmpB.
     * 
     */
    public void setReiprotmpB(int value) {
        this.reiprotmpB = value;
    }

}
