package com.scor.dataProcessing.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import com.scor.dataProcessing.dataChecker.DCFactory;
import com.scor.dataProcessing.dataChecker.functionalChecker.ExpTableFunctionalChecker;
import com.scor.dataProcessing.dataChecker.functionalChecker.StudyFunctionalChecker;
import com.scor.dataProcessing.dataChecker.integrityChecker.IntegrityCheckerRegister;
import com.scor.dataProcessing.dataChecker.schemaChecker.SchemaCheckerRegister;
import com.scor.dataProcessing.models.ControlResults;
import com.scor.dataProcessing.models.NotExecutedDto;
import com.scor.dataProcessing.sparkConnection.*;
import com.scor.dataProcessing.sparkUtils.SparkService;
import com.scor.persistance.entities.StudyEntity;
import com.scor.persistance.services.RefDataService;
import com.scor.persistance.services.StudyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ControlsRestService implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5617628776582212512L;



	@Autowired
	private RefDataService refDataService;

	@Autowired
	StudyService studyService;

	@Autowired
	SparkService sparkService;


	@RequestMapping(value = "/CompColsCheck", method = RequestMethod.POST)
	public List<List<String>> check(HttpEntity<String> req)
			throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		String path = (String) req_map.get("path");
		String type = (String) req_map.get("type");
		String userId = (String) req_map.get("userId");
		String studyId = (String) req_map.get("studyId");
		String checker = "split".equalsIgnoreCase(type) || "combine".equalsIgnoreCase(type)
				? SchemaCheckerRegister.POLICY
				: SchemaCheckerRegister.PRODUCT;
		return DCFactory.getSchemaChecker().run(path, type, checker,userId,studyId);
	}


	@RequestMapping(value = "/TechControls", method = RequestMethod.POST)
	public List<ControlResults> techCtrl(HttpEntity<String> req)
			throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		String path = (String) req_map.get("path_policy");
		String path_product = (String) req_map.get("path_product");
		String type = (String) req_map.get("type");
		String userId = (String) req_map.get("userId");
		String studyId = (String) req_map.get("studyId");
		Map<String, List<String>> refData = refDataService.getAll();
		ControlResults expoRes = DCFactory.getIntegrityChecker().run(refData, path, type,
				IntegrityCheckerRegister.POLICY, userId, studyId);
		ControlResults prodRes = DCFactory.getIntegrityChecker().run(refData, path_product, null,
				IntegrityCheckerRegister.PRODUCT, userId, studyId);
		return Arrays.asList(expoRes, prodRes);
	}

	@RequestMapping(value = "/FuncControls", method = RequestMethod.POST)
	public ControlResults funcCtrl(HttpEntity<String> req)
			throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		int stdid = (int) req_map.get("stdid");
		StudyEntity study = studyService.getStudy(stdid);

		String country = "";
		if (study.getRefCountryById().getRcCode() != null)
			country = study.getRefCountryById().getRcCode().trim().toLowerCase();

		String distributionBrand = "";
		if (study.getStDistributionBrand() != null)
			distributionBrand = study.getStDistributionBrand().trim().toLowerCase();

		String Client = "";
		if (study.getRefCedentNameByStRcnId() == null) {
			if (study.getStMultiCedent())
				Client = "multiple";
			if (study.getStOtherCedent())
				Client = "other";
		} else {
			Client = study.getRefCedentNameByStRcnId().getRcnShortName().toLowerCase().trim();
		}

		String ClientGroup = "";
		if (study.getRefParentGroupByStRpgId() == null) {
			if (study.getStMultiParentGroup())
				ClientGroup = "multiple";
			else if (study.getStOtherParentGroup())
				ClientGroup = "other";
			else
				ClientGroup = "independent";
		} else {
			ClientGroup = study.getRefParentGroupByStRpgId().getRpgName().toLowerCase().trim();
		}

		String Treaty = "";
		if (study.getRefTreatyByStRtId() == null) {
			if (study.getStMultiTreaty())
				Treaty = "multiple";
			if (study.getStOtheriTreaty())
				Treaty = "other";
		} else {
			Treaty = study.getRefTreatyByStRtId().getRtName().toLowerCase().trim();
		}

		String path = (String) req_map.get("path_policy");
		String path_product = (String) req_map.get("path_product");
		String type = (String) req_map.get("type");
		String op_start = (String) req_map.get("op_start");
		String op_end = (String) req_map.get("op_end");
		String userId = (String) req_map.get("userId");
		String studyId = (String) req_map.get("studyId");

		return DCFactory.getFunctionalChecker().runStudyFileChecker(path, path_product, type, op_start, op_end, Client,
				ClientGroup, Treaty, distributionBrand, country, userId, studyId);
	}

	@RequestMapping(value = "/NotExecutedControls", method = RequestMethod.POST)
	public List<NotExecutedDto> notExecutedTachControls(HttpEntity<String> req)
			throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		String path = (String) req_map.get("path_policy");
		String path_product = (String) req_map.get("path_product");
		String type = (String) req_map.get("type");
		String op_start = (String) req_map.get("op_start");
		String op_end = (String) req_map.get("op_end");
		String userId = (String) req_map.get("userId");
		String studyId = (String) req_map.get("studyId");

		return DCFactory.getNotExecutedRuleChecker().run(path, path_product, type, op_start, op_end, userId, studyId);
	}

	@RequestMapping(value = "/ExpTableControls/Comp", method = RequestMethod.POST)
	public List<List<String>> expTableControlsComp(HttpEntity<String> req) throws Exception {
		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		String path = (String) req_map.get("path");
		String type = (String) req_map.get("type");
		String userId = (String) req_map.get("userId");
		String studyId = (String) req_map.get("studyId");
		return DCFactory.getSchemaChecker().run(path, type, SchemaCheckerRegister.EXPTABLE,userId,studyId);
	}

       
    


	@RequestMapping(value = "/ExpTableControls/Tech", method = RequestMethod.POST)
	public ControlResults expTableControlsTech(HttpEntity<String> req) throws Exception {
		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		String path = (String) req_map.get("path");
		String type = (String) req_map.get("type");
		String userId = (String) req_map.get("userId");
		String studyId = (String) req_map.get("studyId");

		Map<String, List<String>> refData = refDataService.getAll();
		return DCFactory.getIntegrityChecker().run(refData, path, type, IntegrityCheckerRegister.EXPTABLE, userId, studyId);
	}

	@RequestMapping(value = "/ExpTableControls/Func", method = RequestMethod.POST)
	public ControlResults expTableControlsFunc(HttpEntity<String> req) throws Exception {
		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		String path = (String) req_map.get("path");
		String type = (String) req_map.get("type");
		HashMap<String, Integer> maxValues = (HashMap<String, Integer>) req_map.get("maxValues");
		HashMap<String, Integer> minValues = (HashMap<String, Integer>) req_map.get("minValues");
		return DCFactory.getFunctionalChecker().runExpTableChecker(path, type, maxValues, minValues);
	}

	/*
	 * @RequestMapping(value = "/test", method = RequestMethod.POST) public void
	 * test(HttpEntity<String> req) throws Exception { HashMap<String, Object>
	 * req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class); String
	 * path = (String) req_map.get("path"); String type = (String)
	 * req_map.get("type"); ExpTableFuncControls efc = new ExpTableFuncControls();
	 * efc.checkCombination(path, type); }
	 */

	@RequestMapping(value = "/ExpTableControls/Calc", method = RequestMethod.POST)
	public HashMap<String, Long> expTableCalc(HttpEntity<String> req) throws Exception {
		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		String path = (String) req_map.get("path");
		String type = (String) req_map.get("type");
		return DCFactory.getFunctionalChecker().runCalculator(path, type) ; 
	}


	@RequestMapping(value ="/CancelJobControls",  method = RequestMethod.POST)
	public String cancelJob(HttpEntity<String> req)throws Exception {
		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		String userId = (String) req_map.get("userId");
		String studyId = (String) req_map.get("studyId");
		sparkService.cancel(userId,studyId);
		return "done";
	}

//	@RequestMapping(value = "/test", method = RequestMethod.POST)
//	public List<ControlResult> result(HttpEntity<String> req) throws Exception {
//		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
//		String path = (String) req_map.get("path");
//		String id  = (String) req_map.get("stid");
//		StudyEntity study = studyService.getStudy(Integer.parseInt(id));
//		String Client = study.getRefCedentNameByStRcnId().getRcnShortName().toLowerCase().trim();
//		String ClientGroup = study.getRefParentGroupByStRpgId().getRpgName().toLowerCase().trim();
//		String Treaty = study.getRefTreatyByStRtId().getRtName().toLowerCase().trim();
//		 return funcControlsService.CheckClientAndClientGroupAndTreaties(path,Client,ClientGroup,Treaty,"","");
//	}
}
