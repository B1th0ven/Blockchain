package com.scor.dataProcessing.snapshot.Operations.Utils;

import com.google.common.collect.Ordering;
import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.snapshot.dto.MinMaxDto;
import com.scor.dataProcessing.sparkConnection.Connection;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;


@Service
public class CheckReportingPeriod implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8090915948278075973L;






    public String getMinReportingPeriod(List<String> minReportingPeriods){
        String min  = (minReportingPeriods.stream().min(String::compareTo).get());
        return min;
    }

    public String getMaxReportingPeriod(List<String> maxReportingPeriods) {
        String max  = (maxReportingPeriods.stream().min(String::compareTo).get());
        return max;
    }
}
