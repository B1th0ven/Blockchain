package com.scor.dataProcessing.Helpers;

import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.List;

public class Grouping implements PairFunction<String, String, String[]> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 686080332209048231L;
	private List<String> names;
    private static int life_index;
    private static int policy_index;
    private static int benefit_index;
    private static int retro_legal_entity_index;

    public Grouping(List<String> names){
        this.names = names;
        this.life_index = names.indexOf(Headers.LIFE_ID);
        this.policy_index = names.indexOf(Headers.POLICY_ID);
        this.benefit_index = names.indexOf(Headers.BENEFIT_ID);
        this.retro_legal_entity_index = names.indexOf(Headers.RETRO_LEGAL_ENTITY);

    }


    @Override
    public Tuple2<String, String[]> call(String row_str) throws Exception {
        String[] row = row_str.toLowerCase().trim().split(";", -1);
        String id = "";
        if(life_index != -1) id += row[life_index];
        if(policy_index != -1) id += row[policy_index];
        if(benefit_index != -1) id += row[benefit_index];
        if(retro_legal_entity_index != -1) id += row[retro_legal_entity_index];

        return new Tuple2<>(id, row);

    }
}
