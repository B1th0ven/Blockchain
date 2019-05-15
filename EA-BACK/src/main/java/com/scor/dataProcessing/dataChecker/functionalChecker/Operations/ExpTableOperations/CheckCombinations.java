package com.scor.dataProcessing.dataChecker.functionalChecker.Operations.ExpTableOperations;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.VoidFunction;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;

import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class CheckCombinations implements VoidFunction<Tuple2<String, Iterable<String>>> {

    HashMap<String,Integer> maxValues;
    HashMap<String,Integer> minValues;
    List<String> combinations = new ArrayList<>();
    List<String> headers;
    ControlResultAccumulator combinationResult;

    public CheckCombinations(HashMap<String, Integer> maxValues,HashMap<String, Integer> minValues, List<String> headers,ControlResultAccumulator combinationResult) {
        this.combinationResult = combinationResult;
        this.headers = headers;
        this.maxValues = maxValues;
        this.minValues = minValues;

        List<List<String>> values = new ArrayList<>();
        if(maxValues.get("age") != null && minValues.get("age") != null)
            values.add(generateRange(minValues.get("age"),maxValues.get("age")));
        if(maxValues.get("duration_year") != null && minValues.get("duration_year") != null)
            values.add(generateRange(minValues.get("duration_year"),maxValues.get("duration_year")));
        if(minValues.get("calendar_year") != null && maxValues.get("calendar_year") != null)
            values.add(generateRange(minValues.get("calendar_year"),maxValues.get("calendar_year")));

        this.GeneratePermutations(values,this.combinations,0,"");


    }

    @Override
    public void call(Tuple2<String, Iterable<String>> stringIterableTuple2) throws Exception {
        //System.out.println("Key : "+stringIterableTuple2._1+" Number of lines : "+ StreamSupport.stream(stringIterableTuple2._2.spliterator(), true).count()+" Number of Combinations : "+this.combinations.size());
        String misssingCombination = null;
          for (String values : this.combinations)  {
              boolean found = false;
          //  System.out.println("Key : "+stringIterableTuple2._1+" Val : "+values);
           // System.out.println("Combination found : "+values);
            for (String line : stringIterableTuple2._2){
                //if(values.equalsIgnoreCase(line)) System.out.println("Key : "+stringIterableTuple2._1+" Combination : "+values+" Line : "+line+" Equals : "+values.equalsIgnoreCase(line)+" Number Of Comb : "+combinations.size());
                if(values.equalsIgnoreCase(line)) {
                    found = true;
                    break;
                }
            }
            if(!found &&  StringUtils.isNotBlank(values.trim())) {
                misssingCombination = values;
               // System.out.println("Key : "+stringIterableTuple2._1+" Control Failed "+misssingCombination);
                List<AffectedColumn> affectedColumns = new ArrayList<>();
                affectedColumns.add(new AffectedColumn("Combination Check", 1,
                        new ArrayList<>(Arrays.asList(stringIterableTuple2._1+"&"+misssingCombination))));
                combinationResult.add(new ControlResult("Combination Check",affectedColumns));
                //break;
            }

        }
    }

    private void GeneratePermutations(List<List<String>> Lists, List<String> result, int depth, String current)
    {
        if(depth == Lists.size())
        {
            result.add(current);
            return;
        }

        for(int i = 0; i < Lists.get(depth).size(); ++i)
        {
            GeneratePermutations(Lists, result, depth + 1, current + Lists.get(depth).get(i) + ";");
        }
    }

    private List<String> generateRange(Integer lowNum,Integer highNum ){
        List<String> res = new ArrayList<>();

        IntStream.rangeClosed(lowNum, highNum).forEach(no -> {
            res.add(Integer.toString(no));
        });

        return res;
    }
}
