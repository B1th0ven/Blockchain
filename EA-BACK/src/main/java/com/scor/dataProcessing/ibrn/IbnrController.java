package com.scor.dataProcessing.ibrn;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
public class IbnrController implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1649247525916979973L;
	@Autowired
    IbnrService service;
    @RequestMapping(value="ibnr/compulsory",method = RequestMethod.POST)
    public List<String> ibnrCompControl(HttpEntity<String> req) throws Exception
    {
        HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody() , HashMap.class) ;
        String path = (String) req_map.get("path") ;
        String type = (String) req_map.get("type") ;

        return service.compoloryCheck(path,type);
    }

    @RequestMapping(value="ibnr/technical",method = RequestMethod.POST)
    public List<String> ibnrPossibleValuesControl(HttpEntity<String> req) throws Exception
    {
        HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody() , HashMap.class) ;
        String path = (String) req_map.get("path") ;
        String type = (String) req_map.get("type") ;

        return service.techControls(path,type);
    }
    
    @RequestMapping(value="ibnr/functional",method = RequestMethod.POST)
    public List<String> ibnrFunctionalControl(HttpEntity<String> req) throws Exception
    {
        HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody() , HashMap.class) ;
        String path = (String) req_map.get("path") ;
        String type = (String) req_map.get("type") ;
        List<String> runDimensions = new ArrayList<>();
        if(req_map.get("runDimensions") instanceof List<?>) {
            runDimensions = (List<String>) req_map.get("runDimensions");
        }
        int runStudyId = (int) req_map.get("runStudyId");
        return service.functionalControls(path,type, runDimensions, runStudyId);
    }
    
    @RequestMapping(value="ibnr/amountAllocationControl",method = RequestMethod.POST)
    public List<String> amountAllocationControl(HttpEntity<String> req) throws Exception
    {
        HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody() , HashMap.class) ;
        String pathAmount = (String) req_map.get("pathAmount") ;
        String pathAllocation = (String) req_map.get("pathAllocation") ;
        if(StringUtils.isBlank(pathAmount)) {
        	return Arrays.asList("Missing Amount File to check combination of Portfolio_Origin, Bucket_ID et Decrements");
        }

        return service.amountAllocationControl(pathAmount,pathAllocation);
    }

}
