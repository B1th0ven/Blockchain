package com.scor.ea.omega.ws_consumer;

import javax.xml.bind.JAXBElement;

import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

@Service
public class ScorConsumer<Request, Response> extends WebServiceGatewaySupport {

	@SuppressWarnings("unchecked")
	public Response consume(Request request, String uri) {
		JAXBElement<Response> response = (JAXBElement<Response>) getWebServiceTemplate().marshalSendAndReceive(
				uri, request,
				new SoapActionCallback(
						uri));
		Response res = response.getValue();
		return res;
	}
	
}
