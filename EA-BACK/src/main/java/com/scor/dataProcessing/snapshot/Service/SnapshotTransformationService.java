package com.scor.dataProcessing.snapshot.Service;

import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.snapshot.Operations.Utils.CombinePolicyFiles;
import com.scor.dataProcessing.snapshot.Operations.Utils.RDDTransformation;
import com.scor.dataProcessing.snapshot.Operations.Utils.SaveRDDinCsv;
import com.scor.dataProcessing.snapshot.Operations.Utils.SnapShotGroupBy;
import com.scor.dataProcessing.snapshot.Operations.transformers.*;
import com.scor.dataProcessing.sparkConnection.Connection;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.spark.sql.types.StructType;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Service
public class SnapshotTransformationService implements Serializable {



    @Autowired
    SnapShotGroupBy snapShotGroupBy ;
    @Autowired
    CombinePolicyFiles combinePolicyFiles ;

    static final JavaSparkContext sc = Connection.getContext();
    static final SparkSession sparkSession = Connection.getSession();


    @Autowired
    SaveRDDinCsv saveRDDinCsv ;

    @Autowired
    RDDTransformation rddTransformation;

	public String transformationOrchestrator(List<String> paths, String portfolio_inception_date, String first_snapshot,
                                             String annual_snapshot_extraction_timing, String studyId,
                                             Boolean missingValues, String datasetId, String reportingMin, String reportingMax,List<String> allHeaders,
                                  Boolean exposure_holes) throws IOException {


        List<String> allHeadersLowercase = new ArrayList<>();
        allHeaders.forEach(s -> allHeadersLowercase.add(s.toLowerCase()));
	    Dataset<Row> result = runTransformationSnapShot(paths, portfolio_inception_date, first_snapshot,
				annual_snapshot_extraction_timing,missingValues,reportingMin,reportingMax,allHeadersLowercase,exposure_holes );


       // result1 = result1.drop("incremented_id","observation_min","observation_max");
        result = result.drop("incremented_id");


      String path =   saveRDDinCsv.saveCsv(result,studyId,datasetId);

		// TODO INJECT DATASET INTO CSV AND RETURN PATH

		return path;
	}

    public Dataset<Row> runTransformationSnapShot(List<String> paths, String portfolio_inception_date, String first_snapshot, String annual_snapshot_extraction_timing,
                                                  Boolean missingValues, String reportingMin, String reportingMax, List<String> names, Boolean exposure_holes){

        names =  updateHeaders(names) ;
        List<String> listHeaderToallwaysExist = new ArrayList<>() ;
        listHeaderToallwaysExist.add(Headers.STATUS_END_CURRENT_CONDITION);
        listHeaderToallwaysExist.add(Headers.STATUS_BEGIN_CURRENT_CONDITION);
        listHeaderToallwaysExist.add(Headers.DATE_OF_END_CURRENT_CONDITION);
        listHeaderToallwaysExist.add(Headers.DATE_OF_BEGIN_CURRENT_CONDITION);

        for (String i : listHeaderToallwaysExist) {
            if (!names.contains(i.toLowerCase())) {
                names.add(i);
            }
        }


        JavaRDD<String> data = combinePolicyFiles.combinePolicyFiles(paths,names) ;

        List<String> tooSee = data.collect();
        tooSee.size();



        String  variableTime;
        if (names.contains(Headers.REPORTING_MONTH)) {
         variableTime = Headers.REPORTING_MONTH ;
        }
        else if (names.contains(Headers.REPORTING_YEAR)) {
            variableTime = Headers.REPORTING_YEAR; }
        else {
            variableTime =Headers.REPORTING_QUARTER ;
        }

		Integer reportPeriodIndex = names.indexOf(variableTime);
		Integer Status_begin_current_condition_index = names.indexOf(Headers.STATUS_BEGIN_CURRENT_CONDITION) ;

       // Map<String,String> MapOfDateEnd = snapShotGroupBy.MapDateOfEndCurrentConditionByIds(data, names,reportPeriodIndex) ;
        JavaRDD<String> resultEvent = data.map(new ValidateEvent(names,annual_snapshot_extraction_timing)) ;

        //PARTIE MISSING VALUE

        JavaRDD<String> pipeline = resultEvent ;



        if(missingValues != null && missingValues == true) {

            JavaPairRDD<String, List<List<String>>> noMoreMissingValues = rddTransformation.missingValuePair(resultEvent, names, reportPeriodIndex);
            pipeline = rddTransformation.getJavaRDDBack(noMoreMissingValues);
        }


        JavaRDD<String> pipelineSecondStage = null;
        if( exposure_holes != null && exposure_holes == true){
            JavaPairRDD<String, List<List<String>>> sortedDataIncrementedId = rddTransformation.incrementedIdPairWithStatusOnly(pipeline, names, reportPeriodIndex, variableTime, Status_begin_current_condition_index);
            pipelineSecondStage = rddTransformation.getJavaRDDBack(sortedDataIncrementedId);
        }
        else {
            JavaPairRDD<String, List<List<String>>> sortedDataIncrementedId = rddTransformation.incrementedIdPairWithStatusAndReporting(pipeline, names, reportPeriodIndex, variableTime, Status_begin_current_condition_index);
            pipelineSecondStage = rddTransformation.getJavaRDDBack(sortedDataIncrementedId);
        }





        JavaRDD<String> pipelineThirdStage = rddTransformation.groupByObservationMinMax(pipelineSecondStage,names,reportPeriodIndex);


        String dateOfEndCurrentCondition = Headers.DATE_OF_END_CURRENT_CONDITION ;
        String dateOfEventIncurred = Headers.DATE_OF_EVENT_INCURRED ;
        String dateOfBeginCurrentCondition = Headers.DATE_OF_BEGIN_CURRENT_CONDITION ;

        if(exposure_holes != null && exposure_holes == true) {
        names.add("incremented_id");}
        names.remove(variableTime);
        names.add("observation_min");
        names.add("observation_max");

        System.out.println(names);
        String maxdateOfEndCurrentCondition = snapShotGroupBy.minMaxVariableDate(data,names,dateOfEndCurrentCondition,false ) ;
        String mindateOfBeginCurrentCondition = snapShotGroupBy.minMaxVariableDate(data,names,dateOfBeginCurrentCondition,true ) ;

       // Map<String,String> MapdateOfEndCurrentCondition = snapShotGroupBy.MapVariableByIds(data, names, dateOfEventIncurred) ;

        JavaRDD<String> resultDateOfEnd = pipelineThirdStage.map(new ValidateDateOfEndVersion2( variableTime, names,reportingMax,maxdateOfEndCurrentCondition,
             annual_snapshot_extraction_timing)) ;


        JavaRDD<String> resultDateOfBegin = resultDateOfEnd.map(new ValidateDateOfBegin(variableTime, names,reportingMin,portfolio_inception_date,first_snapshot,
                mindateOfBeginCurrentCondition,annual_snapshot_extraction_timing)) ;

        JavaRDD<String> resulTStatus = resultDateOfBegin.map(new ValidateStatus(variableTime,names,reportingMax)) ;
        JavaRDD<String> resultExposure = resulTStatus.map(new ValidateExposure(names,annual_snapshot_extraction_timing)) ;




        List<StructField> a  = new ArrayList<>() ;
        for (String col : names ) {
            a.add(new StructField(col,DataTypes.StringType,true, Metadata.empty()));
        }
        StructField[] b = a.stream().toArray(StructField[] :: new ) ;
        StructType schema = new StructType(b);

        JavaRDD<Row> rowJavaRDD =  resultExposure.map( (row) -> RowFactory.create(row.split(";",-1)) );

        Dataset<Row> test = sparkSession.createDataFrame(rowJavaRDD,schema);
        return test ;
    }


public List<String> updateHeaders (List<String> names)
{

    List<String> listHeaderToallwaysExist = new ArrayList<>() ;
    listHeaderToallwaysExist.add(Headers.STATUS_END_CURRENT_CONDITION);
    listHeaderToallwaysExist.add(Headers.STATUS_BEGIN_CURRENT_CONDITION);
    listHeaderToallwaysExist.add(Headers.DATE_OF_END_CURRENT_CONDITION);
    listHeaderToallwaysExist.add(Headers.DATE_OF_BEGIN_CURRENT_CONDITION);

    for (String i : listHeaderToallwaysExist) {
        if (!names.contains(i.toLowerCase())) {
            names.add(i);
        }
    }
return names ;
}





}
