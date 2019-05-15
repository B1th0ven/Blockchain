package com.scor.dataProcessing.ExpTab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class ExpTabController implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7740815298135224102L;
	@Autowired
    ControlService cs;

    @RequestMapping(value="run/exp/checkbase/{sid}/{rid}/{bid}",method = RequestMethod.POST)
    public List<String> CheckBase(@PathVariable("sid") int sid,@PathVariable("rid") int rid,@PathVariable("bid") int bid,HttpEntity<String> req) throws IOException {
        HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
        String dimensions = (String) req_map.get("dimensions");
        Boolean isDateOfCommencement = (Boolean) req_map.get("isDateOfCommencement");
        Boolean isDateOfBirth = (Boolean) req_map.get("isDateOfBirth");
        String exposureMetric = (String) req_map.get("exposureMetric");
        List<Integer> otherTables = (List<Integer>) req_map.get("otherTables");
        boolean isAgeAtCom = (boolean) req_map.get("isAgeAtCommencement");
        boolean date_of_commencement = isDateOfCommencement!= null ? isDateOfCommencement : false;
        boolean date_of_birth = isDateOfBirth!= null ? isDateOfBirth : false;
        return cs.checkBase(bid,rid,sid,dimensions,date_of_commencement,date_of_birth,exposureMetric,otherTables,isAgeAtCom);
    }

    @RequestMapping(value="run/exp/check/{type}/{bid}/{id}",method = RequestMethod.POST)
    public List<String> CheckTrendAdj(@PathVariable("bid") int bid,@PathVariable("id") int id,@PathVariable("type") String type,HttpEntity<String> req) throws IOException {
        HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
        String dimensions = (String) req_map.get("dimensions");
        Boolean isDateOfCommencement = (Boolean) req_map.get("isDateOfCommencement");
        Boolean isDateOfBirth = (Boolean) req_map.get("isDateOfBirth");
        List<Integer> otherTables = (List<Integer>) req_map.get("otherTables");
        boolean isAgeAtCom = (boolean) req_map.get("isAgeAtCommencement");
        boolean date_of_commencement = isDateOfCommencement!= null ? isDateOfCommencement : false;
        boolean date_of_birth = isDateOfBirth!= null ? isDateOfBirth : false;
        return cs.checkAdjTrend(id,bid,type,dimensions,date_of_commencement,date_of_birth,otherTables,isAgeAtCom);
    }
    
    @RequestMapping(value="run/exp/checkcalibration",method = RequestMethod.POST)
    public List<String> CheckCalibration(HttpEntity<String> req) throws Exception
    {
    	HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
		String path = (String) req_map.get("path");
        String userId = (String) req_map.get("userId");
        String studyId = (String) req_map.get("studyId");
        return cs.checkCalibration(path,userId,studyId);
    }
}
