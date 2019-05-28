package com.scor.dataProcessing.snapshot.Operations.Utils;

import com.scor.dataProcessing.sparkConnection.Connection;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.apache.spark.sql.functions.*;

@Service
public class CombinePolicyFiles implements Serializable {

    private static final long serialVersionUID = 4344894819308196937L;

    @Autowired
    SortingDataAccordingToAllHeaders sortingDataAccordingToAllHeaders;

    static final JavaSparkContext sc = Connection.getContext();
    static final SparkSession sparkSession = Connection.getSession();

    public JavaRDD<String> combinePolicyFiles(List<String> paths, List<String> allHeaders) {

        StructType schemaGeneral = getSchema(allHeaders) ;
        Dataset<Row> dataSetToUnion = sparkSession.createDataFrame(sc.emptyRDD(), schemaGeneral);
        Dataset<Row> datasetUnion = sortingDataAccordingToAllHeaders.cleaningdata(dataSetToUnion, allHeaders);




        for (String path : paths) {
            JavaRDD<String> data = sc.textFile(path);
            String header = data.first();
            List<String> names = Arrays.asList(header.toLowerCase().split(";", -1));

            JavaRDD<String> data_without_header = data
                    .filter(line -> !line.equalsIgnoreCase(header) && !line.isEmpty());
           
            StructType schema = getSchema(names) ; 
        
            JavaRDD<Row> rowJavaRDD = data_without_header.map((row) -> RowFactory.create(row.split(";",-1)));

            Dataset<Row> dataSetGeneratedFromRdd = sparkSession.createDataFrame(rowJavaRDD, schema);

            for (String i : allHeaders) {
                if (!names.contains(i.toLowerCase())) {
                    dataSetGeneratedFromRdd = dataSetGeneratedFromRdd.withColumn(i.toLowerCase(), lit(""));
                }
            }

            Dataset<Row> datasetCleaned = sortingDataAccordingToAllHeaders.cleaningdata(dataSetGeneratedFromRdd, allHeaders);
            datasetUnion = datasetUnion.union(datasetCleaned);

        }

        JavaRDD<Row> resulatRDD = datasetUnion.toJavaRDD();
        JavaRDD<String> result = resulatRDD.map(p -> p.mkString(";"));

        return result;
    }
public StructType getSchema (List<String> names) {

    List<StructField> listeOfFieldsOfOnePath = new ArrayList<>();
    for (String col : names) {
        listeOfFieldsOfOnePath.add(new StructField(col, DataTypes.StringType, true, Metadata.empty()));
    }
    StructField[] arrayOfFields = listeOfFieldsOfOnePath.stream().toArray(StructField[]::new);
    StructType schema = new StructType(arrayOfFields);
    return schema;
}

}

