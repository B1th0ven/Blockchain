
package com.scor.ref_treaty.schemas._1;

import javax.xml.bind.annotation.*;


/**
 * <p>Classe Java pour findTtyListByIdRequest complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="findTtyListByIdRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ssdCfList" type="{http://scor.com/ref-treaty/schemas/2.0}ssdCfList" minOccurs="0"/>
 *         &lt;element name="esbCfList" type="{http://scor.com/ref-treaty/schemas/2.0}esbCfList" minOccurs="0"/>
 *         &lt;element name="cedNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="clishonamLd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ctrNfList" type="{http://scor.com/ref-treaty/schemas/2.0}ctrNfList" minOccurs="0"/>
 *         &lt;element name="ctrpcpnamLl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ctrstsCtList" type="{http://scor.com/ref-treaty/schemas/2.0}ctrstsCtList" minOccurs="0"/>
 *         &lt;element name="uwgrpCfList" type="{http://scor.com/ref-treaty/schemas/2.0}uwgrpCfList" minOccurs="0"/>
 *         &lt;element name="lifeCf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findTtyListByIdRequest", propOrder = {
    "ssdCfList",
    "esbCfList",
    "cedNf",
    "clishonamLd",
    "ctrNfList",
    "ctrpcpnamLl",
    "ctrstsCtList",
    "uwgrpCfList",
    "lifeCf"
})
@XmlRootElement(name = "FindTtyListByIdRequest")
//@XmlSeeAlso({EsbCfList.class,SsdCfList.class,CtrNfList.class,CtrstsCtList.class,UwgrpCfList.class})
public class FindTtyListByIdRequest {

    @XmlElement(name = "SsdCfList")
    protected SsdCfList ssdCfList;
    @XmlElement(name = "EsbCfList")
    protected EsbCfList esbCfList;
    protected Integer cedNf;
    protected String clishonamLd;
    @XmlElement(name = "CtrNfList")
    protected CtrNfList ctrNfList;
    protected String ctrpcpnamLl;
    @XmlElement(name = "CtrstsCtList")
    protected CtrstsCtList ctrstsCtList;
    @XmlElement(name = "UwgrpCfList")
    protected UwgrpCfList uwgrpCfList;
    protected Integer lifeCf;

    /**
     * Obtient la valeur de la propriété ssdCfList.
     * 
     * @return
     *     possible object is
     *     {@link SsdCfList }
     *     
     */
    public SsdCfList getSsdCfList() {
        return ssdCfList;
    }

    /**
     * Définit la valeur de la propriété ssdCfList.
     * 
     * @param value
     *     allowed object is
     *     {@link SsdCfList }
     *     
     */
    public void setSsdCfList(SsdCfList value) {
        this.ssdCfList = value;
    }

    /**
     * Obtient la valeur de la propriété esbCfList.
     * 
     * @return
     *     possible object is
     *     {@link EsbCfList }
     *     
     */
    public EsbCfList getEsbCfList() {
        return esbCfList;
    }

    /**
     * Définit la valeur de la propriété esbCfList.
     * 
     * @param value
     *     allowed object is
     *     {@link EsbCfList }
     *     
     */
    public void setEsbCfList(EsbCfList value) {
        this.esbCfList = value;
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
     * Obtient la valeur de la propriété ctrNfList.
     * 
     * @return
     *     possible object is
     *     {@link CtrNfList }
     *     
     */
    public CtrNfList getCtrNfList() {
        return ctrNfList;
    }

    /**
     * Définit la valeur de la propriété ctrNfList.
     * 
     * @param value
     *     allowed object is
     *     {@link CtrNfList }
     *     
     */
    public void setCtrNfList(CtrNfList value) {
        this.ctrNfList = value;
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
     * Obtient la valeur de la propriété ctrstsCtList.
     * 
     * @return
     *     possible object is
     *     {@link CtrstsCtList }
     *     
     */
    public CtrstsCtList getCtrstsCtList() {
        return ctrstsCtList;
    }

    /**
     * Définit la valeur de la propriété ctrstsCtList.
     * 
     * @param value
     *     allowed object is
     *     {@link CtrstsCtList }
     *     
     */
    public void setCtrstsCtList(CtrstsCtList value) {
        this.ctrstsCtList = value;
    }

    /**
     * Obtient la valeur de la propriété uwgrpCfList.
     * 
     * @return
     *     possible object is
     *     {@link UwgrpCfList }
     *     
     */
    public UwgrpCfList getUwgrpCfList() {
        return uwgrpCfList;
    }

    /**
     * Définit la valeur de la propriété uwgrpCfList.
     * 
     * @param value
     *     allowed object is
     *     {@link UwgrpCfList }
     *     
     */
    public void setUwgrpCfList(UwgrpCfList value) {
        this.uwgrpCfList = value;
    }

    /**
     * Obtient la valeur de la propriété lifeCf.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLifeCf() {
        return lifeCf;
    }

    /**
     * Définit la valeur de la propriété lifeCf.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLifeCf(Integer value) {
        this.lifeCf = value;
    }

}
