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

public class Control25 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control25(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
            String p_start = null;
            String p_end = null;
            if (product != null && product.getP_start_date() != null && product.getP_end_date() != null ) {
                p_start = product.getP_start_date();
                p_end = product.getP_end_date();
            }

            if (headers.indexOf("date_of_commencement") >= 0
                    && row[headers.indexOf("date_of_commencement")]
                    .matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")
                    && p_start != null && p_start.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && p_end != null
                    && p_end.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {

                try {
                    LocalDate doc = LocalDate.parse(row[headers.indexOf("date_of_commencement")],
                            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
                    LocalDate p_start_date = LocalDate.parse(p_start,
                            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
                    LocalDate p_end_date = LocalDate.parse(p_end,
                            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));

                    if (p_start_date.isAfter(doc) || doc.isAfter(p_end_date)
                            || p_start_date.isAfter(p_end_date)) {

                        List<AffectedColumn> affectedColumns = new ArrayList<>();
                        affectedColumns.add(new AffectedColumn(
                                "product start date <= date of commencement <= product end date", 1,
                                new ArrayList<>(Arrays.asList(String.join(";",row) + ";;" + p_start + ";" + p_end + ";;;;;"))));
                        controlResult.add(new ControlResult("Date Comparison", affectedColumns));
                        errorsCount.add(1);
                    }
                } catch (Exception e) {

                }

            }



    }
}
