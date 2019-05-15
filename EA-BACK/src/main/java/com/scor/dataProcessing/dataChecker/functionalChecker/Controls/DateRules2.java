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

public class DateRules2 implements IRule {
    List<List<String>> dates_to_com_to_input;
    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;
    String op_end;
    String op_start;

    public DateRules2(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo, String op_start, String op_end){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
        this.op_end = op_end;
        this.op_start = op_start;
        dates_to_com_to_input = new ArrayList<>();
        dates_to_com_to_input.add(new ArrayList<>(Arrays.asList("start_of_observation_period", "benefit_end_date")));
        dates_to_com_to_input
                .add(new ArrayList<>(Arrays.asList("date_of_end_current_condition", "end_of_observation_period")));
        dates_to_com_to_input
                .add(new ArrayList<>(Arrays.asList("start_of_observation_period", "date_of_event_incurred")));
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        if (!((op_start == null || op_start.isEmpty()) && (op_end == null || op_end.isEmpty()))) {
            for (List<String> dtci : dates_to_com_to_input) {
                ArrayList<LocalDate> dts = new ArrayList<>();
                for (String d : dtci) {

                    if (d.equalsIgnoreCase("start_of_observation_period") && !(op_start == null || op_start.isEmpty())
                            && op_start.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
                        try {
                            dts.add(LocalDate.parse(op_start, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
                        } catch (Exception e) {

                        }
                    } else if (d.equalsIgnoreCase("end_of_observation_period") && !(op_end == null || op_end.isEmpty())
                            && op_end.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
                        try {
                            dts.add(LocalDate.parse(op_end, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
                        } catch (Exception e) {

                        }
                    } else if (headers.indexOf(d) != -1 && !row[headers.indexOf(d)].isEmpty()
                            && row[headers.indexOf(d)].matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
                        try {
                            dts.add(LocalDate.parse(row[headers.indexOf(d)],
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
                        } catch (Exception e) {

                        }
                    }
                }
                if (dts.size() == 2) {
                    if (dts.get(0).isAfter(dts.get(1))) {

                        List<AffectedColumn> affectedColumns = new ArrayList<>();
                        affectedColumns.add(new AffectedColumn(
                                dtci.get(0).replace("_", " ") + " <= " + dtci.get(1).replace("_", " "), 1,
                                new ArrayList<>(Arrays.asList(String.join(";", row)))));
                        controlResult.add(new ControlResult("Date Comparison", affectedColumns));
                        errorsCount.add(1);
                    }
                }
            }
        }
    }
}
