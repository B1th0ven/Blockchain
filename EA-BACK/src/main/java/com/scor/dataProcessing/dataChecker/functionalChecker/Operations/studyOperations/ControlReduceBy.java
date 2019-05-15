package com.scor.dataProcessing.dataChecker.functionalChecker.Operations.studyOperations;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.util.LongAccumulator;

import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;

import scala.Tuple2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ControlReduceBy implements VoidFunction<Tuple2<String, String[]>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7745001207012602333L;
	private ControlResultAccumulator exposure_coherent_status;
	private String type;
	private String op_end;
	private List<String> names;
	private LongAccumulator errorsCount;

	public ControlReduceBy(ControlResultAccumulator ac, String type, String op_end, List<String> names,
			LongAccumulator errorsCount) {
		this.exposure_coherent_status = ac;
		this.type = type;
		this.op_end = op_end;
		this.names = names;
		this.errorsCount = errorsCount;

	}

	@Override
	public void call(Tuple2<String, String[]> input) throws Exception {
		String row = String.join(";", input._2);
		if (0 <= names.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)
				&& !row.equals("") & !row.equalsIgnoreCase(String.join(";", names))) {
			String[] row_arr = row.toLowerCase().trim().split(";", -1);
			String decc = row_arr[names.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)].trim();
			String secc = row_arr[names.indexOf(Headers.STATUS_END_CURRENT_CONDITION)].trim();
			String sbcc = row_arr[names.lastIndexOf(Headers.STATUS_BEGIN_CURRENT_CONDITION)].trim();
			String classification = null;
			if (type.equalsIgnoreCase("split")) {
				classification = row_arr[names.indexOf("exposure_or_event")];
			}

			if (type.equalsIgnoreCase("combine") || (type.equalsIgnoreCase("split")
					&& StringUtils.isNotBlank(classification) && classification.equalsIgnoreCase("exposure"))) {
				if (decc.isEmpty()) {
					if (!secc.isEmpty() && !secc.equalsIgnoreCase("active")) {
						List<AffectedColumn> affectedColumns = new ArrayList<>();
						affectedColumns.add(new AffectedColumn("exposure end coherent with status", 1,
								new ArrayList<>(Arrays.asList(row))));
						exposure_coherent_status
								.add(new ControlResult("Exposure end coherent with status ", affectedColumns));
						errorsCount.add(1);
					}
				}
				if (!decc.isEmpty() && decc.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && op_end != null
						&& op_end.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
					LocalDate end = LocalDate.parse(decc, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
					LocalDate opend = LocalDate.parse(op_end,
							DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));

					if ((secc.isEmpty() || secc.equalsIgnoreCase("active")) && !end.isEqual(opend)) {

						List<AffectedColumn> affectedColumns = new ArrayList<>();
						affectedColumns.add(new AffectedColumn("exposure end coherent with status", 1,
								new ArrayList<>(Arrays.asList(row))));
						exposure_coherent_status
								.add(new ControlResult("Exposure end coherent with status ", affectedColumns));
						errorsCount.add(1);
					}

					if (sbcc.equalsIgnoreCase("active")) {
						if ((secc.isEmpty() || secc.equalsIgnoreCase("active")) && !end.isEqual(opend)) {
							List<AffectedColumn> affectedColumns = new ArrayList<>();
							affectedColumns.add(new AffectedColumn("exposure end coherent with status", 1,
									new ArrayList<>(Arrays.asList(row))));
							exposure_coherent_status
									.add(new ControlResult("Exposure end coherent with status ", affectedColumns));
							errorsCount.add(1);
						}
					} else if (sbcc.equalsIgnoreCase("claimant")) {
						if ((secc.isEmpty() || secc.equalsIgnoreCase("active") || secc.equalsIgnoreCase("claimant"))
								&& !end.isEqual(opend)) {
							List<AffectedColumn> affectedColumns = new ArrayList<>();
							affectedColumns.add(new AffectedColumn("exposure end coherent with status", 1,
									new ArrayList<>(Arrays.asList(row))));
							exposure_coherent_status
									.add(new ControlResult("Exposure end coherent with status ", affectedColumns));
							errorsCount.add(1);
						}
					}

				}

			}
		}
	}

}
