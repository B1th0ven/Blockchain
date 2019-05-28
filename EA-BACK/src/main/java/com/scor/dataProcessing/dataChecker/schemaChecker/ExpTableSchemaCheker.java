package com.scor.dataProcessing.dataChecker.schemaChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.spark.api.java.JavaRDD;
import com.scor.dataProcessing.common.ExpTablePivot;



public class ExpTableSchemaCheker implements InterfaceToSchemaChecker {

	private static final long serialVersionUID = 8181456300154390337L;
	private static final ExpTableSchemaCheker instance = new ExpTableSchemaCheker();

	public static ExpTableSchemaCheker getInstance() {
		return instance;
	}
	
	private ExpTableSchemaCheker() {
	};

	List<List<String>> run(String path, String type) throws Exception {
		JavaRDD<String> data = sc.textFile(path);
		String header = data.first();

		List<String> filteredCompCols = new ArrayList<>();
		List<String> compCols = ExpTablePivot.getCompCols(type.trim().toLowerCase());
		List<String> pivCols = ExpTablePivot.getPivotCols(type.trim().toLowerCase()).stream().map(s -> s.getName())
				.collect(Collectors.toList());

		List<String> cols = new ArrayList<String>(Arrays.asList(header.toLowerCase().trim().split(";", -1)));

		if (!cols.contains("age_attained") && !cols.contains("age_at_commencement"))
			filteredCompCols = compCols.stream().filter(s -> !s.equalsIgnoreCase("age_at_commencement_definition"))
					.collect(Collectors.toList());
		else
			filteredCompCols.addAll(compCols);

		if (cols.contains("age_attained")) {
			filteredCompCols.add("attained_age_definition");
		}

		ArrayList<String> colInsidePivot = new ArrayList<String>(
				pivCols.stream().filter(s -> cols.contains(s)).collect(Collectors.toList()));
		List<String> colCompulsoryNotFound = filteredCompCols.stream().filter(s -> !cols.contains(s))
				.collect(Collectors.toList());
		List<String> colOutsidePivot = cols.stream().filter(s -> !pivCols.contains(s)).collect(Collectors.toList());

		List<String> duplicatedColumns = cols.stream().filter(i -> Collections.frequency(cols, i) > 1).distinct()
				.collect(Collectors.toList());

		return Arrays.asList(colInsidePivot, colCompulsoryNotFound, colOutsidePivot, duplicatedColumns);
	}

}
