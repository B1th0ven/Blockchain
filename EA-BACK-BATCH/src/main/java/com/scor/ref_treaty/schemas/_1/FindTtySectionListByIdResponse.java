package com.scor.ref_treaty.schemas._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour findTtySectionListByIdResponse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="findTtySectionListByIdResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ttySection" type="{http://scor.com/ref-treaty/schemas/1.1}SCORTtySection" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findTtySectionListByIdResponse", propOrder = {
    "ttySection"
})
@XmlRootElement(name = "FindTtySectionListByIdResponse")
public class FindTtySectionListByIdResponse {

    protected List<SCORTtySection> ttySection;

    /**
     * Gets the value of the ttySection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ttySection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTtySection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SCORTtySection }
     * 
     * 
     */
    public List<SCORTtySection> getTtySection() {
        if (ttySection == null) {
            ttySection = new ArrayList<SCORTtySection>();
        }
        return this.ttySection;
    }

}
