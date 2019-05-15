package com.scor.persistance.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scor.persistance.entities.StudyEntity;
import com.scor.persistance.services.StudyService;
import com.scor.persistance.specifications.StudyEntityPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class StudyController {

	@Value("${custom.upload.path}")
	private String UPLOADED_FOLDER_BASE;

	@Autowired
	private StudyService studyService;

	@RequestMapping("/studies")
	public StudyEntityPage getStudiesBySearch(@RequestParam("limit") int limit, @RequestParam("page") int page,
			@RequestParam("code") Optional<String> code, @RequestParam("country") Optional<String> country,
			@RequestParam("group") Optional<String> group, @RequestParam("treaty") Optional<String> treaty,
			@RequestParam("lob") Optional<String> lob, @RequestParam("client") Optional<String> client,
			@RequestParam("brand") Optional<String> brand, @RequestParam("status") Optional<String> status,
			@RequestParam("statusdate") Optional<String> statusdate,
			@RequestParam("createdBy") Optional<String> createdBy, @RequestParam("sort") Optional<String> sort,
			@RequestParam("desc") Optional<Boolean> desc) {
		return studyService.searchByQuery(page, limit, code, country, group, client, brand, treaty, lob, status,
				statusdate,createdBy, sort, desc);
	}

	@RequestMapping("/studies/")
	public Page<StudyEntity> getStudies(@RequestParam("limit") int limit, @RequestParam("page") int page) {
		Pageable p = new PageRequest(page, limit);
		return studyService.getPage(p);
	}

	@RequestMapping("/studies/{id}")
	public StudyEntity getStudy(@PathVariable("id") String id) {
		return studyService.getStudy(Integer.parseInt(id));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/studies")
	public StudyEntity postStudy(@RequestBody StudyEntity st) {
		return studyService.postStudy(st);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/studies/{id}")
	public StudyEntity deleteStudy(@PathVariable("id") String st_id, @RequestParam("rminput") Boolean rminput)
			throws Exception {
		return studyService.deleteStudy(Integer.parseInt(st_id), "deleted", rminput);
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/studies/changeStatus/{id}")
	public StudyEntity changeStudyStatus(@PathVariable("id") String st_id, HttpEntity<String> req) throws IOException {
		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		String status = (String) req_map.get("status");
		int ruId = (Integer) req_map.get("ruId");
		return studyService.changeStudyStatus(Integer.parseInt(st_id), status,ruId);
	}

	@RequestMapping(value = "study/{id}/validate", method = RequestMethod.POST)
	public StudyEntity validateStudy(@PathVariable("id") int id, HttpEntity<String> req) throws Exception {
		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		int masterRunQx = req_map.get("masterRunQx") != null ? (int) req_map.get("masterRunQx") : 0;
		int masterRunIx = req_map.get("masterRunIx") != null ? (int) req_map.get("masterRunIx") : 0;
		int masterRunWx = req_map.get("masterRunWx") != null ? (int) req_map.get("masterRunWx") : 0;
		int masterRunIxQx = req_map.get("masterRunIxQx") != null ? (int) req_map.get("masterRunIxQx") : 0;

		Integer ruId = req_map.get("ruId") != null ? (Integer) req_map.get("ruId") : null;

		List<Integer> runsRetained = (req_map.get("runRetained") instanceof List<?>)
				? (List<Integer>) req_map.get("runRetained")
				: new ArrayList<>();
		boolean deleteUnusedDatasets = req_map.get("deleteUnusedDatasets") != null
				? (boolean) req_map.get("deleteUnusedDatasets")
				: false;
		StudyEntity response = studyService.validateStudy(id, masterRunQx, masterRunIx, masterRunWx, masterRunIxQx, runsRetained,
				deleteUnusedDatasets,ruId);

		return response;
	}
	
	@RequestMapping(value = "/study/file", method = RequestMethod.POST)
	public void delete(HttpEntity<String> req) throws Exception {
		HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		int id = req_map.get("id") != null ? (int) req_map.get("id") : 0;
		String path = (String) req_map.get("path");
		studyService.deleteAttachedFile(path, id);
	}

}
