package com.scor.dataProcessing.Helpers;

import java.util.List;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class GroupingByLifeId implements PairFunction<String, String, String[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5923830263599304253L;

	private static int lifeIndex;
	private static int typeOfEventIndex;
	private static int dateOfEventIncurredindex;
	private static int dateOfBeginCurrentConditionIndex;
	private static int dateOfEndCurrentConditionIndex;
	private static int mainRiskTypeIndex;
	private static int accelerationRiskTypeIndex;
	private static int lineIndex;
    private static int policyIdIndex;

	public GroupingByLifeId(List<String> names) {
		this.lifeIndex = names.indexOf(Headers.LIFE_ID);
		this.typeOfEventIndex = names.indexOf(Headers.TYPE_OF_EVENT);
		this.dateOfEventIncurredindex = names.indexOf(Headers.DATE_OF_EVENT_INCURRED);
		this.dateOfBeginCurrentConditionIndex = names.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION);
		this.dateOfEndCurrentConditionIndex = names.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION);
		this.mainRiskTypeIndex = names.indexOf(Headers.MAIN_RISK_TYPE);
		this.accelerationRiskTypeIndex = names.indexOf(Headers.ACCELERATION_RISK_TYPE);
		this.lineIndex = names.indexOf("data_line");
        this.policyIdIndex = names.indexOf(Headers.POLICY_ID);
	}

	@Override
	public Tuple2<String, String[]> call(String row_str) throws Exception {
		String[] row = row_str.toLowerCase().trim().split(";", -1);

		String[] resultArray = new String[8];
		String id = row[lifeIndex];
		resultArray[0] = typeOfEventIndex > -1 ? row[typeOfEventIndex] : "";
		resultArray[1] = dateOfEventIncurredindex > -1 ? row[dateOfEventIncurredindex] : "";
		resultArray[2] = dateOfBeginCurrentConditionIndex > -1 ? row[dateOfBeginCurrentConditionIndex] : "";
		resultArray[3] = dateOfEndCurrentConditionIndex > -1 ? row[dateOfEndCurrentConditionIndex] : "";
		resultArray[4] = mainRiskTypeIndex > -1 ? row[mainRiskTypeIndex] : "";
		resultArray[5] = accelerationRiskTypeIndex > -1 ? row[accelerationRiskTypeIndex] : "";

		resultArray[6] = lineIndex > -1 ? row[lineIndex] : "";
		resultArray[7] = policyIdIndex > -1 ? row[policyIdIndex] : "";

		return new Tuple2<>(id, resultArray);
	}
}
