package com.scor.dataProcessing.ibrn;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.persistance.entities.StudyEntity;
import com.scor.persistance.services.DimensionsService;
import com.scor.persistance.services.RefDataService;
import com.scor.persistance.services.StudyService;

import scala.Tuple2;

@Service
public class IbnrService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7284861786358610207L;

	@Autowired
	DimensionsService dimensionsService;

	@Autowired
	StudyService studyService;

	@Autowired
	RefDataService refDataService;

	Logger LOGGER = Logger.getLogger(IbnrService.class);

	public List<String> compoloryCheck(String filePath, String type) throws Exception {

		List<String> headerColumns = loadFileHeaderLowerCase(filePath);
		List<IbnrPivotEntity> pivots = IbnrPivotLoader.getByName(type);

		return compUnicityColumnsCheck(headerColumns, pivots);
	}

	public List<String> techControls(String filePath, String type) throws Exception {
		Stream<String> lines = readFileContent(filePath);
		List<String> header = loadFileHeader(filePath);
		List<IbnrPivotEntity> pivots = IbnrPivotLoader.getByName(type);
		Map<String, List<String>> refData = refDataService.getAll();

		List<String> errorReport = new ArrayList<String>();

		List<IbnrPivotEntity> headerPivots = new ArrayList<IbnrPivotEntity>();
		AtomicInteger lineCounter = new AtomicInteger();
		lines.forEach((String line) -> {
			lineCounter.getAndIncrement();
			List<String> values = Arrays.asList(line.split(";", -1));
			int i = 0;
			for (String head : header) {
				if (header.size() != values.size()) {
					errorReport.add("Header size does not match line values size");
					continue;
				}
				Optional<IbnrPivotEntity> pivot = pivots.stream()
						.filter(p -> p.getName().toLowerCase().equals(head.toLowerCase())).findFirst();

				// Check if field is empty
				if (values.get(i).isEmpty() && pivot.isPresent() && pivot.get().isRequired()) {
					errorReport.add("Value of " + head + " is empty in line " + lineCounter);
				} else if (pivot.isPresent()) {
					// REGEX VALUE MATCHING
					if(pivot.get().getType().toLowerCase().equals("code")) {
						if (!pivot.get().getRegex().isEmpty()) {
							if ((StringUtils.isBlank(values.get(i)) && !pivot.get().isRequired()) || values.get(i).toLowerCase().trim().matches(pivot.get().getRegex())) {
								// System.out.println(values.get(i) + " matches " + pivot.get().getRegex());
							} else {
								errorReport.add(values.get(i) + " does not match " + pivot.get().getRegex());
							}
						}
					} else {
						if (pivot.get().getType().toLowerCase().equals("numeric+")) {
							// SEE IF NUMBER TYPE FIELDS ARE POSITIVE
							if(pivot.get().getRegex().contains("integer")) {
								try {
									int value = Integer.parseInt(values.get(i));
									if(value<0) {
										throw new Exception();
									}
									if(pivot.get().getRegex().contains("not null")) {
										if(value == 0 ) {
											throw new Exception();
										}
									}
								} catch (NumberFormatException e) {
									errorReport.add(values.get(i) + " is not a numeric value in line " + lineCounter);
								} catch (Exception e) {
									errorReport.add(values.get(i) + " is not a positive value in line " + lineCounter);
								}
								continue;
							}
							try {
								float numericValue = Float.parseFloat(String.join(".", values.get(i).split(",")));
								if (numericValue < 0) {
									errorReport.add(values.get(i) + " is not a positive value in line " + lineCounter);
								}
							} catch (Exception e) {
								errorReport.add(values.get(i) + " is not a numeric value in line " + lineCounter);
							}
						} else if (pivot.get().getType().toLowerCase().equals("numeric")) {
							// SEE IF NUMBER TYPE FIELDS ARE POSITIVE
							try {
								float numericValue = Float.parseFloat(String.join(".", values.get(i).split(",")));
							} catch (Exception e) {
								errorReport.add(values.get(i) + " is not a numeric value in line " + lineCounter);
							}
//						} else if (pivot.get().getType().toLowerCase().equals("ref_treaty")) {
//							if (!refData.get("treaty_number_omega").contains(values.get(i).trim().toLowerCase()))
//								errorReport.add(values.get(i) + " does not exist in treaty referential");
//
//						} else if (pivot.get().getType().toLowerCase().equals("ref_portfolio")) {
//							if (!refData.get("portfolio_origin").contains(values.get(i).trim().toLowerCase()))
//								errorReport.add(values.get(i) + " does not exist in portfolio origin referential");
						} else if(pivot.get().getType().toLowerCase().equals("refdata")) {
							if (!refData.get(pivot.get().getName().toLowerCase()).contains(values.get(i).trim().toLowerCase()))
								errorReport.add(values.get(i) + " does not exist in "+pivot.get().getName()+" referential");
						}
					}
				} else {
					// System.out.println(values.get(i) + " does not have a pivot. Head = "+ head);
				}

				i++;
			}
		});

		return errorReport;
	}

	private List<String> loadFileHeader(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1);
		return Arrays.asList(lines.findFirst().get().split(";"));
	}

	private List<String> loadFileHeaderLowerCase(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1);
		String[] lineArray = lines.findFirst().get().split(";");
		List<String> headers = new ArrayList<>();
		for (String line : lineArray) {
			headers.add(line.toLowerCase());
		}
		return headers;
	}

	private Stream<String> readFileContent(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1);
		return lines.skip(1);
	}

	private List<String> possibleValuesCheck() {

		return null;
	}

	private List<String> compUnicityColumnsCheck(List<String> headerColumns, List<IbnrPivotEntity> pivots) {
		// Unicity Check
		int unicity = 0;
		List<String> errorReport = new ArrayList<String>();
		List<IbnrPivotEntity> uniqueHeaders = new ArrayList<IbnrPivotEntity>();
		List<IbnrPivotEntity> requiredHeaders = new ArrayList<IbnrPivotEntity>();

		requiredHeaders = pivots.stream().filter(p -> p.isRequired() && !p.isUnique()).collect(Collectors.toList());
		uniqueHeaders = pivots.stream().filter(p -> p.isUnique() && p.getUnicityId() > 0).collect(Collectors.toList());

		for (IbnrPivotEntity pivot : requiredHeaders) {
			int ind = headerColumns.indexOf(pivot.getName());
			if (ind < 0)
				errorReport.add(pivot.getName() + " is required but missing.");
		}

		Set<Integer> unicityIds = new HashSet<Integer>();

		for (IbnrPivotEntity pivot : uniqueHeaders) {
			unicityIds.add(pivot.getUnicityId());
		}

		for (Integer id : unicityIds) {
			List<String> unicityCounter = new ArrayList<String>();
			List<IbnrPivotEntity> exclusivePivots = uniqueHeaders.stream().filter(p -> p.getUnicityId() == id)
					.collect(Collectors.toList());
			for (IbnrPivotEntity exlPivot : exclusivePivots) {
				if (headerColumns.contains(exlPivot.getName()))
					unicityCounter.add(exlPivot.getName());
			}

			if (unicityCounter.size() <= 0) {
				errorReport.add("Unique Mandatory Columns of id " + id + " missing");
			} else if (unicityCounter.size() > 1) {
				errorReport.add("More than one Unique Mandatory Columns of id " + id + " found " + unicityCounter);
			}
		}

		return errorReport;
	}

	private boolean isColumnUnique() {
		return false;
	}

	public List<String> functionalControls(String path, String type, List<String> runDimensions, int runStudyId)
			throws Exception {

		// Preparing for controls
		LOGGER.info("IBNR Functional Control of " + type + " File : " + path);
		List<String> headerColumns = loadFileHeaderLowerCase(path);
		List<IbnrPivotEntity> pivots = IbnrPivotLoader.getByName(type);
		List<String> pivotNameList = new ArrayList<String>();
		pivots.forEach(pivot -> {
			pivotNameList.add(pivot.getName().toLowerCase());
		});

		// Blocking Headers Controls
		List<String> headerControlError = new ArrayList<>();
		try {
			headerControlError = headersBlockingControl(headerColumns, pivotNameList);
		} catch (Exception e) {
			String error = e.getMessage();
			headerControlError.add(error + " are not defined in Pivot");
			return headerControlError;
		}

		// Headers Controls
		headerControlError = headersControls(headerColumns, pivotNameList, runDimensions);

		// Lines Controls
		Stream<String> lines = readFileContent(path);
		List<String> controlError = new ArrayList<>();
		if (!type.trim().equals("allocation")) {
			controlError = functionalLinesControls(headerColumns, lines, pivotNameList, runStudyId);
		}
		Stream<String> lines2 = readFileContent(path);
		String controlNumeric = functionalNumericControls(headerColumns, lines2, pivots);

		Stream<String> lines3 = readFileContent(path);
		Integer dupLines = CheckDuplicatedLines(lines3, headerColumns);
		if( dupLines > 0)
		controlError.add(dupLines+" duplicated lines found");
		
		// Merging Results
		if (headerControlError != null && !headerControlError.isEmpty()) {
			controlError.addAll(headerControlError);
		}
		if (controlNumeric != null) {
			controlError.add(controlNumeric);
		}
		if (type.trim().equals("udf")) {
			Stream<String> lines4 = readFileContent(path);
			String missingValuesCheck = missingValuesCheck(lines4, headerColumns, "result_metric");
			if (StringUtils.isNotBlank(missingValuesCheck)) {
				controlError.add(missingValuesCheck);
			}
		}
		
		return controlError;

	}

	private String missingValuesCheck(Stream<String> lines2, List<String> headerColumns, String... variables) {
		List<String> missingVariable = new ArrayList<String>();
		for (String variable : variables) {
			int index = headerColumns.indexOf(variable);
			if (index < 0) {
				continue;
			}
			long missingCount = lines2.filter(line -> StringUtils.isBlank(line.split(";", -1)[index])).count();
			if (missingCount == 0) {
				continue;
			}
			missingVariable.add(variable);
		}
		if (missingVariable.isEmpty()) {
			return null;
		}
		String message = "Certain variables must not have missing values: ";
		for (String variable : missingVariable) {
			message += variable + ", ";
		}
		return message.substring(0, message.length() - 2) + ".";
	}

	private List<String> headersControls(List<String> headerColumns, List<String> pivots, List<String> runDimensions) {
		List<String> headerControlErrors = new ArrayList<>();
		List<String> dimensions = dimensionsService.getAll();
		String missingDimensions = "";
		Map<String, Integer> headerCount = new HashMap();
		for (String header : headerColumns) {
			if (dimensions.contains(header) && !runDimensions.contains(header)) {
				missingDimensions += "," + header;
			}
			Integer nb = headerCount.get(header);
			if(nb == null) headerCount.put(header, 1);
			else headerCount.put(header, nb+1);
		}
		if (StringUtils.isNotBlank(missingDimensions)) {
			headerControlErrors.add("All dimensions in table must be included in data. Missing dimensions : "
					+ missingDimensions.replaceFirst(",", ""));
		}
		List<String> uniqueHeader = new ArrayList<String>();
		headerCount.keySet().forEach(key -> {
			Integer nb = headerCount.get(key);
			if(nb>1) {
				uniqueHeader.add(key);
			}
		});
		if(!uniqueHeader.isEmpty()) {
			String uniqueHeaderMessage = "A column name must only appear once in a table : ";
			for (String header : uniqueHeader) {
				uniqueHeaderMessage += header + ", ";
			}
			headerControlErrors.add(uniqueHeaderMessage.substring(0,uniqueHeaderMessage.length() - 2));
		}
		return headerControlErrors;
	}

	private List<String> headersBlockingControl(List<String> headerColumns, List<String> pivotNameList)
			throws Exception {
		List<String> headerControlError = new ArrayList<>();
		String headerOutsidePivot = "";
		for (String header : headerColumns) {
			if (!pivotNameList.contains(header)) {
				headerOutsidePivot += ", " + header;
			}
		}
		if (StringUtils.isNotBlank(headerOutsidePivot)) {
			throw new Exception(headerOutsidePivot.replaceFirst(",", ""));
		}

		return headerControlError;
	}

	private List<String> functionalLinesControls(List<String> headerColumns, Stream<String> lines,
			List<String> pivotNameList, int studyId) {
		List<String> errorList = new ArrayList<String>();

		// Sorting lines with date metric
		String calendarMetric = fetchingCalendarMetric(headerColumns);
		if (StringUtils.isBlank(calendarMetric)) {
			errorList.add("No IBNR Factors to verify observation periode coverage");
			return errorList;
		}
		List<String> listCalendar = sortingDates(headerColumns, lines, calendarMetric);

		// Collecting Dates of Observation
		StudyEntity study = studyService.getStudy(studyId);
		Date startDateObservation = study.getStStartObservationDate();
		Date endDateObservarion = study.getStEndObservationDate();

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(startDateObservation);
		int yearStart = calStart.get(Calendar.YEAR);
		int monthStart = calStart.get(Calendar.MONTH) + 1;

		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(endDateObservarion);
		int yearEnd = calEnd.get(Calendar.YEAR);
		int monthEnd = calEnd.get(Calendar.MONTH) + 1;

		// verify that IBNR factors must cover entire study observation period
		switch (calendarMetric) {
		case "year":
			if (!listCalendar.contains(String.valueOf(yearStart)) || !listCalendar.contains(String.valueOf(yearEnd))) {
				errorList.add("IBNR factors must cover entire study observation period");
				break;
			}
			for (String line : listCalendar) {
				int year = Integer.parseInt(line);
				if (year < yearStart) {
					continue;
				}
				if (year == yearEnd) {
					break;
				}
				if (year == yearStart) {
					yearStart++;
					continue;
				}

				errorList.add("IBNR factors must cover entire study observation period");
				break;
			}
			break;
		case "half_year":
			int startHalf = Arrays.asList(1, 2, 3, 4, 5, 6).contains(monthStart) ? 1 : 2;
			int endHalf = Arrays.asList(1, 2, 3, 4, 5, 6).contains(monthEnd) ? 1 : 2;
			if (!listCalendar.contains(yearStart + "H" + startHalf)
					|| !listCalendar.contains(yearEnd + "H" + endHalf)) {
				errorList.add("IBNR factors must cover entire study observation period");
				break;
			}
			for (String line : listCalendar) {
				int year = Integer.parseInt(line.substring(0, 4));
				int half = Integer.parseInt(line.substring(5));
				if (year < yearStart || (year == yearStart && half < startHalf)) {
					continue;
				}
				if (year == yearEnd && half == endHalf) {
					break;
				}
				if (year == yearStart && half == startHalf) {
					if (startHalf == 1) {
						startHalf = 2;
						continue;
					}
					yearStart++;
					startHalf = 1;
					continue;
				}
				errorList.add("IBNR factors must cover entire study observation period");
				break;
			}
			break;
		case "quarter":
			int startQuarter = Arrays.asList(1, 2, 3).contains(monthStart) ? 1
					: Arrays.asList(4, 5, 6).contains(monthStart) ? 2
							: Arrays.asList(7, 8, 9).contains(monthStart) ? 3 : 4;
			int endQuarter = Arrays.asList(1, 2, 3).contains(monthEnd) ? 1
					: Arrays.asList(4, 5, 6).contains(monthEnd) ? 2 : Arrays.asList(7, 8, 9).contains(monthEnd) ? 3 : 4;
			if (!listCalendar.contains(yearStart + "Q" + startQuarter)
					|| !listCalendar.contains(yearEnd + "Q" + endQuarter)) {
				errorList.add("IBNR factors must cover entire study observation period");
				break;
			}
			for (String line : listCalendar) {
				int year = Integer.parseInt(line.substring(0, 4));
				int quarter = Integer.parseInt(line.substring(5));
				if (year < yearStart || (year == yearStart && quarter < startQuarter)) {
					continue;
				}
				if (year == yearEnd && quarter == endQuarter) {
					break;
				}
				if (year == yearStart && quarter == startQuarter) {
					if (startQuarter == 4) {
						startQuarter = 1;
						yearStart++;
						continue;
					}
					startQuarter++;
					continue;
				}
				errorList.add("IBNR factors must cover entire study observation period");
				break;
			}
			break;
		case "month":
			if (!listCalendar.contains(yearStart + "M" + (monthStart < 10 ? "0" + monthStart : monthStart))
					|| !listCalendar.contains(yearEnd + "M" + (monthEnd < 10 ? "0" + monthEnd : monthEnd))) {
				errorList.add("IBNR factors must cover entire study observation period");
				break;
			}
			for (String line : listCalendar) {
				int year = Integer.parseInt(line.substring(0, 4));
				int month = Integer.parseInt(line.substring(5));
				if (year < yearStart || (year == yearStart && month < monthStart)) {
					continue;
				}
				if (year == yearEnd && month == monthEnd) {
					break;
				}
				if (year == yearStart && month == monthStart) {
					if (month == 12) {
						yearStart++;
						monthStart = 1;
						continue;
					}
					monthStart++;
					continue;
				}
				errorList.add("IBNR factors must cover entire study observation period");
				break;
			}

			break;
		default:
			break;
		}

		return errorList;
	}

	private List<String> sortingDates(List<String> headerColumns, Stream<String> lines, String calendarMetric) {
		List<String> listCalendar = new ArrayList<>();
		Stream<String> columnCalendarMetricsStream = lines
				.map(line -> line.split(";")[headerColumns.indexOf(calendarMetric)]);
		if (calendarMetric.equals("month")) {
			columnCalendarMetricsStream = columnCalendarMetricsStream
					.map(line -> line.trim().length() == 6 ? line.replace("M", "M0") : line);
		}
		listCalendar = columnCalendarMetricsStream.distinct().sorted().collect(Collectors.toList());
		return listCalendar;
	}

	private String fetchingCalendarMetric(List<String> headerColumns) {
		for (String header : headerColumns) {
			if (header.toLowerCase().equalsIgnoreCase("year") || header.toLowerCase().equalsIgnoreCase("half_year")
					|| header.toLowerCase().equalsIgnoreCase("quarter")
					|| header.toLowerCase().equalsIgnoreCase("month")) {
				return header.toLowerCase();
			}
		}
		return "";
	}

	public List<String> amountAllocationControl(String amountPath, String allocationPath) throws Exception {
		LOGGER.info("Amount Allocation Check for combinations in tow files : Amount File : " + amountPath
				+ " Allocation File : " + allocationPath);
		List<String> errorReport = new ArrayList<>();

		Stream<String> linesAmount = readFileContent(amountPath);
		List<String> headerAmount = loadFileHeaderLowerCase(amountPath);

		Stream<String> linesAllocation = readFileContent(allocationPath);
		List<String> headerAllocation = loadFileHeaderLowerCase(allocationPath);

		if (!headerAmount.contains("portfolio_origin") || !headerAmount.contains("bucket_id")
				|| !headerAmount.contains("decrement")) {
			errorReport.add(
					"Missing column in Amount File to check combination of Portfolio_Origin, Bucket_ID et Decrements");
			return errorReport;
		}
		List<String> linesAllocationList = linesAllocation
				.map(line -> line.split(";")[headerAllocation.indexOf("portfolio_origin")].trim() + ";"
						+ line.split(";")[headerAllocation.indexOf("bucket_id")].trim() + ";"
						+ line.split(";")[headerAllocation.indexOf("decrement")].trim())
				.distinct().sorted().collect(Collectors.toList());
		List<String> linesAmountList = linesAmount
				.map(line -> line.split(";")[headerAmount.indexOf("portfolio_origin")].trim() + ";"
						+ line.split(";")[headerAmount.indexOf("bucket_id")].trim() + ";"
						+ line.split(";")[headerAmount.indexOf("decrement")].trim())
				.distinct().sorted().collect(Collectors.toList());

		long combinationNotFound = linesAmountList.stream().filter(line -> !linesAllocationList.contains(line)).count();
		if (combinationNotFound > 0) {
			errorReport.add(
					"All combinations of (Portfolio Origin & Bucket ID & Decrement) contained in IBNR Amounts table must be present in IBNR Amount Allocation table");
		}
		return errorReport;

	}

	private String functionalNumericControls(List<String> headerColumns, Stream<String> lines,
			List<IbnrPivotEntity> pivots) {
		List<String> headersNumeric = new ArrayList<>();
		for (IbnrPivotEntity p : pivots) {
			if (headerColumns.contains(p.getName().trim().toLowerCase())) {
				if (p.getType().toLowerCase().trim().equals("numeric")
						|| p.getType().toLowerCase().trim().equals("numeric+")) {
					headersNumeric.add(p.getName().trim().toLowerCase());
				}
			}
		}
		if (headersNumeric.isEmpty()) {
			return null;
		}
		Map<String, String> numericValues = new HashMap<>();
		List<String> errorHeader = new ArrayList<>();
		lines.forEach(line -> {
			String[] values = line.split(";");
			for (String header : headersNumeric) {
				String v = values[headerColumns.indexOf(header)];
				String valueToCompare = numericValues.get(header);
				if (valueToCompare == null && (v.contains(".") || v.contains(","))) {
					numericValues.put(header, v);
				} else if (valueToCompare != null) {
					if ((valueToCompare.contains(".") && v.contains(","))
							|| (valueToCompare.contains(",") && v.contains("."))) {
						errorHeader.add(header);
					}
				}
			}
			headersNumeric.removeAll(errorHeader);
		});
		if (errorHeader == null || errorHeader.isEmpty()) {
			return null;
		}
		String message = "";
		for (String header : errorHeader) {
			message = message + ", " + header;
		}

		return "Point and comma are not accepted in a signe column :" + message.replaceFirst(",", "");
	}

	private Integer CheckDuplicatedLines(Stream<String> linesStream,List<String> headerColumns){
		List<String> lines = linesStream.collect(Collectors.toList());
		HashMap<String,Integer> Duplicated = new HashMap<>();
		List<String> var = new ArrayList<>();
		int indexRate = headerColumns.indexOf("rate");
		for(String line : lines){
			String[] splittedLine = line.split(";");
			String key = "";
			Integer occ = 0;
			for(int i=0;i<splittedLine.length;i++){
				if(i != indexRate)
					key += "-"+splittedLine[i];
			}
			if(var.contains(key))
			{
				if(Duplicated.containsKey(key)) {
					occ = Duplicated.get(key);
					Duplicated.put(key, occ++);
				} else Duplicated.put(key,1);
			} else {
				var.add(key);
			}
		}
		List<String> res = new ArrayList<>();
		return Duplicated.size();

	}

}
