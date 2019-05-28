package com.scor.dataProcessing.dataChecker.integrityChecker;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaSparkContext;

import com.scor.dataProcessing.models.ControlResults;
import com.scor.dataProcessing.sparkConnection.Connection;

public interface InterfaceToIntegrityChecker extends Serializable {

	static final JavaSparkContext sc = Connection.getContext();

	default ControlResults run(Map<String, List<String>> refData, String path, String type, String checker,String userId,String studyId) {
		sc.setJobGroup(userId+studyId,"Job datacheck Integrity",true);
		if ("policy".equalsIgnoreCase(checker)) {
			return PolicyIntegrityChecker.getInstance().run(refData, path, type);
		}
		if ("product".equalsIgnoreCase(checker)) {
			return ProductIntegrityChecker.getInstance().run(refData, path);
		}
		if ("expTable".equalsIgnoreCase(checker)) {
			try {
				return ExpTableIntegrityChecker.getInstance().run(refData, path, type);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Invalid Type Of File");
			}
		}
		return null;
	};

}
