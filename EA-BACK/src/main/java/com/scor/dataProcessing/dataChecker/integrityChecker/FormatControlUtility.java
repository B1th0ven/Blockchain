package com.scor.dataProcessing.dataChecker.integrityChecker;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.util.LongAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;

import scala.Tuple2;

public class FormatControlUtility {

	public static void numericValidator(String number, String name, Boolean isMandatory, String line,
										Broadcast<Map<String, String>> decimalSeparator, ControlResultAccumulator nf_acc, LongAccumulator errorsCount,
										List<String> possibleValue, Boolean isPositif) {
		if (StringUtils.isBlank(number)) {
			if (isMandatory)
				invalideFieldCounter(name, nf_acc, errorsCount, line);
		} else {
			Double valueofNumber;
			try {
				valueofNumber = Double.parseDouble(number);
				if (number.contains(".") && (!decimalSeparatorValidator(name, decimalSeparator, "."))) {
					invalideFieldCounter(name, nf_acc, errorsCount, line);
					return;
				}
			} catch (NumberFormatException pointException) {
				try {
					valueofNumber = Double.parseDouble(number.replaceAll("\\,", "\\."));
					if (number.contains(",") && !decimalSeparatorValidator(name, decimalSeparator, ",")) {
						invalideFieldCounter(name, nf_acc, errorsCount, line);
						return;
					}
				} catch (NumberFormatException commaException) {
					invalideFieldCounter(name, nf_acc, errorsCount, line);
					return;
				}
			}
			if (isPositif && valueofNumber < 0) {
				invalideFieldCounter(name, nf_acc, errorsCount, line);
				return;
			}

			if (possibleValue != null) {
				if (possibleValue.contains("integer")) {
					try {
						valueofNumber = (double) Integer.parseInt(number);
					} catch (NumberFormatException e) {
						invalideFieldCounter(name, nf_acc, errorsCount, line);
						return;
					}
				}
				if (possibleValue.contains("not null") && valueofNumber == 0) {
					invalideFieldCounter(name, nf_acc, errorsCount, line);
					return;
				}
				if (possibleValue.contains("<=1") && valueofNumber > 1) {
					invalideFieldCounter(name, nf_acc, errorsCount, line);
					return;
				}
			}
		}

	}

	public static void dateValidator(String date, String name, Boolean isMandatory, String line,
									 ControlResultAccumulator df_acc, LongAccumulator errorsCount) {
		if (StringUtils.isBlank(date)) {
			if (isMandatory)
				invalideFieldCounter(name, df_acc, errorsCount, line);
		} else {
			try {

				LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
			} catch (DateTimeException e) {
				invalideFieldCounter(name, df_acc, errorsCount, line);
				e.printStackTrace();
			}
		}
	}

	public static void ageValidator(String age, String name, Boolean isMandatory, String line,
									ControlResultAccumulator nf_acc, LongAccumulator errorsCount) {

		if (StringUtils.isBlank(age)) {
			if (isMandatory)
				invalideFieldCounter(name, nf_acc, errorsCount, line);
		} else {
			try {
				Integer ageValue = Integer.parseInt(age);
				if (ageValue < 0 || ageValue > 150) {
					invalideFieldCounter(name, nf_acc, errorsCount, line);
				}
			} catch (NumberFormatException e) {
				invalideFieldCounter(name, nf_acc, errorsCount, line);
			}
		}

	}
	public static void codeValidator(String code, String name, Boolean isMandatory, String line,
									 ControlResultAccumulator cf_acc, LongAccumulator errorsCount, List<String> possibleValue) {
		if (StringUtils.isBlank(code)) {
			if (isMandatory && !possibleValue.contains(" "))
				invalideFieldCounter(name, cf_acc, errorsCount, line);
		} else {
			if (!(possibleValue.contains(code)) || code.equalsIgnoreCase("empty")) {
				invalideFieldCounter(name, cf_acc, errorsCount, line);
			}
		}
	}

	public static void invalideFieldCounter(String name, ControlResultAccumulator acc, LongAccumulator errorsCount,
											String line) {
		List<AffectedColumn> affectedColumns = new ArrayList<>();
		affectedColumns.add(new AffectedColumn(name, 1, new ArrayList<>(Arrays.asList(line))));
		acc.add(new ControlResult("", affectedColumns));
		errorsCount.add(1);

	}

	private static Boolean decimalSeparatorValidator(String name, Broadcast<Map<String, String>> decimalSeparator,
													 String separator) {

		if (separator.equals(decimalSeparator.value().get(name))) {
			return true;
		} else
			return false;

	}

	public static Map<String, String> getDecimalSeparator(JavaRDD<String> data, String header, List<String> types,
														  List<String> names) {
		JavaRDD<String> noheader = data.filter(line -> !line.equalsIgnoreCase(header));
		JavaPairRDD<String, String> dataToMap = noheader.flatMapToPair(line -> {
			List<Tuple2<String, String>> result = new ArrayList<Tuple2<String, String>>();
			String[] arr = line.toLowerCase().trim().split(";", -1);
			for (int i = 0; i < arr.length; i++) {
				if (types.get(i).equalsIgnoreCase("numeric") || types.get(i).equalsIgnoreCase("numeric+")) {
					Tuple2<String, String> maps = new Tuple2<String, String>(names.get(i), arr[i]);
					result.add(maps);
				}
			}
			return result.iterator();
		});

		JavaPairRDD<String, Iterable<String>> dataGroupedByHeader = dataToMap.groupByKey();
		JavaPairRDD<String, String> dataByheader = dataGroupedByHeader.mapValues(line -> {
			Iterator<String> itr = line.iterator();
			while (itr.hasNext()) {
				String element = itr.next();
				if (element.contains(","))
					return ",";
				if (element.contains("."))
					return ".";
			}
			return "";
		});

		return dataByheader.collectAsMap();
	}

}
