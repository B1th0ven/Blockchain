package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Control43 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control43(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        if(headers.indexOf(Headers.STATUS_END_CURRENT_CONDITION) > -1) {

            CheckincidenceDeathXORIncidence_Death(row,String.join(";",row), headers,controlResult); // filling the HashMap
        }
    }

    private void CheckincidenceDeathXORIncidence_Death(String[] row, String rowstr, List<String> header,ControlResultAccumulatorV2 result) {


        String typeOfevent = row[header.indexOf(Headers.TYPE_OF_EVENT)].trim().toLowerCase();
        String statusEndCurrentCondition = row[header.indexOf(Headers.STATUS_END_CURRENT_CONDITION)].trim().toLowerCase();
        List<AffectedColumn> affectedColumns = new ArrayList<>();
        List<AffectedColumn> affectedColumns2 = new ArrayList<>();

        affectedColumns.add(new AffectedColumn(typeOfevent,1,new ArrayList<>(Arrays.asList(rowstr))));
        result.add(new ControlResult("Incidence_Death_Check", affectedColumns));
        affectedColumns2.add(new AffectedColumn(statusEndCurrentCondition,1,new ArrayList<>(Arrays.asList(rowstr))));
        result.add(new ControlResult("Claimant_Dead_Check", affectedColumns2));


    }

    public static ControlResult incidenceDeathXORIncidence_DeathResult(ControlResultAccumulator result, ControlResultAccumulator result2, Long errorCount){
        ControlResult incValues = result.value();
        ControlResult claiValues = result2.value();
        List<AffectedColumn> affectedColumns = new ArrayList<>();



        if(incValues.getAffectedColumns() != null && incValues.getAffectedColumns().size() > 1) {
            List<String> incNames = incValues.getAffectedColumns().stream().map(s -> s.getName()).collect(Collectors.toList());
            if(incNames.contains("incidence_death") && (incNames.contains("death") || incNames.contains("incidence"))) {
                AffectedColumn incDea = incValues.getAffectedColumns().stream().filter(s -> s.getName().equalsIgnoreCase("incidence_death")).collect(Collectors.toList()).get(0);
                affectedColumns.add(new AffectedColumn(
                        Headers.TYPE_OF_EVENT+"/"+Headers.TYPE_OF_EVENT,incDea.getErrorsNumber(),
                        incDea.getExamples()));
                errorCount += incDea.getErrorsNumber();
            }
        }

        if(claiValues.getAffectedColumns() != null && claiValues.getAffectedColumns().size() > 1) {
            List<String> incNames = claiValues.getAffectedColumns().stream().map(s -> s.getName()).collect(Collectors.toList());
            if(incNames.contains("claimant_dead") && (incNames.contains("dead") || incNames.contains("claimant"))) {
                AffectedColumn claiDead = claiValues.getAffectedColumns().stream().filter(s -> s.getName().equalsIgnoreCase("claimant_dead")).collect(Collectors.toList()).get(0);
                affectedColumns.add(new AffectedColumn(
                        Headers.STATUS_END_CURRENT_CONDITION+"/"+Headers.STATUS_END_CURRENT_CONDITION,claiDead.getErrorsNumber(),
                        claiDead.getExamples()));
                errorCount += claiDead.getErrorsNumber();

            }
        }

        if(!affectedColumns.isEmpty()) {
            return new ControlResult("Incidence_Death XOR Incidence/Death", affectedColumns);
        }

        return new ControlResult();

    }

    public static ControlResult incidenceDeathXORIncidence_DeathResult2(ControlResultAccumulatorV2 result, Long errorCount){
        ControlResult incValues = new ControlResult();
        ControlResult claiValues = new ControlResult();
        int found = 0 ;
        for(ControlResult cntrl : result.value().getControlResults()){
            if(cntrl.getControl().equalsIgnoreCase("Incidence_Death_Check")){
                incValues = cntrl;
                found ++ ;
            }
            if(cntrl.getControl().equalsIgnoreCase("Claimant_Dead_Check")) {
                claiValues = cntrl;
                found++ ;
            }
            if(found == 2) break ;
        }

        List<AffectedColumn> affectedColumns = new ArrayList<>();



        if(incValues.getAffectedColumns() != null && incValues.getAffectedColumns().size() > 1) {
            List<String> incNames = incValues.getAffectedColumns().stream().map(s -> s.getName()).collect(Collectors.toList());
            if(incNames.contains("incidence_death") && (incNames.contains("death") || incNames.contains("incidence"))) {
                AffectedColumn incDea = incValues.getAffectedColumns().stream().filter(s -> s.getName().equalsIgnoreCase("incidence_death")).collect(Collectors.toList()).get(0);
                affectedColumns.add(new AffectedColumn(
                        Headers.TYPE_OF_EVENT+"/"+Headers.TYPE_OF_EVENT,incDea.getErrorsNumber(),
                        incDea.getExamples()));
                errorCount += incDea.getErrorsNumber();
            }
        }

        if(claiValues.getAffectedColumns() != null && claiValues.getAffectedColumns().size() > 1) {
            List<String> incNames = claiValues.getAffectedColumns().stream().map(s -> s.getName()).collect(Collectors.toList());
            if(incNames.contains("claimant_dead") && (incNames.contains("dead") || incNames.contains("claimant"))) {
                AffectedColumn claiDead = claiValues.getAffectedColumns().stream().filter(s -> s.getName().equalsIgnoreCase("claimant_dead")).collect(Collectors.toList()).get(0);
                affectedColumns.add(new AffectedColumn(
                        Headers.STATUS_END_CURRENT_CONDITION+"/"+Headers.STATUS_END_CURRENT_CONDITION,claiDead.getErrorsNumber(),
                        claiDead.getExamples()));
                errorCount += claiDead.getErrorsNumber();

            }
        }

        if(!affectedColumns.isEmpty()) {
            return new ControlResult("Incidence_Death XOR Incidence/Death", affectedColumns);
        }

        return new ControlResult();

    }


}
