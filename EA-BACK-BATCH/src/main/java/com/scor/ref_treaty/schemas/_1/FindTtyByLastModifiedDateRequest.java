package com.scor.ref_treaty.schemas._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour findTtyByLastModifiedDateRequest complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="findTtyByLastModifiedDateRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="lstupdD" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="canDt" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="ctrtypCt" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="uwgrpCf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="uwyNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findTtyByLastModifiedDateRequest", propOrder = {
    "lstupdD",
    "canDt",
    "ctrtypCt",
    "uwgrpCf",
    "uwyNf"
})
@XmlRootElement(name = "FindTtyByLastModifiedDateRequest")
public class FindTtyByLastModifiedDateRequest {

    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar lstupdD;
    protected Integer canDt;
    protected Integer ctrtypCt;
    protected Integer uwgrpCf;
    protected Integer uwyNf;

    /**
     * Obtient la valeur de la propriété lstupdD.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLstupdD() {
        return lstupdD;
    }

    /**
     * Définit la valeur de la propriété lstupdD.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLstupdD(XMLGregorianCalendar value) {
        this.lstupdD = value;
    }

    /**
     * Obtient la valeur de la propriété canDt.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCanDt() {
        return canDt;
    }

    /**
     * Définit la valeur de la propriété canDt.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCanDt(Integer value) {
        this.canDt = value;
    }

    /**
     * Obtient la valeur de la propriété ctrtypCt.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCtrtypCt() {
        return ctrtypCt;
    }

    /**
     * Définit la valeur de la propriété ctrtypCt.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCtrtypCt(Integer value) {
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

}
