package com.scor.ref_client.schemas._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour CleExtRe complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="CleExtRe"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ExtDtbCliLd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ExtDtbCt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CleExtRe", propOrder = {
    "extDtbCliLd",
    "extDtbCt"
})
public class CleExtRe {

    @XmlElement(name = "ExtDtbCliLd")
    protected String extDtbCliLd;
    @XmlElement(name = "ExtDtbCt")
    protected String extDtbCt;

    /**
     * Obtient la valeur de la propriété extDtbCliLd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtDtbCliLd() {
        return extDtbCliLd;
    }

    /**
     * Définit la valeur de la propriété extDtbCliLd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtDtbCliLd(String value) {
        this.extDtbCliLd = value;
    }

    /**
     * Obtient la valeur de la propriété extDtbCt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtDtbCt() {
        return extDtbCt;
    }

    /**
     * Définit la valeur de la propriété extDtbCt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtDtbCt(String value) {
        this.extDtbCt = value;
    }

}
