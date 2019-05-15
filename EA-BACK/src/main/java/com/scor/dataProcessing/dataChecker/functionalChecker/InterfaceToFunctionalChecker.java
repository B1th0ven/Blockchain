package com.scor.dataProcessing.dataChecker.functionalChecker;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import org.apache.spark.api.java.JavaSparkContext;

import com.scor.dataProcessing.models.ControlResults;
import com.scor.dataProcessing.sparkConnection.Connection;

public interface InterfaceToFunctionalChecker extends Serializable {
	static final JavaSparkContext sc = Connection.getContext();

	default ControlResults runStudyFileChecker(String path, String path_prod, String type, String op_start,
			String op_end, String Client, String ClientGroup, String Treaty, String distributionBrand, String country,String userId, String studyId) {
		sc.setJobGroup(userId+studyId,"Job datacheck Functional",true);

		try {
			return StudyFunctionalChecker.getInstance().run(path, path_prod, type, op_start, op_end, Client,
					ClientGroup, Treaty, distributionBrand, country);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	};

	default ControlResults runExpTableChecker(String path, String type, HashMap<String, Integer> maxValues,
			HashMap<String, Integer> minValues) {
		return ExpTableFunctionalChecker.getInstance().run(path, type, maxValues, minValues);
	}

	default HashMap<String, Long> runCalculator(String path, String type) {
		return ExpTableCalulator.getInstance().runCalculator(path, type);
	}

}
