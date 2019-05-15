package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.util.LongAccumulator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Control20 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control20(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }
    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        String dateOfEventIncurred = (headers.indexOf("date_of_event_incurred") >= 0)
                ? row[headers.indexOf("date_of_event_incurred")]
                : "";
        String dateOfEventNotified = (headers.indexOf("date_of_event_notified") >= 0)
                ? row[headers.indexOf("date_of_event_notified")]
                : "";
        String dateOfEventSettled = (headers.indexOf("date_of_event_settled") >= 0)
                ? row[headers.indexOf("date_of_event_settled")]
                : "";
        String dateOfEventPaid = (headers.indexOf("date_of_event_paid") >= 0)
                ? row[headers.indexOf("date_of_event_paid")]
                : "";
        if (!check(dateOfEventIncurred, dateOfEventNotified, dateOfEventSettled,
                dateOfEventPaid)) {
            List<AffectedColumn> affectedColumns = new ArrayList<>();
            affectedColumns.add(new AffectedColumn(
                    "date of event incurred & date of event notified & date of event settled & date of event paid",
                    1, new ArrayList<>(Arrays.asList(String.join(";",row)))));
            controlResult.add(new ControlResult("Date Comparison", affectedColumns));
            errorsCount.add(1);
        }
    }

    //	-	Date of Event Incurred <= Date of Event Notified <= Date of Event Settled <= Date of Event Paid
    private boolean check(String dateOfEventIncurred, String dateOfEventNotified, String dateOfEventSettled, String dateOfEventPaid) {
        List<LocalDate> dateToComapre = new ArrayList<>();
        if (StringUtils.isNotBlank(dateOfEventIncurred) && dateOfEventIncurred.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
            try {
                dateToComapre.add(
                        LocalDate.parse(dateOfEventIncurred, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
            } catch (Exception e) {

            }
        }
        if (StringUtils.isNotBlank(dateOfEventNotified) && dateOfEventNotified.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
            try {
                dateToComapre.add(
                        LocalDate.parse(dateOfEventNotified, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
            } catch (Exception e) {

            }
        }
        if (StringUtils.isNotBlank(dateOfEventSettled) && dateOfEventSettled.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
            try {
                dateToComapre.add(
                        LocalDate.parse(dateOfEventSettled, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
            } catch (Exception e) {

            }
        }
        if (StringUtils.isNotBlank(dateOfEventPaid) && dateOfEventPaid.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
            try {
                dateToComapre
                        .add(LocalDate.parse(dateOfEventPaid, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
            } catch (Exception e) {

            }
        }

        for (int j = 0; j < dateToComapre.size() - 1; j++) {
            if (dateToComapre.get(j).isAfter(dateToComapre.get(j + 1))) {
                return false;
            }
        }

        return true;
    }
}
