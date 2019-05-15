package com.scor.dataProcessing.dataChecker.functionalChecker.Operations.studyOperations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.StreamSupport;

import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.util.LongAccumulator;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;

import scala.Tuple2;

public class ControlByGroupLifeId implements VoidFunction<Tuple2<String, Iterable<String[]>>> {

	/**
	* 
	*/
	private static final long serialVersionUID = -7404298510082491122L;

	private ControlResultAccumulator claims_acc;
	private LongAccumulator errorsCount;
	private List<String> names;
	private Map<String, List<String>> incidenceMap;

	public ControlByGroupLifeId(ControlResultAccumulator claims_acc, LongAccumulator errorsCount, List<String> names, Map<String, List<String>> map) {
		super();
		this.claims_acc = claims_acc;
		this.errorsCount = errorsCount;
		this.names = names;
		this.incidenceMap = map;
	}

	private ControlResult constructControlResult(String[] line, String life_id) {
		String ex = "";
		for (String name : names) {
			switch (name) {
			case "type_of_event":
				ex+= line[0] + ";";
				break;
			case "acceleration_risk_type":
				ex+= line[5] + ";";
				break;
			case "main_risk_type":
				ex+= line[4] + ";";
				break;
			case "life_id":
				ex+= life_id + ";";
				break;
			case "data_line":
				ex+= line[6] + ";";
				break;
			case "policy_id":
				ex+= line[7] + ";";
				break;

			default:
				ex += ";";
				break;
			}
		}
		List<AffectedColumn> affectedColumns = new ArrayList<>();
		affectedColumns
				.add(new AffectedColumn("claims existence", 1, new ArrayList<>(Arrays.asList(ex))));
		ControlResult controlResult = new ControlResult("Claims existence", affectedColumns);
		return controlResult;
	}
	
	@Override
	public void call(Tuple2<String, Iterable<String[]>> rows) throws Exception {
		String lifeId = rows._1;
		long count = StreamSupport.stream(rows._2.spliterator(), false).count();
		if(count<2) {
			return;
		}
		String typeOfEventOfEvent = null;
		String mainRiskTypeOfEvent = null;
		String accelerationRiskTypeOfEvent = null;
		LocalDate dateOfEventIncurredOfEvent = null;
		String[] lineOfEvent = null;
		for (String[] row : rows._2) {
			String toe = row[0].toLowerCase();
			if(!toe.equals("death") && !toe.equals("incidence")) {
				continue;
			}
			typeOfEventOfEvent = toe;
			mainRiskTypeOfEvent = row[4].toLowerCase();
			accelerationRiskTypeOfEvent = row[5].toLowerCase();
			dateOfEventIncurredOfEvent = converteDate(row[1]);
			lineOfEvent = row;
			break;
		}
		if(lineOfEvent == null) {
			return;
		}
		for (String[] row : rows._2) {
			if(row.equals(lineOfEvent)) {
				continue;
			}
			String mainRiskType = row[4].toLowerCase();
			String accelerationRiskType = row[5].toLowerCase();
			if(typeOfEventOfEvent.equals("death")) {
				if (!mainRiskType.equals("life")
						|| !Arrays.asList("", "ci", "tpd", "ltc").contains(accelerationRiskType)) {
					continue;
				}
			} else if(typeOfEventOfEvent.equals("incidence")) {
				String riskOfEvent = mainRiskTypeOfEvent + accelerationRiskTypeOfEvent;
				String risk = mainRiskType + accelerationRiskType;
				if(incidenceMap.get(riskOfEvent) == null || incidenceMap.get(riskOfEvent).isEmpty() || !incidenceMap.get(riskOfEvent).contains(risk)) {
					continue;
				}
			}
			LocalDate dateOfBegin = converteDate(row[2]);
			LocalDate dateOfEnd = converteDate(row[3]);
			if(dateOfEventIncurredOfEvent == null ) {
				continue;
			}
			if(dateOfBegin != null && dateOfBegin.isAfter(dateOfEventIncurredOfEvent)) {
				continue;
			}
			if(dateOfEnd != null && dateOfEnd.isBefore(dateOfEventIncurredOfEvent)) {
				continue;
			}
			String typeOfEvent = row[0].toLowerCase();
			LocalDate dateOfEventIncurred = converteDate(row[1]);
			if(typeOfEventOfEvent.equals(typeOfEvent) && dateOfEventIncurredOfEvent.equals(dateOfEventIncurred)) {
				continue;
			}
			ControlResult controlResult = constructControlResult(row, lifeId);
			claims_acc.add(controlResult);
			errorsCount.add(1);
		}
	}

	private LocalDate converteDate(String stringDate) {
		try {
			LocalDate date = LocalDate.parse(stringDate,
					DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
			return date;
		} catch (Exception e) {
			return null;
		}
	}

}
