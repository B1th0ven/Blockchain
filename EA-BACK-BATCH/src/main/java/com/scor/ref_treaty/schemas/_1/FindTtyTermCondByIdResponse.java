package com.scor.ref_treaty.schemas._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour findTtyTermCondByIdResponse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="findTtyTermCondByIdResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sectionID" type="{http://scor.com/ref-treaty/schemas/1.1}SCORTtyTermCond" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findTtyTermCondByIdResponse", propOrder = {
    "sectionID"
})
@XmlRootElement(name = "FindTtyTermCondByIdResponse")
public class FindTtyTermCondByIdResponse {

    protected List<SCORTtyTermCond> sectionID;

    /**
     * Gets the value of the sectionID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sectionID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSectionID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SCORTtyTermCond }
     * 
     * 
     */
    public List<SCORTtyTermCond> getSectionID() {
        if (sectionID == null) {
            sectionID = new ArrayList<SCORTtyTermCond>();
        }
        return this.sectionID;
    }

}
