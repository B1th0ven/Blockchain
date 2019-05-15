package com.scor.ref_client.schemas._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour FindClientByIdResponse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="FindClientByIdResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CliNf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="HldNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="HldNam1Ld" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CliLvlCt" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="CliShoNamLd" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CliStsCt" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/&gt;
 *         &lt;element name="CliMantypCt" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/&gt;
 *         &lt;element name="CliTypCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CliCityLm" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CliCtyCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CliAddLn5La" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CliAddStaCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CliAddLn3La" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CliAddLn4La" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CliActCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CliIlgst1La" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CliIlgst2La" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CliResSdCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CliResGrpcf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CliInactD" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" minOccurs="0"/&gt;
 *         &lt;element name="CliNaNatCt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CliRepCliNf" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="CliCedB" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="CliPyrB" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="CliAccB" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="CliRetB" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="CliSupIndB" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="CreUsrCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CreD" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="LstUpdUsrCf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="LstUpdD" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="HordNbrNt" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="HindNbrNt" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="SrepcriSsdCf" type="{http://www.w3.org/2001/XMLSchema}short" minOccurs="0"/&gt;
 *         &lt;element name="SrepcriSordNbrNt" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="EmbargoB" type="{http://www.w3.org/2001/XMLSchema}short" minOccurs="0"/&gt;
 *         &lt;element name="CleExtRe" type="{http://scor.com/ref-client/schemas/1.1}CleExtRe" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="CliRating" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="RatingName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RatingValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RatingEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindClientByIdResponse", propOrder = {
    "cliNf",
    "hldNf",
    "hldNam1Ld",
    "cliLvlCt",
    "cliShoNamLd",
    "cliStsCt",
    "cliMantypCt",
    "cliTypCf",
    "cliCityLm",
    "cliCtyCf",
    "cliAddLn5La",
    "cliAddStaCf",
    "cliAddLn3La",
    "cliAddLn4La",
    "cliActCf",
    "cliIlgst1La",
    "cliIlgst2La",
    "cliResSdCf",
    "cliResGrpcf",
    "cliInactD",
    "cliNaNatCt",
    "cliRepCliNf",
    "cliCedB",
    "cliPyrB",
    "cliAccB",
    "cliRetB",
    "cliSupIndB",
    "creUsrCf",
    "creD",
    "lstUpdUsrCf",
    "lstUpdD",
    "hordNbrNt",
    "hindNbrNt",
    "srepcriSsdCf",
    "srepcriSordNbrNt",
    "embargoB",
    "cleExtRe",
    "cliRating"
})
@XmlRootElement(name = "FindClientByIdResponse")
public class FindClientByIdResponse {

    @XmlElement(name = "CliNf")
    protected int cliNf;
    @XmlElement(name = "HldNf")
    protected Integer hldNf;
    @XmlElement(name = "HldNam1Ld")
    protected String hldNam1Ld;
    @XmlElement(name = "CliLvlCt")
    protected Integer cliLvlCt;
    @XmlElement(name = "CliShoNamLd", required = true)
    protected String cliShoNamLd;
    @XmlElement(name = "CliStsCt", defaultValue = "0")
    @XmlSchemaType(name = "unsignedByte")
    protected short cliStsCt;
    @XmlElement(name = "CliMantypCt", defaultValue = "1")
    @XmlSchemaType(name = "unsignedByte")
    protected short cliMantypCt;
    @XmlElement(name = "CliTypCf", required = true)
    protected String cliTypCf;
    @XmlElement(name = "CliCityLm", required = true)
    protected String cliCityLm;
    @XmlElement(name = "CliCtyCf", required = true)
    protected String cliCtyCf;
    @XmlElement(name = "CliAddLn5La", required = true)
    protected String cliAddLn5La;
    @XmlElement(name = "CliAddStaCf", required = true)
    protected String cliAddStaCf;
    @XmlElement(name = "CliAddLn3La", required = true)
    protected String cliAddLn3La;
    @XmlElement(name = "CliAddLn4La")
    protected String cliAddLn4La;
    @XmlElement(name = "CliActCf", required = true)
    protected String cliActCf;
    @XmlElement(name = "CliIlgst1La", required = true)
    protected String cliIlgst1La;
    @XmlElement(name = "CliIlgst2La")
    protected String cliIlgst2La;
    @XmlElement(name = "CliResSdCf", required = true)
    protected String cliResSdCf;
    @XmlElement(name = "CliResGrpcf", required = true)
    protected String cliResGrpcf;
    @XmlElement(name = "CliInactD")
    @XmlSchemaType(name = "anySimpleType")
    protected Object cliInactD;
    @XmlElement(name = "CliNaNatCt")
    protected String cliNaNatCt;
    @XmlElement(name = "CliRepCliNf")
    protected Integer cliRepCliNf;
    @XmlElement(name = "CliCedB", defaultValue = "0")
    protected boolean cliCedB;
    @XmlElement(name = "CliPyrB", defaultValue = "0")
    protected boolean cliPyrB;
    @XmlElement(name = "CliAccB", defaultValue = "0")
    protected boolean cliAccB;
    @XmlElement(name = "CliRetB", defaultValue = "0")
    protected boolean cliRetB;
    @XmlElement(name = "CliSupIndB", defaultValue = "0")
    protected boolean cliSupIndB;
    @XmlElement(name = "CreUsrCf", required = true)
    protected String creUsrCf;
    @XmlElement(name = "CreD", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creD;
    @XmlElement(name = "LstUpdUsrCf", required = true)
    protected String lstUpdUsrCf;
    @XmlElement(name = "LstUpdD", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lstUpdD;
    @XmlElement(name = "HordNbrNt")
    protected Integer hordNbrNt;
    @XmlElement(name = "HindNbrNt")
    protected Integer hindNbrNt;
    @XmlElement(name = "SrepcriSsdCf")
    protected Short srepcriSsdCf;
    @XmlElement(name = "SrepcriSordNbrNt")
    protected Integer srepcriSordNbrNt;
    @XmlElement(name = "EmbargoB")
    protected Short embargoB;
    @XmlElement(name = "CleExtRe")
    protected List<CleExtRe> cleExtRe;
    @XmlElement(name = "CliRating")
    protected List<FindClientByIdResponse.CliRating> cliRating;

    /**
     * Obtient la valeur de la propriété cliNf.
     * 
     */
    public int getCliNf() {
        return cliNf;
    }

    /**
     * Définit la valeur de la propriété cliNf.
     * 
     */
    public void setCliNf(int value) {
        this.cliNf = value;
    }

    /**
     * Obtient la valeur de la propriété hldNf.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHldNf() {
        return hldNf;
    }

    /**
     * Définit la valeur de la propriété hldNf.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHldNf(Integer value) {
        this.hldNf = value;
    }

    /**
     * Obtient la valeur de la propriété hldNam1Ld.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHldNam1Ld() {
        return hldNam1Ld;
    }

    /**
     * Définit la valeur de la propriété hldNam1Ld.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHldNam1Ld(String value) {
        this.hldNam1Ld = value;
    }

    /**
     * Obtient la valeur de la propriété cliLvlCt.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCliLvlCt() {
        return cliLvlCt;
    }

    /**
     * Définit la valeur de la propriété cliLvlCt.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCliLvlCt(Integer value) {
        this.cliLvlCt = value;
    }

    /**
     * Obtient la valeur de la propriété cliShoNamLd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliShoNamLd() {
        return cliShoNamLd;
    }

    /**
     * Définit la valeur de la propriété cliShoNamLd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliShoNamLd(String value) {
        this.cliShoNamLd = value;
    }

    /**
     * Obtient la valeur de la propriété cliStsCt.
     * 
     */
    public short getCliStsCt() {
        return cliStsCt;
    }

    /**
     * Définit la valeur de la propriété cliStsCt.
     * 
     */
    public void setCliStsCt(short value) {
        this.cliStsCt = value;
    }

    /**
     * Obtient la valeur de la propriété cliMantypCt.
     * 
     */
    public short getCliMantypCt() {
        return cliMantypCt;
    }

    /**
     * Définit la valeur de la propriété cliMantypCt.
     * 
     */
    public void setCliMantypCt(short value) {
        this.cliMantypCt = value;
    }

    /**
     * Obtient la valeur de la propriété cliTypCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliTypCf() {
        return cliTypCf;
    }

    /**
     * Définit la valeur de la propriété cliTypCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliTypCf(String value) {
        this.cliTypCf = value;
    }

    /**
     * Obtient la valeur de la propriété cliCityLm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliCityLm() {
        return cliCityLm;
    }

    /**
     * Définit la valeur de la propriété cliCityLm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliCityLm(String value) {
        this.cliCityLm = value;
    }

    /**
     * Obtient la valeur de la propriété cliCtyCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliCtyCf() {
        return cliCtyCf;
    }

    /**
     * Définit la valeur de la propriété cliCtyCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliCtyCf(String value) {
        this.cliCtyCf = value;
    }

    /**
     * Obtient la valeur de la propriété cliAddLn5La.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliAddLn5La() {
        return cliAddLn5La;
    }

    /**
     * Définit la valeur de la propriété cliAddLn5La.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliAddLn5La(String value) {
        this.cliAddLn5La = value;
    }

    /**
     * Obtient la valeur de la propriété cliAddStaCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliAddStaCf() {
        return cliAddStaCf;
    }

    /**
     * Définit la valeur de la propriété cliAddStaCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliAddStaCf(String value) {
        this.cliAddStaCf = value;
    }

    /**
     * Obtient la valeur de la propriété cliAddLn3La.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliAddLn3La() {
        return cliAddLn3La;
    }

    /**
     * Définit la valeur de la propriété cliAddLn3La.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliAddLn3La(String value) {
        this.cliAddLn3La = value;
    }

    /**
     * Obtient la valeur de la propriété cliAddLn4La.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliAddLn4La() {
        return cliAddLn4La;
    }

    /**
     * Définit la valeur de la propriété cliAddLn4La.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliAddLn4La(String value) {
        this.cliAddLn4La = value;
    }

    /**
     * Obtient la valeur de la propriété cliActCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliActCf() {
        return cliActCf;
    }

    /**
     * Définit la valeur de la propriété cliActCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliActCf(String value) {
        this.cliActCf = value;
    }

    /**
     * Obtient la valeur de la propriété cliIlgst1La.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliIlgst1La() {
        return cliIlgst1La;
    }

    /**
     * Définit la valeur de la propriété cliIlgst1La.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliIlgst1La(String value) {
        this.cliIlgst1La = value;
    }

    /**
     * Obtient la valeur de la propriété cliIlgst2La.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliIlgst2La() {
        return cliIlgst2La;
    }

    /**
     * Définit la valeur de la propriété cliIlgst2La.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliIlgst2La(String value) {
        this.cliIlgst2La = value;
    }

    /**
     * Obtient la valeur de la propriété cliResSdCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliResSdCf() {
        return cliResSdCf;
    }

    /**
     * Définit la valeur de la propriété cliResSdCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliResSdCf(String value) {
        this.cliResSdCf = value;
    }

    /**
     * Obtient la valeur de la propriété cliResGrpcf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliResGrpcf() {
        return cliResGrpcf;
    }

    /**
     * Définit la valeur de la propriété cliResGrpcf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliResGrpcf(String value) {
        this.cliResGrpcf = value;
    }

    /**
     * Obtient la valeur de la propriété cliInactD.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getCliInactD() {
        return cliInactD;
    }

    /**
     * Définit la valeur de la propriété cliInactD.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setCliInactD(Object value) {
        this.cliInactD = value;
    }

    /**
     * Obtient la valeur de la propriété cliNaNatCt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliNaNatCt() {
        return cliNaNatCt;
    }

    /**
     * Définit la valeur de la propriété cliNaNatCt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliNaNatCt(String value) {
        this.cliNaNatCt = value;
    }

    /**
     * Obtient la valeur de la propriété cliRepCliNf.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCliRepCliNf() {
        return cliRepCliNf;
    }

    /**
     * Définit la valeur de la propriété cliRepCliNf.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCliRepCliNf(Integer value) {
        this.cliRepCliNf = value;
    }

    /**
     * Obtient la valeur de la propriété cliCedB.
     * 
     */
    public boolean isCliCedB() {
        return cliCedB;
    }

    /**
     * Définit la valeur de la propriété cliCedB.
     * 
     */
    public void setCliCedB(boolean value) {
        this.cliCedB = value;
    }

    /**
     * Obtient la valeur de la propriété cliPyrB.
     * 
     */
    public boolean isCliPyrB() {
        return cliPyrB;
    }

    /**
     * Définit la valeur de la propriété cliPyrB.
     * 
     */
    public void setCliPyrB(boolean value) {
        this.cliPyrB = value;
    }

    /**
     * Obtient la valeur de la propriété cliAccB.
     * 
     */
    public boolean isCliAccB() {
        return cliAccB;
    }

    /**
     * Définit la valeur de la propriété cliAccB.
     * 
     */
    public void setCliAccB(boolean value) {
        this.cliAccB = value;
    }

    /**
     * Obtient la valeur de la propriété cliRetB.
     * 
     */
    public boolean isCliRetB() {
        return cliRetB;
    }

    /**
     * Définit la valeur de la propriété cliRetB.
     * 
     */
    public void setCliRetB(boolean value) {
        this.cliRetB = value;
    }

    /**
     * Obtient la valeur de la propriété cliSupIndB.
     * 
     */
    public boolean isCliSupIndB() {
        return cliSupIndB;
    }

    /**
     * Définit la valeur de la propriété cliSupIndB.
     * 
     */
    public void setCliSupIndB(boolean value) {
        this.cliSupIndB = value;
    }

    /**
     * Obtient la valeur de la propriété creUsrCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreUsrCf() {
        return creUsrCf;
    }

    /**
     * Définit la valeur de la propriété creUsrCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreUsrCf(String value) {
        this.creUsrCf = value;
    }

    /**
     * Obtient la valeur de la propriété creD.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreD() {
        return creD;
    }

    /**
     * Définit la valeur de la propriété creD.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreD(XMLGregorianCalendar value) {
        this.creD = value;
    }

    /**
     * Obtient la valeur de la propriété lstUpdUsrCf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLstUpdUsrCf() {
        return lstUpdUsrCf;
    }

    /**
     * Définit la valeur de la propriété lstUpdUsrCf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLstUpdUsrCf(String value) {
        this.lstUpdUsrCf = value;
    }

    /**
     * Obtient la valeur de la propriété lstUpdD.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLstUpdD() {
        return lstUpdD;
    }

    /**
     * Définit la valeur de la propriété lstUpdD.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLstUpdD(XMLGregorianCalendar value) {
        this.lstUpdD = value;
    }

    /**
     * Obtient la valeur de la propriété hordNbrNt.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHordNbrNt() {
        return hordNbrNt;
    }

    /**
     * Définit la valeur de la propriété hordNbrNt.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHordNbrNt(Integer value) {
        this.hordNbrNt = value;
    }

    /**
     * Obtient la valeur de la propriété hindNbrNt.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHindNbrNt() {
        return hindNbrNt;
    }

    /**
     * Définit la valeur de la propriété hindNbrNt.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHindNbrNt(Integer value) {
        this.hindNbrNt = value;
    }

    /**
     * Obtient la valeur de la propriété srepcriSsdCf.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getSrepcriSsdCf() {
        return srepcriSsdCf;
    }

    /**
     * Définit la valeur de la propriété srepcriSsdCf.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setSrepcriSsdCf(Short value) {
        this.srepcriSsdCf = value;
    }

    /**
     * Obtient la valeur de la propriété srepcriSordNbrNt.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSrepcriSordNbrNt() {
        return srepcriSordNbrNt;
    }

    /**
     * Définit la valeur de la propriété srepcriSordNbrNt.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSrepcriSordNbrNt(Integer value) {
        this.srepcriSordNbrNt = value;
    }

    /**
     * Obtient la valeur de la propriété embargoB.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getEmbargoB() {
        return embargoB;
    }

    /**
     * Définit la valeur de la propriété embargoB.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setEmbargoB(Short value) {
        this.embargoB = value;
    }

    /**
     * Gets the value of the cleExtRe property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cleExtRe property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCleExtRe().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CleExtRe }
     * 
     * 
     */
    public List<CleExtRe> getCleExtRe() {
        if (cleExtRe == null) {
            cleExtRe = new ArrayList<CleExtRe>();
        }
        return this.cleExtRe;
    }

    /**
     * Gets the value of the cliRating property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cliRating property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCliRating().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FindClientByIdResponse.CliRating }
     * 
     * 
     */
    public List<FindClientByIdResponse.CliRating> getCliRating() {
        if (cliRating == null) {
            cliRating = new ArrayList<FindClientByIdResponse.CliRating>();
        }
        return this.cliRating;
    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * 
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="RatingName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RatingValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RatingEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "ratingName",
        "ratingValue",
        "ratingEffectiveDate"
    })
    public static class CliRating {

        @XmlElement(name = "RatingName")
        protected String ratingName;
        @XmlElement(name = "RatingValue")
        protected String ratingValue;
        @XmlElement(name = "RatingEffectiveDate")
        @XmlSchemaType(name = "anySimpleType")
        protected Object ratingEffectiveDate;

        /**
         * Obtient la valeur de la propriété ratingName.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRatingName() {
            return ratingName;
        }

        /**
         * Définit la valeur de la propriété ratingName.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRatingName(String value) {
            this.ratingName = value;
        }

        /**
         * Obtient la valeur de la propriété ratingValue.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRatingValue() {
            return ratingValue;
        }

        /**
         * Définit la valeur de la propriété ratingValue.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRatingValue(String value) {
            this.ratingValue = value;
        }

        /**
         * Obtient la valeur de la propriété ratingEffectiveDate.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getRatingEffectiveDate() {
            return ratingEffectiveDate;
        }

        /**
         * Définit la valeur de la propriété ratingEffectiveDate.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setRatingEffectiveDate(Object value) {
            this.ratingEffectiveDate = value;
        }

    }

}
