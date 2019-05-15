package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;
import org.apache.spark.util.LongAccumulator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Control40 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control40(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        if(headers.indexOf("date_of_last_medical_selection")>-1 && headers.indexOf("date_of_commencement")>-1) {
            Boolean control = dateOfLastMedicalSelectionControl(
                    row[headers.indexOf("date_of_last_medical_selection")],
                    row[headers.indexOf("date_of_commencement")]);
            if (control != null && control) {
                List<AffectedColumn> affectedColumns = new ArrayList<>();
                affectedColumns.add(new AffectedColumn("After", 1, new ArrayList<>(Arrays.asList(String.join(";",row)))));
                controlResult.add(new ControlResult(
                        "Consistent date of last medical selection", affectedColumns));
            } else if( control != null && !control){
                List<AffectedColumn> affectedColumns = new ArrayList<>();
                affectedColumns.add(new AffectedColumn("Before", 1, new ArrayList<>(Arrays.asList(String.join(";",row)))));
                controlResult.add(new ControlResult(
                        "Consistent date of last medical selection", affectedColumns));
            }
        }
    }

    private Boolean dateOfLastMedicalSelectionControl(String dateOfLastMedicalSelectionString, String dateOfCommencementString) {
        try {
            LocalDate dateOfLastMedicalSelection = LocalDate.parse(dateOfLastMedicalSelectionString,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
            LocalDate dateOfCommencement = LocalDate.parse(dateOfCommencementString,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
            if(dateOfLastMedicalSelection.isAfter(dateOfCommencement)) {
                return true;
            }
            if(dateOfLastMedicalSelection.isBefore(dateOfCommencement)) {
                return false;
            }
            return null;
        } catch (Exception e) {
            return null;
        }


    }

    public static ControlResult collectdateOfLastMedicalSelectionControlResult(
            ControlResultAccumulator consistentDateOfLastMedicalSelectionAccumulator) {
        AffectedColumn affectedColumnAfter = null;
        AffectedColumn affectedColumnBefore = null;
        for (AffectedColumn affectedColumn : consistentDateOfLastMedicalSelectionAccumulator.value()
                .getAffectedColumns()) {
            if (affectedColumn.getName().equals("After")) {
                affectedColumn.setName("Consistent date of last medical selection");
                affectedColumnAfter = affectedColumn;
            } else if (affectedColumn.getName().equals("Before")) {
                affectedColumn.setName("Consistent date of last medical selection");
                affectedColumnBefore = affectedColumn;
            }
        }
        if (affectedColumnAfter == null || affectedColumnBefore == null) {
            return null;
        }
        if (affectedColumnAfter.getErrorsNumber() - affectedColumnBefore.getErrorsNumber() >= 0) {
            return new ControlResult("Consistent date of last medical selection", Arrays.asList(affectedColumnBefore));
        } else {
            return new ControlResult("Consistent date of last medical selection", Arrays.asList(affectedColumnAfter));
        }
    }

    public static ControlResult collectdateOfLastMedicalSelectionControlResult2(
            ControlResultAccumulatorV2 acc) {
        ControlResult control = new ControlResult();
        for (ControlResult cntrl : acc.value().getControlResults() ) {
            if(cntrl.getControl().equalsIgnoreCase("Consistent date of last medical selection")){
                control = cntrl;
                break;
            }
        }


        AffectedColumn affectedColumnAfter = null;
        AffectedColumn affectedColumnBefore = null;
        for (AffectedColumn affectedColumn : control
                .getAffectedColumns()) {
            if (affectedColumn.getName().equals("After")) {
                affectedColumn.setName("Consistent date of last medical selection");
                affectedColumnAfter = affectedColumn;
            } else if (affectedColumn.getName().equals("Before")) {
                affectedColumn.setName("Consistent date of last medical selection");
                affectedColumnBefore = affectedColumn;
            }
        }
        if (affectedColumnAfter == null || affectedColumnBefore == null) {
            return null;
        }
        if (affectedColumnAfter.getErrorsNumber() - affectedColumnBefore.getErrorsNumber() >= 0) {
            return new ControlResult("Consistent date of last medical selection", Arrays.asList(affectedColumnBefore));
        } else {
            return new ControlResult("Consistent date of last medical selection", Arrays.asList(affectedColumnAfter));
        }
    }
}
