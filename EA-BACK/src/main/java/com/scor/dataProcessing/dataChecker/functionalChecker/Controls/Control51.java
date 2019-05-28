package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.util.LongAccumulator;

import com.clearspring.analytics.util.Lists;
import com.clearspring.analytics.util.Pair;
import com.scor.dataProcessing.Helpers.GroupingEntityFieldById;
import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.Helpers.SortByIDs;
import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.common.DataPivot;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.PivotCol;


public class Control51 implements Serializable {

	private static final long serialVersionUID = -7792291201979184187L;
	ControlResultAccumulator controlResult;
	LongAccumulator errorsCount;
	List<String> names;
	String type ; 
	List<String> entityFieldsName; 
	List<String> entityFieldsNameinFile ; 

	public Control51(ControlResultAccumulator controlResult, LongAccumulator errorsCount, List<String> names, String type ) {
		this.controlResult = controlResult;
		this.errorsCount = errorsCount;
		this.names = names;
		this.type=type ; 
	}

	public void validate(JavaRDD<String> data) {
		
		
		this.entityFieldsName = DataPivot.getPivotCols().stream().filter(p -> {
			return "life".equalsIgnoreCase(p.getEntity());
		}).map(PivotCol::getName).collect(Collectors.toList());
		
		this.entityFieldsNameinFile= names.stream().filter(ele-> entityFieldsName.contains(ele)).collect(Collectors.toList()) ; 
		if ( type.equalsIgnoreCase("split") ) {
			data=data.filter(line -> {
				String[] row = line.toLowerCase().split(";",-1);
				Integer exposureorEventindex= names.indexOf(Headers.EXPOSURE_OR_EVENT) ; 
				if (exposureorEventindex > -1 && (!row[exposureorEventindex].replaceAll(" ", "").equalsIgnoreCase("exposure+event") && !row[exposureorEventindex].trim().equalsIgnoreCase("exposure")) ) 
					return false ;
				return true; 
			}) ;
		}
		if (names.contains(Headers.LIFE_ID)) {
			String[] lifeIdsName = { Headers.LIFE_ID };
			
			data.mapToPair(new GroupingEntityFieldById(names, lifeIdsName, this.entityFieldsNameinFile)).groupByKey()
					.foreach(ele -> {
						String lifeId = ele._1;
						List<List<String>> lifeField = Lists.newArrayList(ele._2);
						List<List<String>> distinct= lifeField.stream().distinct().collect(Collectors.toList()) ; 
						Integer colNameIndex=null ; 
						if (distinct.size() > 1) {
							for (int i=0 ; i< distinct.get(0).size() ; i++) {
								if (!distinct.get(0).get(i).equalsIgnoreCase(distinct.get(1).get(i))) {
									colNameIndex=  i ; 
									break ; 
								}
							}
							List<AffectedColumn> affectedColumns = new ArrayList<>();
							affectedColumns.add(new AffectedColumn("Life"+"/"+this.entityFieldsNameinFile.get(colNameIndex), 1, new ArrayList<>(Arrays.asList(lifeId))));
							controlResult.add(new ControlResult("", affectedColumns));
							errorsCount.add(1);
						}
					});
		}
		this.entityFieldsName = DataPivot.getPivotCols().stream().filter(p -> {
			return "policy".equalsIgnoreCase(p.getEntity());
		}).map(PivotCol::getName).collect(Collectors.toList());
		this.entityFieldsNameinFile= names.stream().filter(ele-> entityFieldsName.contains(ele)).collect(Collectors.toList()) ; 
		if (names.contains(Headers.POLICY_ID)) {
			String[] policyIdsName = { Headers.POLICY_ID };
			data.mapToPair(new GroupingEntityFieldById(names, policyIdsName, this.entityFieldsNameinFile)).groupByKey()
					.foreach(ele -> {
						String policyID = ele._1;
						List<List<String>> policyField = Lists.newArrayList(ele._2);
						List<List<String>> distinct= policyField.stream().distinct().collect(Collectors.toList()) ; 
						Integer colNameIndex=null ; 
						if (distinct.size() > 1) {
							for (int i=0 ; i< distinct.get(0).size() ; i++) {
								if (!distinct.get(0).get(i).equalsIgnoreCase(distinct.get(1).get(i))) {
									colNameIndex=  i ; 
									break ; 
								}
							}
							List<AffectedColumn> affectedColumns = new ArrayList<>();
							affectedColumns
									.add(new AffectedColumn("Policy"+"/"+this.entityFieldsNameinFile.get(colNameIndex), 1, new ArrayList<>(Arrays.asList(policyID))));
							controlResult.add(new ControlResult("", affectedColumns));
							errorsCount.add(1);
						}
					});
		}
		this.entityFieldsName = DataPivot.getPivotCols().stream().filter(p -> {
			return "BENEFIT".equalsIgnoreCase(p.getEntity());
		}).map(PivotCol::getName).collect(Collectors.toList());
		this.entityFieldsNameinFile= names.stream().filter(ele-> entityFieldsName.contains(ele)).collect(Collectors.toList()) ; 
		if (names.containsAll(Arrays.asList(Headers.BENEFIT_ID,Headers.POLICY_ID,Headers.LIFE_ID))) {
			String[] benefitIdsName = { "benefit_id", "policy_id", "life_id" };
			data.mapToPair(new GroupingEntityFieldById(names, benefitIdsName, this.entityFieldsNameinFile)).groupByKey()
					.foreach(ele -> {
						String benefitCode = ele._1;
						List<List<String>> benefitField = Lists.newArrayList(ele._2);
						List<List<String>> distinct= benefitField.stream().distinct().collect(Collectors.toList()) ; 
						Integer colNameIndex=null ; 
						if (distinct.size() > 1) {
							for (int i=0 ; i< distinct.get(0).size() ; i++) {
								if (!distinct.get(0).get(i).equalsIgnoreCase(distinct.get(1).get(i))) {
									colNameIndex=  i ; 
									break ; 
								}
							}
							List<AffectedColumn> affectedColumns = new ArrayList<>();
							affectedColumns
									.add(new AffectedColumn("Benefit"+"/"+this.entityFieldsNameinFile.get(colNameIndex), 1, new ArrayList<>(Arrays.asList(benefitCode))));
							controlResult.add(new ControlResult("", affectedColumns));
							errorsCount.add(1);
						}
					});
		}

	}

}
