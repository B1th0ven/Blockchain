package com.scor.ea.omega.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.xml.datatype.DatatypeConfigurationException;

import com.scor.ref_treaty.schemas._1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scor.ea.omega.service.OmegaService;
import com.scor.ea.omega.service.ScorClientService;
import com.scor.ea.omega.service.ScorTreatyService;
import com.scor.ref_client.schemas._1.FindClientByIdResponse;

@RestController
public class ScorClientController {

	@Autowired
	private ScorClientService scorClientService;

	@Autowired
	private ScorTreatyService ScorTreatyService;
	
	@Autowired
	private OmegaService omegaService;

	@GetMapping("scorClient/clients")
	public List<FindClientByIdResponse> findClientsInfo(@RequestParam("lastModDate") String date)
			throws DatatypeConfigurationException, ParseException {
		return scorClientService.scorClientsInfo(date);
	}
	
	@GetMapping("scorClient/client/{id}")
	public FindClientByIdResponse findClientInfo(@PathVariable("id") Integer id)
			throws DatatypeConfigurationException, ParseException {
		return scorClientService.findClientById(id);
	}

	@GetMapping("scorClient/tty")
	public FindTtyByLastModifiedDateResponse findTtyInfo(@RequestParam("lastModDate") String date,
			@RequestParam("canDt") Optional<Integer> canDt, @RequestParam("ctrTypCt") Optional<Integer> ctrTypCt,
			@RequestParam("uwGrpCf") Optional<Integer> uwGrpCf, @RequestParam("uwNf") Optional<Integer> uwNf)
			throws DatatypeConfigurationException, ParseException {
		Integer canDtValue = null;
		if(canDt.isPresent()) {
			canDtValue = canDt.get();
		}
		Integer ctrTypCtValue = null;
		if(ctrTypCt.isPresent()) {
			ctrTypCtValue = ctrTypCt.get();
		}
		Integer uwGrpCfValue = null;
		if(uwGrpCf.isPresent()) {
			uwGrpCfValue = uwGrpCf.get();
		}
		Integer uwNfValue = null;
		if(uwNf.isPresent()) {
			uwNfValue = uwNf.get();
		}
		return ScorTreatyService.findTtyByLastModifiedDate(date, canDtValue, ctrTypCtValue, uwGrpCfValue, uwNfValue);
	}

	@GetMapping("findTtyCountryPerilById")
	public FindTtyCountryPerilByIdResponse findTtyCountryPerilById(@RequestParam("treatyid") String treatyId,
			@RequestParam("sectionIdList") List<Integer> sectionIdList) {
		return ScorTreatyService.findTtyCountryPerilById(treatyId, sectionIdList);
	}

	@GetMapping("findTtyHeaderById")
	public FindTtyHeaderByIdResponse findTtyHeaderById(@RequestParam("treatyid") String treatyId) {
		return ScorTreatyService.findTtyHeaderById(treatyId);
	}

	@GetMapping("findTtyReinstatementById")
	public FindTtyReinstatementByIdResponse findTtyReinstatementById(@RequestParam("treatyid") String treatyId,
			@RequestParam("sectionIdList") List<Integer> sectionIdList) {
		return ScorTreatyService.findTtyReinstatementById(treatyId, sectionIdList);
	}

	@GetMapping("findTtySectionLabelListById")
	public FindTtySectionLabelListByIdResponse findTtySectionLabelListById(@RequestParam("treatyid") String treatyId) {
		return ScorTreatyService.findTtySectionLabelListById(treatyId);
	}

	@GetMapping("findTtySectionListById")
	public FindTtySectionListByIdResponse findTtySectionListById(@RequestParam("treatyid") String treatyId,
			@RequestParam("lagCf") Optional<String> lagCf, @RequestParam("uwNt") Optional<Integer> uwNt,
			@RequestParam("endNt") Optional<Integer> endNt, @RequestParam("secNf") int secNf,
			@RequestParam("uwyNf") Optional<Integer> uwyNf) {
		String lagCfValue = null;
		if(lagCf.isPresent()) {
			lagCfValue = lagCf.get();
		}
		Integer uwNtValue = null;
		if(uwNt.isPresent()) {
			uwNtValue = uwNt.get();
		}
		Integer endNtValue = null;
		if(endNt.isPresent()) {
			endNtValue = endNt.get();
		}
		Integer uwyNfValue = null;
		if(uwyNf.isPresent()) {
			uwyNfValue = uwyNf.get();
		}
		return ScorTreatyService.findTtySectionListById(treatyId, lagCfValue, uwNtValue, endNtValue, secNf, uwyNfValue);
	}

	@GetMapping("findTtyTermCondById")
	public FindTtyTermCondByIdResponse findTtyTermCondById(@RequestParam("treatyid") String treatyId,
			@RequestParam("sectionIdList") List<Integer> sectionIdList) {
		return ScorTreatyService.findTtyTermCondById(treatyId, sectionIdList);
	}

	@GetMapping("findTtyGuaranteeById")
	public FindTtyGuaranteeByIdResponse findTtyGuaranteeById(@RequestParam("treatyid") String treatyId,
			@RequestParam("sectionIdList") List<Integer> sectionIdList) {
		return ScorTreatyService.findTtyGuaranteeById(treatyId, sectionIdList);
	}

	@GetMapping("findTtyListById")
	public FindTtyListByIdResponse findTtyLife(@RequestParam("clishonamLd") String clishonamLd,
											   @RequestParam("lifeCf") Optional<Integer> lifeCf) throws DatatypeConfigurationException, ParseException {
		Integer lifeCfValue = null;
		if(lifeCf.isPresent()) {
			lifeCfValue = lifeCf.get();
		}

		return ScorTreatyService.findTtyListById(clishonamLd,lifeCfValue);
	}
	
	@GetMapping("scorClient/inject")
	public void injectClients(@RequestParam("lastModDate") String date) {
		omegaService.injectClient(date);
	}
	
	@GetMapping("scorTreaty/inject")
	public void injectTreaties(@RequestParam("lastModDate") String date) {
		omegaService.injectTreaties(date);
	}
	
}
