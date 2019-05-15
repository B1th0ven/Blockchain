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

public class Control23 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control23(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        if (headers.containsAll(Arrays.asList("date_of_birth"))) {

            String DOB_s = row[headers.indexOf("date_of_birth")].trim();
            String age_def = null;
            if (product != null &&  product.getAge_def() != null) {
                age_def = product.getAge_def().trim();
            }
        if (DOB_s.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && age_def != null && !age_def.isEmpty()
                && headers.containsAll(Arrays.asList("benefit_end_date"))
                && headers.containsAll(Arrays.asList("benefit_max_age"))
                && row[headers.indexOf("benefit_max_age")].matches("^\\-?\\d+\\,?\\d*$")) {

            try {
                int coverExpiryAge = 0;
                int cea = Integer.parseInt(row[headers.indexOf("benefit_max_age")]);
                LocalDate ced = LocalDate.parse(row[headers.indexOf("benefit_end_date")],
                        DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
                LocalDate dob = LocalDate.parse(row[headers.indexOf("date_of_birth")],
                        DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));

//							long daysBetween = ChronoUnit.DAYS.between(dob, ced);
//							double age = daysBetween / (double) 365.25;
                int years = ced.getYear() - dob.getYear();
                float days = ced.getDayOfYear() - dob.getDayOfYear();
                float daysNearest = ced.getDayOfYear() - dob.getDayOfYear() + 1;
                int nbOfDaysPerYear = 365;
                if((ced.getYear() % 4) == 0) {
                    nbOfDaysPerYear = 366;
                }
                double age = years + (days / nbOfDaysPerYear);
                double ageNearest = years + (daysNearest / nbOfDaysPerYear);
                switch (age_def) {
                    case "age last birthday":
                        coverExpiryAge = (int) age;
                        break;
                    case "age nearest birthday":
                        coverExpiryAge = (int) Math.round(ageNearest);
                        break;
                    case "age next birthday":
                        coverExpiryAge = (int) age + 1;
                        break;
                    default:
                        break;
                }

                if (cea < coverExpiryAge) {
                    List<AffectedColumn> affectedColumns = new ArrayList<>();
                    affectedColumns
                            .add(new AffectedColumn("cover expiry date - date of birth <= cover expiry age",
                                    1, new ArrayList<>(Arrays.asList(String.join(";",row) + ";" + age_def))));
                    controlResult.add(new ControlResult("Date Comparison", affectedColumns));
                    errorsCount.add(1);
                }
            } catch (Exception e) {

            }
        }
    }
}
}
