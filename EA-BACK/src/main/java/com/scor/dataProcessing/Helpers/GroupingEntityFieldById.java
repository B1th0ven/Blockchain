package com.scor.dataProcessing.Helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.api.java.function.PairFunction;

import com.scor.dataProcessing.common.DataPivot;

import scala.Tuple2;

public class GroupingEntityFieldById implements PairFunction<String, String, List<String>> {

	private static final long serialVersionUID = -2762861907652372843L;
	List<String> names;
	String[] entityIdsName;
	List<String> entityFieldsName;

	public GroupingEntityFieldById(List<String> names, String[] entityIdsName, List<String> entityFieldsName) {
		this.names = names;
		this.entityIdsName = entityIdsName;
		this.entityFieldsName = entityFieldsName;
	}

	@Override
	public Tuple2<String, List<String>> call(String row_str) throws Exception {

		String[] row = row_str.toLowerCase().trim().split(";", -1);
		String entityId = row[names.indexOf(entityIdsName[0])];
		for (int i = 1; i < entityIdsName.length; i++) {
			entityId += ";" + row[names.indexOf(entityIdsName[i])];
		}

		List<String> entityFieldsValue = new ArrayList<String>();
		entityFieldsName.stream().forEach(col -> {
			if (names.indexOf(col) > -1)
				entityFieldsValue.add(row[names.indexOf(col)]);
		});

		return new Tuple2<String, List<String>>(entityId, entityFieldsValue);

	}

}
