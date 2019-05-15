package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.util.LongAccumulator;

import java.util.*;

public class Control38 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;
    final List<String> headers;
    final HashMap<Integer, String> missingValuesCheckHeadersEntireFile;


    public Control38(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo, List<String> headers){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
        this.headers = headers;
        this.missingValuesCheckHeadersEntireFile = fillHeadersForEmptyCheckEntireFile(headers);


    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {

        List<AffectedColumn> affectedColumnsMissingValuesEntireFile = missingValuesCheckEntireFile(missingValuesCheckHeadersEntireFile,
                row, String.join(";",row));
        if (!affectedColumnsMissingValuesEntireFile.isEmpty()) {
            controlResult
                    .add(new ControlResult("Missing Values Check_2", affectedColumnsMissingValuesEntireFile));
        }
    }

   private static HashMap<Integer, String> fillHeadersForEmptyCheckEntireFile(List<String> names) {
        List<String> headersToCheck = Arrays.asList("date_of_birth", "date_of_commencement","age_at_commencement","date_of_claim_commencement");
        //List<String> headersToCheck = Arrays.asList("date_of_birth", "age_at_commencement", );
        HashMap<Integer, String> missingValuesHeadersToCheck = new HashMap<>();
        for (String header : headersToCheck) {
            if (!names.contains(header)) {
                continue;
            }
            missingValuesHeadersToCheck.put(names.indexOf(header), header);
        }

        return missingValuesHeadersToCheck;
    }




    private List<AffectedColumn> missingValuesCheckEntireFile(HashMap<Integer, String> missingValuesCheckHeadersEntireFile, String[] row_arr, String row) {
        List<AffectedColumn> affectedColumnss = new ArrayList<>();
        // update sprint 11 control 38
        missingValuesCheckHeadersEntireFile.entrySet().forEach(header -> {
                    if (header.getValue().equalsIgnoreCase("date_of_commencement") &&
                            (headers.contains("status_begin_current_condition")) &&
                            (row_arr[headers.indexOf("status_begin_current_condition")].trim().equalsIgnoreCase("Active"))) {

                        if (StringUtils.isBlank(row_arr[header.getKey()])) {
                            errorsCount.add(1);
                            affectedColumnss.add(
                                    new AffectedColumn(header.getValue() + "/" + header.getValue(), 1, new ArrayList<>(Arrays.asList(row))));
                        }
                    }

                    if (header.getValue().equalsIgnoreCase("date_of_Claim_Commencement") &&
                            (headers.contains("status_begin_current_condition")) &&
                            row_arr[headers.indexOf("status_begin_current_condition")].trim().equalsIgnoreCase("Claimant")) {

                        if (StringUtils.isBlank(row_arr[header.getKey()])) {
                            errorsCount.add(1);
                            affectedColumnss.add(
                                    new AffectedColumn(header.getValue() + "/" + header.getValue(), 1, new ArrayList<>(Arrays.asList(row))));
                        }
                    }

                    if (header.getValue().equalsIgnoreCase("date_of_birth") || header.getValue().equalsIgnoreCase("age_at_commencement")) {

                        if (StringUtils.isBlank(row_arr[header.getKey()])) {
                            errorsCount.add(1);
                            affectedColumnss.add(
                                    new AffectedColumn(header.getValue() + "/" + header.getValue(), 1, new ArrayList<>(Arrays.asList(row))));
                        }

                    }

                }
        );
        return affectedColumnss;
    }




}

