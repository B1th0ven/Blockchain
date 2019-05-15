package com.scor.ref_treaty.schemas._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour findTtyGuaranteeByIdRequest complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="findTtyGuaranteeByIdRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ctrNf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ttySectionIdList" type="{http://scor.com/ref-treaty/schemas/1.1}ttySectionIdList"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findTtyGuaranteeByIdRequest", propOrder = {
    "ctrNf",
    "ttySectionIdList"
})
@XmlRootElement(name = "FindTtyGuaranteeByIdRequest")
public class FindTtyGuaranteeByIdRequest {

    @XmlElement(required = true)
    protected String ctrNf;
    @XmlElement(required = true)
    protected TtySectionIdList ttySectionIdList;

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
     * Obtient la valeur de la propriété ttySectionIdList.
     * 
     * @return
     *     possible object is
     *     {@link TtySectionIdList }
     *     
     */
    public TtySectionIdList getTtySectionIdList() {
        return ttySectionIdList;
    }

    /**
     * Définit la valeur de la propriété ttySectionIdList.
     * 
     * @param value
     *     allowed object is
     *     {@link TtySectionIdList }
     *     
     */
    public void setTtySectionIdList(TtySectionIdList value) {
        this.ttySectionIdList = value;
    }

}
