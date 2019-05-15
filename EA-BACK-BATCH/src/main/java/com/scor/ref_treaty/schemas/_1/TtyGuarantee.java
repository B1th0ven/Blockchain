package com.scor.ref_treaty.schemas._1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour ttyGuarantee complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="ttyGuarantee"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ctrNf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="secNf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="uwyNf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="uwNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="endNt" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="vntCtrLinN" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="vntCtrValCt" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="incExcValB" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="egpPcpR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="garGl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ttyGuarantee", propOrder = {
    "ctrNf",
    "secNf",
    "uwyNf",
    "uwNt",
    "endNt",
    "vntCtrLinN",
    "vntCtrValCt",
    "incExcValB",
    "egpPcpR",
    "garGl"
})
public class TtyGuarantee {

    @XmlElement(required = true)
    protected String ctrNf;
    protected int secNf;
    protected int uwyNf;
    protected int uwNt;
    protected int endNt;
    protected int vntCtrLinN;
    @XmlElement(required = true)
    protected String vntCtrValCt;
    protected boolean incExcValB;
    protected BigDecimal egpPcpR;
    protected String garGl;

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

    /**
     * Obtient la valeur de la propriété vntCtrLinN.
     * 
     */
    public int getVntCtrLinN() {
        return vntCtrLinN;
    }

    /**
     * Définit la valeur de la propriété vntCtrLinN.
     * 
     */
    public void setVntCtrLinN(int value) {
        this.vntCtrLinN = value;
    }

    /**
     * Obtient la valeur de la propriété vntCtrValCt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVntCtrValCt() {
        return vntCtrValCt;
    }

    /**
     * Définit la valeur de la propriété vntCtrValCt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVntCtrValCt(String value) {
        this.vntCtrValCt = value;
    }

    /**
     * Obtient la valeur de la propriété incExcValB.
     * 
     */
    public boolean isIncExcValB() {
        return incExcValB;
    }

    /**
     * Définit la valeur de la propriété incExcValB.
     * 
     */
    public void setIncExcValB(boolean value) {
        this.incExcValB = value;
    }

    /**
     * Obtient la valeur de la propriété egpPcpR.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEgpPcpR() {
        return egpPcpR;
    }

    /**
     * Définit la valeur de la propriété egpPcpR.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEgpPcpR(BigDecimal value) {
        this.egpPcpR = value;
    }

    /**
     * Obtient la valeur de la propriété garGl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGarGl() {
        return garGl;
    }

    /**
     * Définit la valeur de la propriété garGl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGarGl(String value) {
        this.garGl = value;
    }

}
