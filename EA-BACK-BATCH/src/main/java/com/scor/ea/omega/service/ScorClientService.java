package com.scor.ea.omega.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scor.ea.omega.ws_consumer.ScorConsumer;
import com.scor.ea.util.EaUtilities;
import com.scor.ref_client.schemas._1.FindClientByIdRequest;
import com.scor.ref_client.schemas._1.FindClientByIdResponse;
import com.scor.ref_client.schemas._1.FindClientByLastModifiedDateRequest;
import com.scor.ref_client.schemas._1.FindClientByLastModifiedDateResponse;

@Service
public class ScorClientService {

	private static final String WS_OMEGA_CLIENT_ENDPOINT = "${ws.omega.client.endpoint}";

	@Autowired
	ScorConsumer<FindClientByIdRequest, FindClientByIdResponse> scorConsumerFindClientById;

	@Autowired
	ScorConsumer<FindClientByLastModifiedDateRequest, FindClientByLastModifiedDateResponse> scorConsumerFindClientByLastModifiedDate;

	@Value(WS_OMEGA_CLIENT_ENDPOINT)
	private String CLIENT_URI;

	public List<FindClientByIdResponse> scorClientsInfo(String stringDate)
			throws DatatypeConfigurationException, ParseException {
		XMLGregorianCalendar date = EaUtilities.xmlGregorianDateConverter(stringDate);
		FindClientByLastModifiedDateResponse response = findClientByLastModificationDate(date);
		System.out.println("FindClientsId since : " + stringDate + " // " + response.getIdList().size());
		List<FindClientByIdResponse> res = new ArrayList<>();
		for (int id : response.getIdList()) {
			FindClientByIdResponse clientInfo = findClientById(id);
			System.out.println("Find client : " + clientInfo.getCliNf());
			res.add(clientInfo);
		}
		return res;
	}

	private FindClientByLastModifiedDateResponse findClientByLastModificationDate(XMLGregorianCalendar date) {
		FindClientByLastModifiedDateRequest request = new FindClientByLastModifiedDateRequest();
		request.setLastModifiedDate(date);
		FindClientByLastModifiedDateResponse response = scorConsumerFindClientByLastModifiedDate.consume(request,
				CLIENT_URI);
		return response;
	}

	public FindClientByIdResponse findClientById(int id) {
		FindClientByIdRequest requestClient = new FindClientByIdRequest();
		requestClient.setCliNf(id);
		FindClientByIdResponse clientInfo = scorConsumerFindClientById.consume(requestClient, CLIENT_URI);
		return clientInfo;
	}

}
