package com.scor.ref_client.schemas._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour FindClientByIdRequest complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="FindClientByIdRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cliNf" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindClientByIdRequest", propOrder = {
    "cliNf"
})
@XmlRootElement(name="FindClientByIdRequest")
public class FindClientByIdRequest {

    protected int cliNf;

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

}
