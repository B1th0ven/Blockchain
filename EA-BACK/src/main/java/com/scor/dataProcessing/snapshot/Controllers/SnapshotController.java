package com.scor.dataProcessing.snapshot.Controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scor.dataProcessing.snapshot.Operations.Utils.CheckReportingPeriod;
import com.scor.dataProcessing.snapshot.Service.SnapshotControlService;
import com.scor.dataProcessing.snapshot.Service.SnapshotPersistence;
import com.scor.dataProcessing.snapshot.Service.SnapshotTransformationService;
import com.scor.dataProcessing.snapshot.Service.SnapshotTransformationService;
import com.scor.persistance.entities.SnapshotFilesEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SnapshotController implements Serializable {

    @Autowired
    SnapshotTransformationService snapshotTransformationService;

    @Autowired
    SnapshotControlService snapshotControlService;

    @Autowired
    SnapshotPersistence snapshotPersistence;

    @Autowired
    CheckReportingPeriod checkReportingPeriod;


    // CheckHeaders  3 controles
    @RequestMapping(value = "/SnapShotIntegrityControls", method = RequestMethod.POST)
    public HashMap<String, String> CheckHeaderss(HttpEntity<String> req) throws IOException {
        HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
        List<String> paths = (List<String>) req_map.get("listPolicySnapshotPath");
        String portfolioInceptionDate = (String) req_map.get("portfolioInceptionDate");
        String firstSnapshot = (String) req_map.get("firstSnapshot");
        String AnnualSnapshotExtractionTiming = (String) req_map.get("AnnualSnapshotExtractionTiming");
        String studyId = (String) req_map.get("studyId");
        Boolean missingvalues = true ;
        Boolean exposure_holes = (Boolean) req_map.get("exposureHoles");
        String datasetid = (String) req_map.get("datasetid");
        List<String> allHeaders = (List<String>) req_map.get("allCols");
		/*
		List<List<String>> allHeaders = (List<List<String>>) req_map.get("allCols") ;

		 */
        List<String> minReportingPeriods = (List<String>) req_map.get("minReportingPeriod");
        List<String> maxReportingPeriods = (List<String>) req_map.get("maxReportingPeriod");

        String reportingMin = checkReportingPeriod.getMinReportingPeriod(minReportingPeriods).toLowerCase();
        String reportingMax = checkReportingPeriod.getMaxReportingPeriod(maxReportingPeriods).toLowerCase();


        HashMap<String, String> snapshotcontrolResult = snapshotControlService.ControlSnapshotOrchestrator(paths, portfolioInceptionDate, reportingMin, reportingMax,allHeaders );

        if ("true".equalsIgnoreCase(snapshotcontrolResult.get("No inception / renewal after ealiest reporting period"))
                && "true".equalsIgnoreCase(snapshotcontrolResult.get("Reporting frequency consistency in snapshots"))) {

            String pathResult = snapshotTransformationService.transformationOrchestrator(paths, portfolioInceptionDate,
                    firstSnapshot, AnnualSnapshotExtractionTiming, studyId, missingvalues, datasetid, reportingMin, reportingMax,allHeaders,exposure_holes);
            if (StringUtils.isNotBlank(pathResult))
                snapshotcontrolResult.put("path", pathResult);
        }

        return snapshotcontrolResult;
    }


    @RequestMapping(value = "/datasets/snapshot", method = RequestMethod.POST)
    public void testPersistence(HttpEntity<String> req) throws IOException {
        SnapshotFilesEntity[] snapshotFilesEntities = new SnapshotFilesEntity[0];
        Gson gson = new GsonBuilder().create();
        snapshotFilesEntities = gson.fromJson(req.getBody(), snapshotFilesEntities.getClass());
        snapshotPersistence.testInsertintoSnapshotTable(Arrays.asList(snapshotFilesEntities));
    }

    @RequestMapping(value = "/datasets/snapshot/{id}", method = RequestMethod.GET)
    public List<SnapshotFilesEntity> testget(@PathVariable("id") Integer id) {
        return snapshotPersistence.testSelectSnapshotTable(id);
    }


}




