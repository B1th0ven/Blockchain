package com.scor.persistance.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scor.dataProcessing.common.DataPivot;
import com.scor.dataProcessing.models.PivotCol;
import com.scor.persistance.entities.DataSetEntity;
import com.scor.persistance.services.DatasetService;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class DatasetController {

	@Autowired
	private DatasetService service;

	@RequestMapping("/datasets")
	public List<DataSetEntity> getDatasets() {
		return service.getAll();
	}

	@RequestMapping("/datasets/study/{id}")
	public List<DataSetEntity> getDatasetsByStudyId(@PathVariable("id") int id) {
		// System.out.println("--------------------------------------");
		return service.getDatasetsByStudyId(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/datasets")
	public DataSetEntity saveDataset(@RequestBody() DataSetEntity ds) {
		return service.postDataset(ds);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/datasets/report")
	public DataSetEntity saveDatasetReport(@RequestBody() DataSetEntity ds) {
		return service.postDatasetReport(ds);
	}

	@RequestMapping(value = "/datasets/run/study/{id}")
	public ObjectNode isStudyDatasetsValid(@PathVariable("id") int id) throws JSONException {
		int count = service.validStudyDatasetsCount(id);
		boolean valid = count > 0;

		String res = "{'valid':'" + valid + "'}";
		ObjectNode node = JsonNodeFactory.instance.objectNode(); // initializing
		node.put("valid", valid); // building
		node.put("count", count); // building
		return node;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/datasets/delete/{id}")
	public HashMap<String, String> deleteDataSet(@PathVariable("id") int id) throws Exception {
		service.deleteDataset(id);
		return new HashMap<String, String>() {
			{
				put("deleteStatus", "Done");
			}
		};
	}

	@RequestMapping("/datasets/pivots/{type}")
	public List<String> getDatasetsPivots(@PathVariable("type") String type) {
		List<PivotCol> pivot = new ArrayList<>();
		if (type.equals("policy")) {
			pivot = DataPivot.getPivotCols();
		} else if (type.equals("product")) {
			pivot = DataPivot.getPivotColsProduct();
		}
		List<String> result = new ArrayList<>();
		pivot.forEach(p -> {
			result.add(p.getName());
		});
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/datasets/file")
	public List<DataSetEntity> findDatasetByFile(@RequestBody() String path) {
		return service.findDatasetByFileId(path);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/datasets/{id}/decrement")
	public List<String> validateDecrements(@PathVariable("id") int id,HttpEntity<String> req) throws JsonParseException, JsonMappingException, IOException {
        HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody() , HashMap.class) ;
		List<String> decrements = new ArrayList<>();
        if(req_map.get("decrements") instanceof List<?>) {
        	decrements = (List<String>) req_map.get("decrements");
        }
		return service.validateListDecrement(decrements, id);
	}

}
