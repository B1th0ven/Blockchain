package com.scor.dataProcessing.common;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class ResultColumnPivot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4266945961725512207L;
	private Map<String, RefResultColumn> resultColumn = null;
//	private Map<String, RefResultColumn> policyPivot = null;
//	private Map<String, RefResultColumn> productPivot = null;

	public ResultColumnPivot() {
		this.resultColumn = getSchema("data/pivotColumn.csv");
	}

	public Map<String, RefResultColumn> getResultColumn() {
		if (resultColumn == null || resultColumn.isEmpty()) {
			resultColumn = getSchema("data/pivotColumn.csv");
		}
		return resultColumn;
	}
	
	public Map<String, RefResultColumn> getPolicyPivot() {
		return getSchema("data/tableau/policy.csv");
	}
	
	public Map<String, RefResultColumn> getProductPivot() {
		return getSchema("data/tableau/product.csv");
	}
	
	public Map<String, RefResultColumn> getIbnrUdfPivot() {
		return getSchema("data/tableau/ibnr_udf.csv");
	}
	
	public Map<String, RefResultColumn> getIbnrAllocationPivot() {
		return getSchema("data/tableau/ibnr_allocation.csv");
	}
	
	public Map<String, RefResultColumn> getIbnrAmountPivot() {
		return getSchema("data/tableau/ibnr_amount.csv");
	}
	
	
	private Map<String, RefResultColumn> getSchema(String fileName) {
		Map<String, RefResultColumn> schema = new HashMap<String, RefResultColumn>();
		Stream<String> lines = null;
		try {
			lines = Files.lines(Paths.get(DataPivot.class.getClassLoader().getResource(fileName).toURI()),
					Charset.forName("Cp1252"));

			lines.forEach(line -> {
				if (!line.equals("")) {
					String[] arr = line.toLowerCase().split(";", -1);
					String name = arr[0];
					String type = arr[1];
					int index = Integer.parseInt(arr[2]);
					RefResultColumn a = new RefResultColumn(name, type, index);
					schema.put(name, a);
				}
			});
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return schema;
	}

}
