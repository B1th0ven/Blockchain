package com.scor.dataProcessing.dataChecker.functionalChecker.Operations.ExpTableOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.util.LongAccumulator;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;

public class MissingValuesControl implements VoidFunction<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3076236576073161206L;
	
	private List<String> names;
	private ControlResultAccumulator missingValueAccumulator;
	private List<Integer> indexs;
	private LongAccumulator errorsCount;
	
	public MissingValuesControl(List<String> missingValuesHeaders, List<String> names, ControlResultAccumulator missingValueAccumulator, LongAccumulator errorsCount) {
		this.names = names;
		this.missingValueAccumulator = missingValueAccumulator;
		indexs = new ArrayList<>();
		for (String variable : missingValuesHeaders) {
			indexs.add(names.indexOf(variable));
		}
		this.errorsCount = errorsCount;
	}

	@Override
	public void call(String line) throws Exception {
		String[] row = line.split(";",-1);
		for (int index : indexs) {
			if(StringUtils.isBlank(row[index])) {
				List<AffectedColumn> affectedColumns = new ArrayList<>();
                affectedColumns.add(new AffectedColumn(names.get(index), 1,
                        new ArrayList<>(Arrays.asList(line))));
                missingValueAccumulator.add(new ControlResult("Missing values",affectedColumns));
                errorsCount.add(1);
			}
		}
		
	}

}
