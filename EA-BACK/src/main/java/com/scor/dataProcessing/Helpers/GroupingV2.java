package com.scor.dataProcessing.Helpers;

import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.List;

public class GroupingV2 implements PairFunction<String, String, String> {

    private List<String> names ;
    private int indexReportingVariable ;

    public GroupingV2(List<String> names, int indexReportingVariable) {
        this.names = names;
        this.indexReportingVariable = indexReportingVariable;
    }

    @Override
    public Tuple2<String, String> call(String s) throws Exception {
        String key = "";
        String value = "";
        String[] row_arr = s.trim().split(";",-1);
        for(int i = 0 ; i < row_arr.length ; i++){
            if(i != indexReportingVariable){
                key += row_arr[i] + ";";
            }
            else{
                value = row_arr[i];
            }
        }


        return new Tuple2<>(key,value);
    }
}
