package com.scor.dataProcessing.dataChecker.notExecutedRuleChecker;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.scor.dataProcessing.models.NotExecutedDto;
import com.scor.dataProcessing.sparkConnection.Connection;

public interface InterfaceToNotExecutedRuleChecker extends Serializable {
	
	static final JavaSparkContext sc = Connection.getContext();

	default List<NotExecutedDto> run(String path, String path_product, String type, String op_start, String op_end,String userId, String studyId) {
		sc.setJobGroup(userId+studyId,"Job datacheck not executed",true);
		return NotExecutedRuleChecker.getInstance().run(path, path_product, type, op_start, op_end);
	}

}
