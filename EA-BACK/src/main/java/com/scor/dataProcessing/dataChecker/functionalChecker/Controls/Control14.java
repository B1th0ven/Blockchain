package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;
import org.apache.spark.util.LongAccumulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Control14 implements IRule {
    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;
    String type;

    public Control14(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo, String type){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
        this.type = type;
    }


	@Override
	public void validate(String[] row, Product product, List<String> headers) {
		String begin_stat = row[headers.indexOf("status_begin_current_condition")].trim();
		String mainRiskType = row[headers.indexOf(Headers.MAIN_RISK_TYPE)];
		if (mainRiskType.equalsIgnoreCase("Life") || mainRiskType.equalsIgnoreCase("Health")
				|| mainRiskType.equalsIgnoreCase("CI") || mainRiskType.equalsIgnoreCase("TPD")) {
			if ((type.equalsIgnoreCase("combine")
					|| (type.equalsIgnoreCase("split") && headers.contains("exposure_or_event")
							&& ("exposure + event".equalsIgnoreCase(row[headers.indexOf("exposure_or_event")])
									|| "exposure".equalsIgnoreCase(row[headers.indexOf("exposure_or_event")]))))
					&& !"Active".equalsIgnoreCase(begin_stat)) {
				List<AffectedColumn> affectedColumns = new ArrayList<>();
				affectedColumns.add(new AffectedColumn("exposure termination", 1,
						new ArrayList<>(Arrays.asList(String.join(";", row)))));
				controlResult.add(new ControlResult("No exposure following terminating status at end condition",
						affectedColumns));
				errorsCount.add(1);
			}
		}
	}
}
