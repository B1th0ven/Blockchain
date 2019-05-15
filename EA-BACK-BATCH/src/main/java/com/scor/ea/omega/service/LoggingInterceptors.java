package com.scor.ea.omega.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Service
public class LoggingInterceptors implements ClientInterceptor {
	
	private static final Logger LOGGER = Logger.getLogger(LoggingInterceptors.class);

	@Override
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
		TransportContext context = TransportContextHolder.getTransportContext();
		HttpUrlConnection connection = (HttpUrlConnection) context.getConnection();
		try {
			connection.addRequestHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			messageContext.getRequest().writeTo(os);
		} catch (IOException e) {
			throw new WebServiceIOException(e.getMessage(), e);
		}
		String request = new String(os.toByteArray());
		LOGGER.info("Request Envelope: \n" + format(request));
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			messageContext.getResponse().writeTo(os);
		} catch (IOException e) {
			throw new WebServiceIOException(e.getMessage(), e);
		}

		String response = new String(os.toByteArray());
		LOGGER.info("Response Envelope: \n" + format(response));
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {
		// TODO Auto-generated method stub

	}
	
	private static String format(String a) {
		Document xmlDoc = null;
        String formattedXML = "";
        try {
            xmlDoc = toXmlDocument(a);
            formattedXML = prettyPrint(xmlDoc);
        } catch (ParserConfigurationException | SAXException | IOException
                | TransformerException e) {
            e.printStackTrace();
        }
        return formattedXML;
	}
	
	private static String prettyPrint(Document document)
	        throws TransformerException {
	    TransformerFactory transformerFactory = TransformerFactory
	            .newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty(
	            "{http://xml.apache.org/xslt}indent-amount", "2");
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	    DOMSource source = new DOMSource(document);
	    StringWriter strWriter = new StringWriter();
	    StreamResult result = new StreamResult(strWriter);
	 
	    transformer.transform(source, result);
	 
	    return strWriter.getBuffer().toString();
	 
	}
	
	private static Document toXmlDocument(String str)
            throws ParserConfigurationException, SAXException, IOException {
 
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(new InputSource(new StringReader(
                str)));
 
        return document;
    }
}
