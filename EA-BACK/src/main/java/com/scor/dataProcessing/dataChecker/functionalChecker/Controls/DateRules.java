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
import java.util.stream.Collectors;

public class DateRules implements IRule {
    List<List<String>> dates_to_comp;
    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public DateRules(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
        dates_to_comp = new ArrayList<>();
        dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_birth", "date_of_commencement")));
        dates_to_comp.add(
                new ArrayList<>(Arrays.asList("date_of_begin_current_condition", "date_of_end_current_condition")));
        dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_commencement", "date_of_event_incurred")));
        dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_commencement", "benefit_end_date")));
        dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_commencement", "date_of_begin_current_condition")));

        dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_event_incurred", "benefit_end_date"))); // sprint 3
        dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_end_current_condition", "benefit_end_date")));
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        for (List<String> dtc : dates_to_comp) {

            if (headers.containsAll(dtc)) {

                ArrayList<LocalDate> dates = new ArrayList<>();
                boolean empty = false;
                for (String col : dtc) {
                    if (headers.indexOf(col) == -1) {
                        empty = true;
                        break;
                    } else if (!row[headers.indexOf(col)].matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
                        empty = true;
                        break;
                    }
                    try {
                        dates.add(LocalDate.parse(row[headers.indexOf(col)],
                                DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
                    } catch (Exception e) {
                        continue;
                    }
                }
                if (empty)
                    continue;
                for (int j = 0; j < dates.size() - 1; j++) {
                    if (dates.get(j).isAfter(dates.get(j + 1))) {
                        List<AffectedColumn> affectedColumns = new ArrayList<>();
                        affectedColumns.add(new AffectedColumn(
                                String.join(" & ",
                                        dtc.stream().map(s -> s.replace("_", " "))
                                                .collect(Collectors.toList())),
                                1, new ArrayList<>(Arrays.asList(String.join(";", row)))));
                        controlResult.add(new ControlResult("Date Comparison", affectedColumns));
                        errorsCount.add(1);
                        break;
                    }
                }
            }

        }
    }
}
