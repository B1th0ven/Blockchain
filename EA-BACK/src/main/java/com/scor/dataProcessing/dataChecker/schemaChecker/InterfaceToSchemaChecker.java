package com.scor.dataProcessing.dataChecker.schemaChecker;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaSparkContext;

import com.scor.dataProcessing.common.DataPivot;
import com.scor.dataProcessing.sparkConnection.Connection;

public interface InterfaceToSchemaChecker extends Serializable {

	static final JavaSparkContext sc = Connection.getContext();
	static List<String> compCols = DataPivot.getCompCols();
	static List<String> pivCols = DataPivot.getPivotCols().stream().map(s -> s.getName()).collect(Collectors.toList());
	static List<String> compColsProduct = DataPivot.getCompColsProduct();
	static List<String> pivColsProduct = DataPivot.getPivotColsProduct().stream().map(s -> s.getName())
			.collect(Collectors.toList());

	default List<List<String>> run(String path, String type, String checker,String userId, String studyId) {
		sc.setJobGroup(userId+studyId,"Job datacheck schema",true);
		if ("policy".equalsIgnoreCase(checker)) {
			return PolicySchemaChecker.getInstance().run(path, type);
		}
		if ("product".equalsIgnoreCase(checker)) {
			return ProductSchemaCheker.getInstance().run(path);
		}
		if ("expTable".equalsIgnoreCase(checker)) {
			try {
				return ExpTableSchemaCheker.getInstance().run(path, type);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Invalid Type Of File");
			}
		}
		return null;
	}

}
