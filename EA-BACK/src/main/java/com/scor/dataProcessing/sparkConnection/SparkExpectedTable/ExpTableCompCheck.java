package com.scor.dataProcessing.sparkConnection.SparkExpectedTable;
//package com.scor.dataProcessing.spark.SparkExpectedTable;
//
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//
//import com.scor.dataProcessing.common.ExpTablePivot;
//import com.scor.dataProcessing.spark.Connection;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class ExpTableCompCheck implements Serializable {
//    /**
//	 * 
//	 */
//	private static final long serialVersionUID = -7109226075010789905L;
//	static JavaSparkContext sc = Connection.getContext();
//
//    public  List<List<String>> run(String path, String type) throws Exception {
//        JavaRDD<String> data = sc.textFile(path);
//        String header = data.first();
//
//        List<String> filteredCompCols = new ArrayList<>() ;
//        List<String> compCols = ExpTablePivot.getCompCols(type.trim().toLowerCase());
//        List<String> pivCols = ExpTablePivot.getPivotCols(type.trim().toLowerCase()).stream().map(s -> s.getName()).collect(Collectors.toList());
//
//        List<String> cols = new ArrayList<String>(Arrays.asList(header.toLowerCase().trim().split(";",-1)));
//
//        if(!cols.contains("age_attained") && !cols.contains("age_at_commencement"))
//            filteredCompCols = compCols.stream().filter(s -> !s.equalsIgnoreCase("age_at_commencement_definition")).collect(Collectors.toList());
//        else filteredCompCols.addAll(compCols);
//
//        if(cols.contains("age_attained")){
//            filteredCompCols.add("attained_age_definition");
//        }
//
//		ArrayList<String> colInsidePivot = new ArrayList<String>(
//				pivCols.stream().filter(s -> cols.contains(s)).collect(Collectors.toList()));
//		List<String> colCompulsoryNotFound = filteredCompCols.stream().filter(s -> !cols.contains(s))
//				.collect(Collectors.toList());
//		List<String> colOutsidePivot = cols.stream().filter(s -> !pivCols.contains(s)).collect(Collectors.toList());
//		
//		List<String> duplicatedColumns = cols.stream().filter(i -> Collections.frequency(cols, i) > 1).distinct()
//				.collect(Collectors.toList());
//		
//		return Arrays.asList(colInsidePivot, colCompulsoryNotFound, colOutsidePivot,duplicatedColumns);
//    }
//}
