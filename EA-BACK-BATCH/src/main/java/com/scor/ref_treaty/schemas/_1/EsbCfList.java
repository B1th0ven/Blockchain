
package com.scor.ref_treaty.schemas._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * <p>Classe Java pour esbCfList complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="esbCfList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="esbCf" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "esbCfList", propOrder = {
    "esbCf"
})
@XmlRootElement(name = "EsbCfList")
public class EsbCfList {

    @XmlElement(type = Integer.class)
    protected List<Integer> esbCf;

    /**
     * Gets the value of the esbCf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the esbCf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEsbCf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getEsbCf() {
        if (esbCf == null) {
            esbCf = new ArrayList<Integer>();
        }
        return this.esbCf;
    }

}
