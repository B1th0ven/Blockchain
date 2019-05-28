package com.scor.dataProcessing.Helpers;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.Function;

public class SortByIDs implements Function<String, String> {


	private static final long serialVersionUID = 7195907909623113216L;
	private List<String> names;
	private String by;

	public SortByIDs(List<String> names, String by) {
		this.names = names;
		this.by = by;
	}

	@Override
	public String call(String line) throws Exception {

		String sorted = "";
		if (names.contains(by)) {
			sorted = line.split(";")[names.indexOf(by)].trim();
		}
		return sorted;

}
}