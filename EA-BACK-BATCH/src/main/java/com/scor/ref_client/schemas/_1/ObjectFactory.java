package com.scor.ref_client.schemas._1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.scor.ref_client.schemas._1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FindClientByIdRequest_QNAME = new QName("http://scor.com/ref-client/schemas/1.1", "FindClientByIdRequest");
    private final static QName _FindClientByIdResponse_QNAME = new QName("http://scor.com/ref-client/schemas/1.1", "FindClientByIdResponse");
    private final static QName _FindClientByLastModifiedDateRequest_QNAME = new QName("http://scor.com/ref-client/schemas/1.1", "FindClientByLastModifiedDateRequest");
    private final static QName _FindClientByLastModifiedDateResponse_QNAME = new QName("http://scor.com/ref-client/schemas/1.1", "FindClientByLastModifiedDateResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.scor.ref_client.schemas._1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FindClientByIdResponse }
     * 
     */
    public FindClientByIdResponse createFindClientByIdResponse() {
        return new FindClientByIdResponse();
    }

    /**
     * Create an instance of {@link FindClientByIdRequest }
     * 
     */
    public FindClientByIdRequest createFindClientByIdRequest() {
        return new FindClientByIdRequest();
    }

    /**
     * Create an instance of {@link FindClientByLastModifiedDateRequest }
     * 
     */
    public FindClientByLastModifiedDateRequest createFindClientByLastModifiedDateRequest() {
        return new FindClientByLastModifiedDateRequest();
    }

    /**
     * Create an instance of {@link FindClientByLastModifiedDateResponse }
     * 
     */
    public FindClientByLastModifiedDateResponse createFindClientByLastModifiedDateResponse() {
        return new FindClientByLastModifiedDateResponse();
    }

    /**
     * Create an instance of {@link CleExtRe }
     * 
     */
    public CleExtRe createCleExtRe() {
        return new CleExtRe();
    }

    /**
     * Create an instance of {@link FindClientByIdResponse.CliRating }
     * 
     */
    public FindClientByIdResponse.CliRating createFindClientByIdResponseCliRating() {
        return new FindClientByIdResponse.CliRating();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindClientByIdRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://scor.com/ref-client/schemas/1.1", name = "FindClientByIdRequest")
    public JAXBElement<FindClientByIdRequest> createFindClientByIdRequest(FindClientByIdRequest value) {
        return new JAXBElement<FindClientByIdRequest>(_FindClientByIdRequest_QNAME, FindClientByIdRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindClientByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://scor.com/ref-client/schemas/1.1", name = "FindClientByIdResponse")
    public JAXBElement<FindClientByIdResponse> createFindClientByIdResponse(FindClientByIdResponse value) {
        return new JAXBElement<FindClientByIdResponse>(_FindClientByIdResponse_QNAME, FindClientByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindClientByLastModifiedDateRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://scor.com/ref-client/schemas/1.1", name = "FindClientByLastModifiedDateRequest")
    public JAXBElement<FindClientByLastModifiedDateRequest> createFindClientByLastModifiedDateRequest(FindClientByLastModifiedDateRequest value) {
        return new JAXBElement<FindClientByLastModifiedDateRequest>(_FindClientByLastModifiedDateRequest_QNAME, FindClientByLastModifiedDateRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindClientByLastModifiedDateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://scor.com/ref-client/schemas/1.1", name = "FindClientByLastModifiedDateResponse")
    public JAXBElement<FindClientByLastModifiedDateResponse> createFindClientByLastModifiedDateResponse(FindClientByLastModifiedDateResponse value) {
        return new JAXBElement<FindClientByLastModifiedDateResponse>(_FindClientByLastModifiedDateResponse_QNAME, FindClientByLastModifiedDateResponse.class, null, value);
    }

}
