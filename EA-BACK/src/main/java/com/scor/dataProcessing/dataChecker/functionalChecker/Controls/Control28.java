package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;
import org.apache.spark.util.LongAccumulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Control28 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;
    private final static String CONTROL_NAME = "min face amount <= risk amount insurer" ;


    public Control28(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {

        if (headers.contains("risk_amount_insurer")) {
            Double min_fa = null;
            Double max_fa = null;
            Double rai = null;

            if (product != null) {
                if (product.getMin_face_amount() != null && !product.getMin_face_amount().isEmpty()
                        && product.getMin_face_amount().replaceAll("\\,", "\\.")
                        .matches("^\\-?\\d+\\.?\\d*$"))
                    min_fa = Double.parseDouble(product.getMin_face_amount().replaceAll("\\,", "\\."));
            }
            if (row[headers.indexOf("risk_amount_insurer")] != null
                    && row[headers.indexOf("risk_amount_insurer")].replaceAll("\\,", "\\.")
                    .matches("^\\-?\\d+\\.?\\d*$"))
                rai = Double.parseDouble(
                        row[headers.indexOf("risk_amount_insurer")].replaceAll("\\,", "\\."));
            if (product != null) {
                if (product.getMax_face_amount() != null && !product.getMax_face_amount().isEmpty()
                        && product.getMax_face_amount().replaceAll("\\,", "\\.")
                        .matches("^\\-?\\d+\\.?\\d*$"))
                    max_fa = Double.parseDouble(product.getMax_face_amount().replaceAll("\\,", "\\."));
            }
            // System.out.println("****************"+min_fa+"**"+rai);

            if(rai != null){
                if(min_fa != null && max_fa != null){
                        if( rai < min_fa || rai > max_fa){
                            List<AffectedColumn> affectedColumns = new ArrayList<>();
                            affectedColumns.add(new AffectedColumn(CONTROL_NAME, 1,
                                    new ArrayList<>(Arrays.asList(String.join(";",row) + ";;;;" + product.getMin_face_amount() +  ";" + product.getMax_face_amount() +  ";;;"))));
                            controlResult.add(new ControlResult("Amount Comparison", affectedColumns));
                            errorsCount.add(1);
                        }
                }
                else if(min_fa != null && max_fa == null){
                        if(rai < min_fa){
                            List<AffectedColumn> affectedColumns = new ArrayList<>();
                            affectedColumns.add(new AffectedColumn(CONTROL_NAME, 1,
                                    new ArrayList<>(Arrays.asList(String.join(";",row) + ";;;;" + product.getMin_face_amount() + ";;;;"))));
                            controlResult.add(new ControlResult("Amount Comparison", affectedColumns));
                            errorsCount.add(1);
                        }
                }
                else if(min_fa == null && max_fa != null){
                        if(rai > max_fa){
                            List<AffectedColumn> affectedColumns = new ArrayList<>();
                            affectedColumns.add(new AffectedColumn(CONTROL_NAME, 1,
                                    new ArrayList<>(Arrays.asList(String.join(";",row) + ";;;;;" + product.getMax_face_amount() + ";;;"))));
                            controlResult.add(new ControlResult("Amount Comparison", affectedColumns));
                            errorsCount.add(1);
                        }
                }
            }



           /* if (min_fa != null && rai != null&& max_fa!=null && rai < min_fa && rai > max_fa) {
                List<AffectedColumn> affectedColumns = new ArrayList<>();
                affectedColumns.add(new AffectedColumn("min face amount <= risk amount insurer", 1,
                        new ArrayList<>(Arrays.asList(String.join(";",row) + ";;;;" + product.getMin_face_amount() + ";;;;"))));
                controlResult.add(new ControlResult("Amount Comparison", affectedColumns));
                errorsCount.add(1);
            }*/
        }
    }
}
