package com.scor.dataProcessing.snapshot.Service;

import com.scor.dataProcessing.snapshot.Operations.checkers.CheckReporting;
import com.scor.dataProcessing.snapshot.Operations.checkers.Control48;
import com.scor.dataProcessing.snapshot.Operations.checkers.CheckDate;
import com.scor.dataProcessing.sparkConnection.Connection;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SnapshotControlService {


    static final JavaSparkContext sc = Connection.getContext();


    @Autowired
    CheckReporting checkReportingservice ;
    @Autowired
    CheckDate checkDate ;
    @Autowired
    Control48 control48 ;

    public HashMap<String,String> ControlSnapshotOrchestrator(List<String> paths, String portfolio, String minReportingPeriod, String maxReportingPeriod, List<String> allHeaders){
        HashMap<String, String> resultMap = new HashMap<>();

        String isCheckReporting = checkReportingservice.checkReporting(paths);

        resultMap.put("Reporting frequency consistency in snapshots", isCheckReporting);

        if ("true".equalsIgnoreCase(isCheckReporting)){
            resultMap.put("No unexplained disappearance in snapshots" , control48.control48(paths,maxReportingPeriod));

            resultMap.put("No inception / renewal after ealiest reporting period", checkDate.checkDate(portfolio, allHeaders,minReportingPeriod));
        }
        else {
            resultMap.put("No unexplained disappearance in snapshots","false");

            resultMap.put("No inception / renewal after ealiest reporting period","false");
        }



        return resultMap ;

    }



}
