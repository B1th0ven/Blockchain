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

public class Control34 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;
    final List<String> headers;
    final String type;
    final HashMap<Integer, String> missingValuesCheckHeaders;
    final Map<Integer, Integer> missingValuesWithConditionHeaders;
   // public Map<Integer, Integer> missingvalues1;


    public Control34(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo, List<String> headers, String type){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
        this.headers = headers;
        this.type = type;
        this.missingValuesCheckHeaders = fillHeadersForEmptyCheck(headers);
        this.missingValuesWithConditionHeaders = fillHeadersForEmptyCheckWithCondition(headers);



    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        Map<Integer, Integer> missingvalues1 = fillHeadersForEmptyCheckWithConditionNew(headers,row);
        if (type.equalsIgnoreCase("combine") || (type.equalsIgnoreCase("split")
                && headers.contains("exposure_or_event")
                && ("exposure + event".equalsIgnoreCase(row[headers.indexOf("exposure_or_event")])
                || "exposure".equalsIgnoreCase(row[headers.indexOf("exposure_or_event")])))) {
            List<AffectedColumn> affectedColumnsMissingValues = missingValuesCheck(
                    missingValuesCheckHeaders, missingValuesWithConditionHeaders, missingvalues1,row, String.join(";",row));
            if (!affectedColumnsMissingValues.isEmpty()) {
                controlResult
                        .add(new ControlResult("Missing Values Check", affectedColumnsMissingValues));
            }
        }
    }

    private List<AffectedColumn> missingValuesCheck(HashMap<Integer, String> missingValuesCheckHeaders,
                                                    Map<Integer, Integer> missingValuesWithConditionHeaders, Map<Integer,Integer> missingvalueBTR,String[] row_arr, String row) {
        // missingvalueBTR = missingvalue Benefit_Term_Years
        List<AffectedColumn> affectedColumnss = new ArrayList<>();
        missingValuesCheckHeaders.entrySet().forEach(header -> {
            if (StringUtils.isBlank(row_arr[header.getKey()])) {
                errorsCount.add(1);
                affectedColumnss.add(
                        new AffectedColumn(header.getValue() + "/" + header.getValue(), 1, new ArrayList<>(Arrays.asList(row))));
            }
        });
        missingValuesWithConditionHeaders.entrySet().forEach(header -> {
            if (StringUtils.isNotBlank(row_arr[header.getValue()])
                    && StringUtils.isBlank(row_arr[header.getKey()])) {
                errorsCount.add(1);
                affectedColumnss.add(
                        new AffectedColumn(headers.get(header.getKey()) + "/" + headers.get(header.getValue()), 1, new ArrayList<>(Arrays.asList(row))));
            }
        });
        missingvalueBTR.entrySet().forEach(header -> {
            if (StringUtils.isNotBlank(row_arr[header.getValue()])
                    && StringUtils.isBlank(row_arr[header.getKey()])) {
                errorsCount.add(1);
                affectedColumnss.add(
                        new AffectedColumn(headers.get(header.getKey()) + "/" + headers.get(header.getValue()), 1, new ArrayList<>(Arrays.asList(row))));
            }
        });

        return affectedColumnss;
    }
    // this fnct may be deleted if we dont have this type of control without condition
    private static HashMap<Integer, String> fillHeadersForEmptyCheck(List<String> names) {
        // missing values will appear here
        List<String> headersToCheck = Arrays.asList("age_at_commencement" , "age_at_commencement_definition");
        //   "risk_amount_insurer",
        //  "risk_amount_reinsurer", "annual_premium_insurer",
        //  "annual_premium_reinsurer");
        HashMap<Integer, String> missingValuesHeadersToCheck = new HashMap<>();
        for (String header : headersToCheck) {
            if (!names.contains(header)) {
                continue;
            }
            missingValuesHeadersToCheck.put(names.indexOf(header), header);
        }

        return missingValuesHeadersToCheck;
    }

    private  Map<Integer, Integer> fillHeadersForEmptyCheckWithCondition(List<String> names ) {
        Map<Integer, Integer> indexOfHeaders = new HashMap<>();
        if (names.contains("acceleration_risk_type")) {
            if (names.contains("acceleration_risk_amount_insurer")) {
                indexOfHeaders.put(names.indexOf("acceleration_risk_amount_insurer"),
                        names.indexOf("acceleration_risk_type"));
            }

            if (names.contains("acceleration_risk_amount_reinsur")) {
                indexOfHeaders.put(names.indexOf("acceleration_risk_amount_reinsur"),
                        names.indexOf("acceleration_risk_type"));
            }
            if (names.contains("risk_amount_insurer")) {
                indexOfHeaders.put(names.indexOf("risk_amount_insurer"),
                        names.indexOf("acceleration_risk_type"));
            }
            if (names.contains("event_amount _insurer")) {
                indexOfHeaders.put(names.indexOf("event_amount _insurer"),
                        names.indexOf("acceleration_risk_type"));
            }
            if (names.contains("event_amount_reinsurer")) {
                indexOfHeaders.put(names.indexOf("event_amount_reinsurer"),
                        names.indexOf("acceleration_risk_type"));
            }
            if (names.contains("risk_amount_reinsurer")) {
                indexOfHeaders.put(names.indexOf("risk_amount_reinsurer"),
                        names.indexOf("acceleration_risk_type"));
            }
            if (names.contains("annual_premium_insurer")) {
                indexOfHeaders.put(names.indexOf("annual_premium_insurer"),
                        names.indexOf("acceleration_risk_type"));
            }
            if (names.contains("annual_premium_reinsurer")) {
                indexOfHeaders.put(names.indexOf("annual_premium_reinsurer"),
                        names.indexOf("acceleration_risk_type"));
            }

        }



        if (names.contains("benefit_change_rate_type")) {
            if (names.contains("benefit_change_rate_annual")) {
                indexOfHeaders.put(names.indexOf("benefit_change_rate_annual"), names.indexOf("benefit_change_rate_type"));
            }
            // if (names.contains("benefit_term_years")) {
            //    indexOfHeaders.put(names.indexOf("benefit_term_years"), names.indexOf("benefit_change_rate_type"));
            // }
            if (names.contains("benefit_change_frequency")) {
                indexOfHeaders.put(names.indexOf("benefit_change_frequency"),
                        names.indexOf("benefit_change_rate_type"));
            }
        }


        if (names.contains("type_of_event")) {
            if (names.contains("event_amount_reinsurer")) {
                indexOfHeaders.put(names.indexOf("event_amount_reinsurer"), names.indexOf("type_of_event"));
            }
            if (names.contains("event_amount_insurer")) {
                indexOfHeaders.put(names.indexOf("event_amount_insurer"), names.indexOf("type_of_event"));
            }
        }
        return indexOfHeaders;
    }

    private  Map<Integer, Integer> fillHeadersForEmptyCheckWithConditionNew(List<String> names , String[] row_arr ) {
        Map<Integer, Integer> indexOfHeaders = new HashMap<>();
        if (names.contains("Benefit_Change_Rate_Type")) {
            if (row_arr[headers.indexOf("Benefit_Change_Rate_Type")].trim().equalsIgnoreCase("Loan Interest")) {
                if (names.contains("benefit_term_years")) {
                    indexOfHeaders.put(names.indexOf("benefit_term_years"), names.indexOf("benefit_change_rate_type"));
                }}


        } return indexOfHeaders;}
}
