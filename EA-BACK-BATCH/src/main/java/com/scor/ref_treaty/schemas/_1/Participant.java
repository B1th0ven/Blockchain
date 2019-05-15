package com.scor.ref_treaty.schemas._1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour participant complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="participant"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="risNf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="risName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ldiB" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="risshaR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "participant", propOrder = {
    "risNf",
    "risName",
    "ldiB",
    "risshaR"
})
public class Participant {

    protected int risNf;
    @XmlElement(required = true)
    protected String risName;
    protected boolean ldiB;
    protected BigDecimal risshaR;

    /**
     * Obtient la valeur de la propriété risNf.
     * 
     */
    public int getRisNf() {
        return risNf;
    }

    /**
     * Définit la valeur de la propriété risNf.
     * 
     */
    public void setRisNf(int value) {
        this.risNf = value;
    }

    /**
     * Obtient la valeur de la propriété risName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRisName() {
        return risName;
    }

    /**
     * Définit la valeur de la propriété risName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRisName(String value) {
        this.risName = value;
    }

    /**
     * Obtient la valeur de la propriété ldiB.
     * 
     */
    public boolean isLdiB() {
        return ldiB;
    }

    /**
     * Définit la valeur de la propriété ldiB.
     * 
     */
    public void setLdiB(boolean value) {
        this.ldiB = value;
    }

    /**
     * Obtient la valeur de la propriété risshaR.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRisshaR() {
        return risshaR;
    }

    /**
     * Définit la valeur de la propriété risshaR.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRisshaR(BigDecimal value) {
        this.risshaR = value;
    }

}
