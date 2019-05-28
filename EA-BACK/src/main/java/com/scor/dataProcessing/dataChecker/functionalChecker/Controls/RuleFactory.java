package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.accumulators.MapAccumulator;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;

import java.util.ArrayList;
import java.util.List;


public class RuleFactory {





    private ControlResultAccumulatorV2 acc = new ControlResultAccumulatorV2();

    private MapAccumulator valuesPersistAccumulator = new MapAccumulator();


    private LongAccumulator errorsCount;

    private String type;
    private String op_start;
    private String op_end;
    private List<String> names;

    public RuleFactory(JavaSparkContext sc,String type,String op_start,String op_end, List<String> names) {


        this.type = type;
        this.op_end = op_end;
        this.op_start = op_start;
        this.names = names;


        sc.sc().register(acc);

        sc.sc().register(valuesPersistAccumulator);


        errorsCount = sc.sc().longAccumulator();

    }

    public List<IRule> getRules() {
        List<IRule> rules = new ArrayList<>();
        rules.add(new DateRules(acc,errorsCount));
        rules.add(new Control33(acc,errorsCount,type));
        rules.add(new Control6(acc,errorsCount,type));
        rules.add(new FloatComparaison(acc,errorsCount));
        rules.add(new Control10(acc,errorsCount));
        rules.add(new Control13(acc,errorsCount,type));
        rules.add(new Control14(acc,errorsCount,type));
        rules.add(new DateRules2(acc,errorsCount,op_start,op_end));
        rules.add(new Control20(acc,errorsCount));
        rules.add(new Control30(acc,errorsCount));
        rules.add(new Control23(acc,errorsCount));
        rules.add(new Control24(acc,errorsCount));
        rules.add(new Control25(acc,errorsCount));
        rules.add(new Control28(acc,errorsCount));
        rules.add(new Control29(acc,errorsCount));
        rules.add(new Control32(acc,errorsCount));
        rules.add(new Control34(acc,errorsCount,names,type));
        rules.add(new Control35(acc,errorsCount,names,type));
        rules.add(new Control36(acc,errorsCount));
        rules.add(new Control38(acc,errorsCount,names));
        rules.add(new Control39(acc,errorsCount));
        rules.add(new Control40(acc,errorsCount));
        rules.add(new Control42(acc,errorsCount));
        rules.add(new Control43(acc,errorsCount));
        rules.add(new Control44(acc,errorsCount));
        rules.add(new Control45(acc,errorsCount));
        rules.add(new Control46(acc,errorsCount));
        rules.add(new Control47(acc,errorsCount));
        rules.add(new Control52(acc,errorsCount));
        rules.add(new ValuePersist(valuesPersistAccumulator,errorsCount));
        return rules;
    }



    public MapAccumulator getValuesPersistAccumulator() {
        return valuesPersistAccumulator;
    }

    public LongAccumulator getErrorsCount() {
        return errorsCount;
    }

    public ControlResultAccumulatorV2 getAcc() {
        return acc;
    }
}
