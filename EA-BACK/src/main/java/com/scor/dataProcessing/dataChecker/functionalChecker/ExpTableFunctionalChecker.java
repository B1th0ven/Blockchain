package com.scor.dataProcessing.dataChecker.functionalChecker;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.util.LongAccumulator;

import com.scor.dataProcessing.Helpers.MappingTableLibrary;
import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.common.DimensionPivot;
import com.scor.dataProcessing.dataChecker.functionalChecker.Operations.ExpTableOperations.CheckCombinations;
import com.scor.dataProcessing.dataChecker.functionalChecker.Operations.ExpTableOperations.MissingValuesControl;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.ControlResults;
import com.scor.dataProcessing.sparkConnection.Connection;

import scala.Tuple2;

public class ExpTableFunctionalChecker implements InterfaceToFunctionalChecker {
	

	private static final long serialVersionUID = -9147692310597453029L;


	private static final ExpTableFunctionalChecker instance = new ExpTableFunctionalChecker();

	private ExpTableFunctionalChecker() {
	}
	public static ExpTableFunctionalChecker getInstance() {
		return instance;
	}
	
  

    public  ControlResults run (String path,String type,HashMap<String,Integer> maxValues,HashMap<String,Integer> minValues){

        JavaRDD<String> data = sc.textFile(path);
        String header = data.first();
        List<String> names = Arrays.asList(header.toLowerCase().split(";", -1));
        List<String> Dimensions = names.stream().filter( s -> DimensionPivot.getDimensionCols().contains(s)).collect(Collectors.toList());
        String dimension = "";
        for (String string : Dimensions) {
        	dimension = dimension + ";" + string;
		}
        dimension = dimension.replaceFirst(";", "");
        ControlResultAccumulator fc_acc = new ControlResultAccumulator(
                new ControlResult("Functional Controls", new ArrayList<>()));
        sc.sc().register(fc_acc);
        ControlResultAccumulator missingValueAccumulator = new ControlResultAccumulator(
                new ControlResult("Missing values", new ArrayList<>()));
        sc.sc().register(missingValueAccumulator);
        ControlResultAccumulator combinationCheck = new ControlResultAccumulator(
                new ControlResult("Combination Check", new ArrayList<>()));
        sc.sc().register(combinationCheck);

        LongAccumulator errorsCount = sc.sc().longAccumulator();

        int c= 0;
        if(names.contains("age_attained")) c++;
        if(names.contains("age_at_commencement")) c++;
        if(names.contains("insurance_age_attained")) c++;
        if(c>1)
        {
            List<AffectedColumn> affectedColumns = new ArrayList<>();
            affectedColumns.add(new AffectedColumn("age_attained age_at_commencement both are provided", 1,
                    new ArrayList<>(Arrays.asList("age_attained age_at_commencement insurance_age_attained"))));
            fc_acc.add(new ControlResult("Functional Controls",affectedColumns));
            errorsCount.add(1);

        }
		ArrayList<String> examples = new ArrayList<>();
		if (names.contains("age_at_commencement") && !names.contains("age_at_commencement_definition")) {
			examples.add("age_at_commencement_definition age_at_commencement");
			errorsCount.add(1);
		}

		if (names.contains("age_attained") && !names.contains("attained_age_definition")) {
			examples.add("attained_age_definition age_attained");
			errorsCount.add(1);
		}
		if (names.contains("insurance_age_attained") && !names.contains("insurance_age_attained_def")) {
			examples.add("Insurance_age_attained_def Insurance_age_attained");
			errorsCount.add(1);
		}
		if (!examples.isEmpty()) {
			List<AffectedColumn> affectedColumns = new ArrayList<>();
			affectedColumns.add(new AffectedColumn(
					"age_at_commencement provided but age_at_commencement_definition doesn't exist", examples.size(), examples));
			fc_acc.add(new ControlResult("Functional Controls", affectedColumns));
		}

        //List<Integer> dimensionIndexs = getDimensionsIndexs(names);

        data.filter(s -> !s.toLowerCase().trim().replace(";","").equalsIgnoreCase("") && !s.equalsIgnoreCase(header))
                .map(s -> s.toLowerCase().trim().split(";", -1))
                .mapToPair(new PairFunction<String[],String,String>() {
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Tuple2<String,String> call(String[] row) throws Exception {

                        String id = "";
                        for(int i=0;i<names.size();i++){
                            if(!names.get(i).equalsIgnoreCase("data_line") && !names.get(i).equalsIgnoreCase("rate") && !names.get(i).equalsIgnoreCase("adjustment")&& !names.get(i).equalsIgnoreCase("trend_adjustment"))
                                id+=row[i]+";";
                        }
                      /*  for(int i=0;i<dimensionIndexs.size();i++){
                            id += row[dimensionIndexs.get(i)] + ";";
                        }*/
                       // if(id.equalsIgnoreCase("")) return new Tuple2<>(id, "0/"+String.join(";",row));
                        return new Tuple2<>(id, "1/"+String.join(";",row));

                    }
                }).reduceByKey(new Function2<String, String, String>() {
                     @Override
                       public String call(String a, String b) throws Exception {
                             String row = b.split("/",-1)[1];
                             Integer a_val = Integer.parseInt(a.split("/",-1)[0]);
                             Integer b_val = Integer.parseInt(b.split("/",-1)[0]);
                             Integer res = a_val+b_val;
                         return res+"/"+row;

                          }
               }).foreach(new VoidFunction<Tuple2<String, String>>() {

                      /**
                       *
                       */
                      private static final long serialVersionUID = 1L;

                      @Override
                      public void call(Tuple2<String, String> in) throws Exception {
                          Integer row_occ = Integer.parseInt(in._2.split("/",-1)[0]);
                          String row = in._2.split("/",-1)[1];

                         if(row_occ > 1){
                              List<AffectedColumn> affectedColumns = new ArrayList<>();
                              affectedColumns.add(new AffectedColumn("Duplicated lines", 1,
                                      new ArrayList<>(Arrays.asList(row))));
                              fc_acc.add(new ControlResult("Functional Controls",affectedColumns));
                              errorsCount.add(1);
                          }


                  }
               });


        checkCombination(names,data,type,combinationCheck,maxValues,minValues);
        List<String> missingValuesHeaders = findMissingValuesHeaders(names,type);
        MissingValuesControl control = new MissingValuesControl(missingValuesHeaders,names,missingValueAccumulator,errorsCount);
		data.filter(s -> !s.toLowerCase().trim().replace(";","").equalsIgnoreCase("")).foreach(control);
        List<ControlResult> controlResultsList = new ArrayList<>();
        controlResultsList.add(fc_acc.value());
        controlResultsList.add(missingValueAccumulator.value());
        controlResultsList.add(combinationCheck.value());

        return new ControlResults(null,errorsCount.value(),controlResultsList,dimension,new HashMap<>());
    }

	private static List<String> findMissingValuesHeaders(List<String> names, String type) {
		List<String> missingValuesHeaders = Arrays.asList("calendar_year", "insurance_age_attained",
				"insurance_age_attained_def", "age_attained", "attained_age_definition", "duration_year",
				"result_metric","age_at_commencement","age_at_commencement_definition");
		List<String> missingValuesToCheck = new ArrayList<>();
		for (String header : missingValuesHeaders) {
			if(names.indexOf(header) > -1) {
				missingValuesToCheck.add(header);
			}
		}
		if(type.toLowerCase().trim().equals("base")) {
			if(names.indexOf("rate") > -1) {
				missingValuesToCheck.add("rate");
			}
		}
		if(type.toLowerCase().trim().equals("trend")) {
			if(names.indexOf("rate") > -1) {
				missingValuesToCheck.add("rate");
			}
			if(names.indexOf("trend_adjustment") > -1) {
				missingValuesToCheck.add("trend_adjustment");
			}
		}
		if(type.toLowerCase().trim().equals("adjustment")) {
			if(names.indexOf("adjustment") > -1) {
				missingValuesToCheck.add("adjustment");
			}
		}
		return missingValuesToCheck;
	}

	public static void checkCombination(List<String> names,JavaRDD<String> data,String type,ControlResultAccumulator combinationResult,HashMap<String,Integer> maxValues,HashMap<String,Integer> minValues){

        if(!type.trim().toLowerCase().equalsIgnoreCase("policy")){

                 String header = data.first();
                 data.filter(s -> !s.toLowerCase().trim().replace(";","").equalsIgnoreCase("") && !s.equalsIgnoreCase(header))
                .mapToPair(new MappingTableLibrary(names))
                .groupByKey()
                .foreach(new CheckCombinations(maxValues,minValues,names,combinationResult));
        }

    }

    public static List<Integer> getDimensionsIndexs(List<String> headers){
        List<String> dimensions = DimensionPivot.getDimensionCols();
        List<Integer> Indexs = new ArrayList<>();
        for(int i=0;i<headers.size();i++){
            if(dimensions.contains(headers.get(i).toLowerCase().trim())) {Indexs.add(i);}
        }
          return Indexs;
    }


}
