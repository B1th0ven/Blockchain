package com.scor.dataProcessing.dataChecker.schemaChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaRDD;



public class ProductSchemaCheker implements InterfaceToSchemaChecker {

	
	private static final long serialVersionUID = -1173186533237159343L;
	private static final ProductSchemaCheker instance = new ProductSchemaCheker();

	private ProductSchemaCheker() {
	}

	public static ProductSchemaCheker getInstance() {
		return instance;
	};

	public List<List<String>> run(String path) {
		JavaRDD<String> data = sc.textFile(path);
		String header = data.first();

		List<String> cols = new ArrayList<String>(Arrays.asList(header.toLowerCase().trim().split(";")));

		return Arrays.asList(
				new ArrayList<String>(
						pivColsProduct.stream().filter(s -> cols.contains(s)).collect(Collectors.toList())),
				compColsProduct.stream().filter(s -> !cols.contains(s)).collect(Collectors.toList()),
				cols.stream().filter(s -> !pivColsProduct.contains(s)).collect(Collectors.toList()),
				cols.stream().filter(i -> Collections.frequency(cols, i) > 1).distinct().collect(Collectors.toList()));
	}

}
