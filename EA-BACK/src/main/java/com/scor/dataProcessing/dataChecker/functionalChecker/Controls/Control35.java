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

public class Control35 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;
    final List<String> headers;
    final String type;
    final Map<Integer, String> missingValuesWithConditionHeaders2;

    public Control35(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo, List<String> headers, String type){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
        this.headers = headers;
        this.type = type;
        this.missingValuesWithConditionHeaders2 = fillHeadersForEmptyCheckWithCondition2(headers);


    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        List<AffectedColumn> affectedColumnsMissingValues2 = missingValuesCheckBlocking(
                missingValuesWithConditionHeaders2, row, String.join(";",row),product);
        if (!affectedColumnsMissingValues2.isEmpty()) {
            controlResult.add(
                    new ControlResult("Missing Values Check Blocking", affectedColumnsMissingValues2));
        }
    }

    private  Map<Integer, String> fillHeadersForEmptyCheckWithCondition2(List<String> names) {
        Map<Integer, String> indexOfHeaders = new HashMap<>();

        /// Vice Versa controls :
        if (names.contains("type_of_event")) {
            indexOfHeaders.put(names.indexOf("type_of_event"), "date_of_event_incurred");
        }
        if (names.contains("date_of_event_incurred")) {
            indexOfHeaders.put(names.indexOf("date_of_event_incurred"), "type_of_event");
        }

        if (names.contains("temp_mult_extra_rating_term_1")) {
            indexOfHeaders.put(names.indexOf("temp_mult_extra_rating_term_1"), "temp_mult_extra_rating_1");
        }
        if (names.contains("temp_mult_extra_rating_1")) {
            indexOfHeaders.put(names.indexOf("temp_mult_extra_rating_1"), "temp_mult_extra_rating_term_1");
        }

        if (names.contains("temp_add_extra_rating_term_1")) {
            indexOfHeaders.put(names.indexOf("temp_add_extra_rating_term_1"), "temp_add_extra_rating_1");
        }
        if (names.contains("temp_add_extra_rating_1")) {
            indexOfHeaders.put(names.indexOf("temp_add_extra_rating_1"), "temp_add_extra_rating_term_1");
        }

        if (names.contains("temp_mult_extra_rating_term_2")) {
            indexOfHeaders.put(names.indexOf("temp_mult_extra_rating_term_2"), "temp_mult_extra_rating_2");
        }
        if (names.contains("temp_mult_extra_rating_2")) {
            indexOfHeaders.put(names.indexOf("temp_mult_extra_rating_2"), "temp_mult_extra_rating_term_2");
        }

        if (names.contains("temp_add_extra_rating_term_2")) {
            indexOfHeaders.put(names.indexOf("temp_add_extra_rating_term_2"), "temp_add_extra_rating_2");
        }
        if (names.contains("temp_add_extra_rating_2")) {
            indexOfHeaders.put(names.indexOf("temp_add_extra_rating_2"), "temp_add_extra_rating_term_2");
        }

        /// Simple Controls :
        if(names.contains("smoker_status_detailed")) {
            indexOfHeaders.put(names.indexOf("smoker_status_detailed"), "smoker_status");
        }
        if (names.contains("benefit_term_years")) {
            indexOfHeaders.put(names.indexOf("benefit_term_years"), "benefit_term_type");
        }
        if (names.contains("benefit_max_age")) {
            indexOfHeaders.put(names.indexOf("benefit_max_age"), "benefit_term_type");
        }
        if (names.contains("multiplicative_rated_status")) {
            indexOfHeaders.put(names.indexOf("multiplicative_rated_status"), "rated_status");
        }
        if (names.contains("additive_rated_status")) {
            indexOfHeaders.put(names.indexOf("additive_rated_status"), "rated_status");
        }
        if (names.contains("waiting_period_1_type")) {
            indexOfHeaders.put(names.indexOf("waiting_period_1_type"), "waiting_period_1");
        }
        if (names.contains("waiting_period_2_type")) {
            indexOfHeaders.put(names.indexOf("waiting_period_2_type"), "waiting_period_2");
        }
        if (names.contains("waiting_period_3_type")) {
            indexOfHeaders.put(names.indexOf("waiting_period_3_type"), "waiting_period_3");
        }
//		if (names.contains("child_benefit_type") && names.contains("child_benefit")) {
//			indexOfHeaders.put(names.indexOf("child_benefit"), names.indexOf("child_benefit_type"));
//		}
        if (names.contains("child_benefit_type")) {
            indexOfHeaders.put(names.indexOf("child_benefit_type"), "child_benefit");
        }
//		if (names.contains("acceleration_risk_type") && names.contains("acceleration_benefit")) {
//			indexOfHeaders.put(names.indexOf("acceleration_benefit"), names.indexOf("acceleration_risk_type"));
//		}
        if (names.contains("acceleration_risk_type")) {
            indexOfHeaders.put(names.indexOf("acceleration_risk_type"), "acceleration_benefit");
        }
//		if (names.contains("buyback_option_type") && names.contains("buyback_option")) {
//			indexOfHeaders.put(names.indexOf("buyback_option"), names.indexOf("buyback_option_type"));
//		}
        if (names.contains("buyback_option_type")) {
            indexOfHeaders.put(names.indexOf("buyback_option_type"), "buyback_option");
        }
        if (names.contains("benefit_change_rate_annual")) {
            indexOfHeaders.put(names.indexOf("benefit_change_rate_annual"), "benefit_change_frequency");
        }
        if (names.contains("mult_rating_upper_band_1")) {
            indexOfHeaders.put(names.indexOf("mult_rating_upper_band_1"), "rating_type_1");
        }
        if (names.contains("add_rating_upper_band_1")) {
            indexOfHeaders.put(names.indexOf("add_rating_upper_band_1"), "rating_type_1");
        }
        if (names.contains("mult_rating_upper_band_2")) {
            indexOfHeaders.put(names.indexOf("mult_rating_upper_band_2"), "rating_type_2");
        }
        if (names.contains("add_rating_upper_band_2")) {
            indexOfHeaders.put(names.indexOf("add_rating_upper_band_2"), "rating_type_2");
        }
        return indexOfHeaders;
    }

    private List<AffectedColumn> missingValuesCheckBlocking(
            Map<Integer, String> missingValuesWithConditionHeaders, String[] row_arr, String row, Product product) {
        List<AffectedColumn> affectedColumnss = new ArrayList<>();
        missingValuesWithConditionHeaders.entrySet().forEach(header -> {
            String headerName = header.getValue();
            String value = null;
            if("rating_type_1".equals(headerName)) {
                if(product != null){
                    value = product.getRating_type_1();
                    headerName += "/product_id";
                }
            } else if("rating_type_2".equals(headerName)) {
                if(product != null) {
                    value = product.getRating_type_2();
                    headerName += "/product_id";
                }
            } else {
                value = headers.indexOf(headerName) > -1 ? row_arr[headers.indexOf(headerName)] : "";
            }
            if (StringUtils.isNotBlank(row_arr[header.getKey()]) && (StringUtils.isBlank(value))) {
                errorsCount.add(1);
                affectedColumnss.add(new AffectedColumn(headerName + "/" + headers.get(header.getKey()), 1,
                        new ArrayList<>(Arrays.asList(row))));
            }
        });
        return affectedColumnss;
    }
}
