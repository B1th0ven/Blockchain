package com.scor.dataProcessing.Helpers;

import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.List;

public class GroupingV3 implements PairFunction<String,String,String[]> {


    private List<String> names;
    private static int life_index;
    private static int policy_index;
    private static int benefit_index;

    public GroupingV3(List<String> names) {
        this.names = names;
        this.life_index = names.indexOf(Headers.LIFE_ID);
        this.policy_index = names.indexOf(Headers.POLICY_ID);
        this.benefit_index = names.indexOf(Headers.BENEFIT_ID);
    }

    @Override
    public Tuple2<String, String[]> call(String s) throws Exception {
        String[] row = s.toLowerCase().trim().split(";", -1);
        String id = "";
        if(life_index != -1) id += row[life_index];
        if(policy_index != -1) id += row[policy_index];
        if(benefit_index != -1) id += row[benefit_index];
        return new Tuple2<>(id,row);
    }
}
