package com.scor.dataProcessing.dataChecker.schemaChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.scor.dataProcessing.Helpers.Headers;
import org.apache.spark.api.java.JavaRDD;



public class PolicySchemaChecker implements InterfaceToSchemaChecker {


	private static final long serialVersionUID = -5692080906652533015L;
	private static final PolicySchemaChecker instance = new PolicySchemaChecker();

	private PolicySchemaChecker() {
	};

	public static PolicySchemaChecker getInstance() {
		return instance;
	}

	public List<List<String>> run(String path, String type) {
		JavaRDD<String> data = sc.textFile(path);
		String header = data.first();
		List<String> HeadersCols = new ArrayList<String>(Arrays.asList(header.toLowerCase().trim().split(";", -1)));
		List<String> cols = new ArrayList<String>(Arrays.asList(header.toLowerCase().trim().split(";", -1)));
		List<String> columns = pivCols.stream().filter(s -> HeadersCols.contains(s)).collect(Collectors.toList());
		List<String> missingColumns = compCols.stream().filter(s -> !cols.contains(s)).collect(Collectors.toList());
		List<String> ignoredColumns = cols.stream().filter(s -> !pivCols.contains(s)).collect(Collectors.toList());
		List<String> duplicatedColumns = cols.stream().filter(i -> Collections.frequency(cols, i) > 1).distinct()
				.collect(Collectors.toList());
		if (type.equalsIgnoreCase("combine")) {
			if (cols.contains("exposure_or_event")) {
				columns.remove("exposure_or_event");
				ignoredColumns.add("exposure_or_event");
			} else {
				missingColumns.remove("exposure_or_event");
			}
		}
		missingColumns.remove(Headers.REPORTING_MONTH);
		missingColumns.remove(Headers.REPORTING_YEAR);
		missingColumns.remove(Headers.REPORTING_QUARTER);
		if (cols.contains(Headers.REPORTING_MONTH)) {
			ignoredColumns.add(Headers.REPORTING_MONTH);
		}
		if (cols.contains(Headers.REPORTING_YEAR)) {

			ignoredColumns.add(Headers.REPORTING_YEAR);
		}
		if (cols.contains(Headers.REPORTING_QUARTER)) {

			ignoredColumns.add(Headers.REPORTING_QUARTER);
		}
		return Arrays.asList(new ArrayList<String>(columns), missingColumns, ignoredColumns, duplicatedColumns);
	}

}
