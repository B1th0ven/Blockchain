package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Control10 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control10(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        String age_def = null;
        if (product != null && product.getAge_def() != null) {
            age_def = product.getAge_def().trim();
        }
        if (headers.containsAll(Arrays.asList("date_of_birth"))
                && age_def != null) {

            String DOB_s = row[headers.indexOf("date_of_birth")].trim();

        if (headers.containsAll(Arrays.asList("age_at_commencement", "date_of_commencement"))) {
            String AAC_s = row[headers.indexOf("age_at_commencement")];
            String DC_s = row[headers.indexOf("date_of_commencement")];
            if (AAC_s.matches("^\\-?\\d+\\,?\\d*$") && DC_s.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")
                    && DOB_s.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && age_def != null
                    && !age_def.isEmpty()) {
                try {
                    int AAC = Integer.parseInt(AAC_s);
                    LocalDate DC = LocalDate.parse(DC_s,
                            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
                    LocalDate DOB = LocalDate.parse(DOB_s,
                            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));

//								long daysBetween = ChronoUnit.DAYS.between(DOB, DC);
//								double age = daysBetween / (double) 365.25;
                    int calculatedAge = 0;
                    int years = DC.getYear() - DOB.getYear();
                    float days = DC.getDayOfYear() - DOB.getDayOfYear();
                    float daysNearest = DC.getDayOfYear() - DOB.getDayOfYear() + 1;
                    int nbOfDaysPerYear = 365;
                    if((DC.getYear() % 4) == 0) {
                        nbOfDaysPerYear = 366;
                    }
                    double ageNext = years + (days / nbOfDaysPerYear);
                    double ageNearest = years + (daysNearest / nbOfDaysPerYear);

                    switch (age_def) {
                        case "age last birthday":
                            calculatedAge = (int) ageNext;
                            break;
                        case "age nearest birthday":
                            calculatedAge = (int) Math.round(ageNearest);
                            break;
                        case "age next birthday":
                            calculatedAge = (int) ageNext + 1;
                            break;
                        default:
                            break;
                    }
                    if (AAC != calculatedAge) {
                        List<AffectedColumn> affectedColumns = new ArrayList<>();
                        affectedColumns.add(new AffectedColumn(
                                "date of commencement = age at commencement - date of birth", 1,
                                new ArrayList<>(Arrays.asList(String.join(";",row) + ";" + age_def))));
                        controlResult.add(new ControlResult("Date Comparison", affectedColumns));
                        errorsCount.add(1);
                    }
                } catch (Exception e) {

                }
            }
        }
    }
}
}
