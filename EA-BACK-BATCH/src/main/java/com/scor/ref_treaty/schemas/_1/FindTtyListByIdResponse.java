
package com.scor.ref_treaty.schemas._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * <p>Classe Java pour findTtyListByIdResponse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="findTtyListByIdResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tty" type="{http://scor.com/ref-treaty/schemas/2.0}SCORTty" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findTtyListByIdResponse", propOrder = {
    "tty"
})
@XmlRootElement(name = "FindTtyListByIdResponse")
//@XmlSeeAlso({SCORTty.class})
public class FindTtyListByIdResponse {

    @XmlElement(name = "SCORTty")
    protected List<SCORTty> tty;

    /**
     * Gets the value of the tty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SCORTty }
     * 
     * 
     */
    public List<SCORTty> getTty() {
        if (tty == null) {
            tty = new ArrayList<SCORTty>();
        }
        return this.tty;
    }

}
