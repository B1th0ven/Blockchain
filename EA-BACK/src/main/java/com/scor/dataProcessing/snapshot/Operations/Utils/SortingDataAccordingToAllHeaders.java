package com.scor.dataProcessing.snapshot.Operations.Utils;

import com.scor.dataProcessing.sparkConnection.Connection;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SortingDataAccordingToAllHeaders implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7207732619730050005L;
	static final JavaSparkContext sc = Connection.getContext();
    static final SparkSession sparkSession = Connection.getSession();

public Dataset<Row> cleaningdata (Dataset<Row > test, List<String> Allheaders) {
    test.registerTempTable("test");
    String querry = "SELECT ";

    for (String head : Allheaders) {
         querry = querry + head + ",";
    }
    querry  = querry.substring(0, querry.length()-1) ;
    querry = querry + " FROM test";
    Dataset<Row> data1 = sparkSession.sql(querry);
    return (data1);

}

}


