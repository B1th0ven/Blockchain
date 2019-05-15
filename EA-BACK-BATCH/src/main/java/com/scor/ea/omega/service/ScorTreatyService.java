package com.scor.ea.omega.service;

import java.text.ParseException;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import com.scor.ref_treaty.schemas._1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scor.ea.omega.ws_consumer.ScorConsumer;
import com.scor.ea.util.EaUtilities;

@Service
public class ScorTreatyService {

	private static final String WS_OMEGA_TREATY_ENDPOINT = "${ws.omega.treaty.endpoint}";

	@Value(WS_OMEGA_TREATY_ENDPOINT)
	private String TREATY_URI;

	@Autowired
	private ScorConsumer<FindTtyByLastModifiedDateRequest, FindTtyByLastModifiedDateResponse> scorConsumerFindTtyByLastModifiedDate;

	@Autowired
	private ScorConsumer<FindTtyCountryPerilByIdRequest, FindTtyCountryPerilByIdResponse> scorConsumerFindTtyCountryPerilById;

	@Autowired
	private ScorConsumer<FindTtyGuaranteeByIdRequest, FindTtyGuaranteeByIdResponse> scorConsumerFindTtyGuaranteeById;

	@Autowired
	private ScorConsumer<FindTtyHeaderByIdRequest, FindTtyHeaderByIdResponse> scorConsumerFindTtyHeaderById;

	@Autowired
	private ScorConsumer<FindTtyReinstatementByIdRequest, FindTtyReinstatementByIdResponse> scorConsumerFindTtyReinstatementById;

	@Autowired
	private ScorConsumer<FindTtySectionLabelListByIdRequest, FindTtySectionLabelListByIdResponse> scorConsumerFindTtySectionLabelListById;

	@Autowired
	private ScorConsumer<FindTtySectionListByIdRequest, FindTtySectionListByIdResponse> scorConsumerFindTtySectionListById;

	@Autowired
	private ScorConsumer<FindTtyTermCondByIdRequest, FindTtyTermCondByIdResponse> scorConsumerFindTtyTermCondById;

	@Autowired
	private ScorConsumer<FindTtyListByIdRequest,FindTtyListByIdResponse> scorConsumerFindTtyListById;

	public FindTtyByLastModifiedDateResponse findTtyByLastModifiedDate(String stringDate, Integer canDt, Integer ctrTypCt, Integer uwGrpCf, Integer uwNf)
			throws ParseException, DatatypeConfigurationException {
		FindTtyByLastModifiedDateRequest request = new FindTtyByLastModifiedDateRequest();
		request.setLstupdD(EaUtilities.xmlGregorianDateConverter(stringDate));
		request.setCanDt(canDt);
		request.setCtrtypCt(ctrTypCt);
		request.setUwgrpCf(uwGrpCf);
		request.setUwyNf(uwNf);
		FindTtyByLastModifiedDateResponse response = scorConsumerFindTtyByLastModifiedDate.consume(request, TREATY_URI);
		return response;
	}

	public FindTtyCountryPerilByIdResponse findTtyCountryPerilById(String treatyId, List<Integer> sectionIdList) {
		FindTtyCountryPerilByIdRequest request = new FindTtyCountryPerilByIdRequest();
		request.setCtrNf(treatyId);
		TtySectionIdList idList = new TtySectionIdList();
		idList.getSecNf().addAll(sectionIdList);
		request.setTtySectionIdList(idList);
		FindTtyCountryPerilByIdResponse response = scorConsumerFindTtyCountryPerilById.consume(request, TREATY_URI);
		return response;
	}

	public FindTtyGuaranteeByIdResponse findTtyGuaranteeById(String treatyId, List<Integer> sectionIdList) {
		FindTtyGuaranteeByIdRequest request = new FindTtyGuaranteeByIdRequest();
		request.setCtrNf(treatyId);
		TtySectionIdList idList = new TtySectionIdList();
		idList.getSecNf().addAll(sectionIdList);
		request.setTtySectionIdList(idList);
		FindTtyGuaranteeByIdResponse response = scorConsumerFindTtyGuaranteeById.consume(request, TREATY_URI);
		return response;
	}

	public FindTtyHeaderByIdResponse findTtyHeaderById(String treatyId) {
		FindTtyHeaderByIdRequest request = new FindTtyHeaderByIdRequest();
		request.setCtrNf(treatyId);
		FindTtyHeaderByIdResponse response = scorConsumerFindTtyHeaderById.consume(request, TREATY_URI);
		return response;
	}

	public FindTtyReinstatementByIdResponse findTtyReinstatementById(String treatyId, List<Integer> sectionIdList) {
		FindTtyReinstatementByIdRequest request = new FindTtyReinstatementByIdRequest();
		request.setCtrNf(treatyId);
		TtySectionIdList idList = new TtySectionIdList();
		idList.getSecNf().addAll(sectionIdList);
		request.setTtySectionIdList(idList);
		FindTtyReinstatementByIdResponse response = scorConsumerFindTtyReinstatementById.consume(request, TREATY_URI);
		return response;
	}

	public FindTtySectionLabelListByIdResponse findTtySectionLabelListById(String treatyId) {
		FindTtySectionLabelListByIdRequest request = new FindTtySectionLabelListByIdRequest();
		request.setCtrNf(treatyId);
		FindTtySectionLabelListByIdResponse response = scorConsumerFindTtySectionLabelListById.consume(request,
				TREATY_URI);
		return response;
	}

	public FindTtySectionListByIdResponse findTtySectionListById(String treatyId, String lagCf, Integer uwNt,
			Integer endNt, int secNf, Integer uwyNf) {
		FindTtySectionListByIdRequest request = new FindTtySectionListByIdRequest();
		request.setCtrNf(treatyId);
		request.setLagCf(lagCf);
		request.setUwNt(uwNt);
		request.setEndNt(endNt);
		request.setSecNf(secNf);
		request.setUwyNf(uwyNf);
		FindTtySectionListByIdResponse response = scorConsumerFindTtySectionListById.consume(request, TREATY_URI);
		return response;
	}

	public FindTtyTermCondByIdResponse findTtyTermCondById(String treatyId, List<Integer> sectionIdList) {
		FindTtyTermCondByIdRequest request = new FindTtyTermCondByIdRequest();
		request.setCtrNf(treatyId);
		TtySectionIdList idList = new TtySectionIdList();
		idList.getSecNf().addAll(sectionIdList);
		request.setTtySectionIdList(idList);
		FindTtyTermCondByIdResponse response = scorConsumerFindTtyTermCondById.consume(request, TREATY_URI);
		return response;
	}

	public FindTtyListByIdResponse findTtyListById(String clishonamLd, Integer lifeCf)
			throws ParseException, DatatypeConfigurationException {
		FindTtyListByIdRequest request = new FindTtyListByIdRequest();
		request.setClishonamLd(clishonamLd);
		request.setLifeCf(lifeCf);
		FindTtyListByIdResponse response = scorConsumerFindTtyListById.consume(request, TREATY_URI);
		return response;
	}

}
