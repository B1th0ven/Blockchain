package com.scor.dataProcessing.sparkUtils;

import com.scor.dataProcessing.sparkConnection.Connection;
import com.scor.persistance.services.StudyService;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.scheduler.ActiveJob;
import org.apache.spark.scheduler.DAGScheduler;

import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class SparkService implements Serializable {

    private final static Logger LOGGER = Logger.getLogger(StudyService.class);

    static final JavaSparkContext sc = Connection.getContext();



    public void  cancel(String userId, String studyId){
        String id  =userId+studyId;

               sc.cancelJobGroup(id);
        LOGGER.log(Level.INFO,"the job with id = " + id + "was terminated");
    }
}
