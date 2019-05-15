package com.scor.dataProcessing.Helpers;

import org.apache.spark.api.java.function.PairFunction;

import com.scor.dataProcessing.common.DimensionPivot;

import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class MappingTableLibrary implements PairFunction<String, String, String> {

    List<String> headers;
   // List<Integer> indexs;


    public MappingTableLibrary(List<String> names) {
        this.headers = names;
      //  this.indexs = this.getDimensionsIndexs();

    }


    @Override
    public Tuple2<String, String> call(String line) throws Exception {
        String[] row = line.toLowerCase().trim().split(";", -1);
        String key = "";
        for(int i=0;i<headers.size();i++){
            if(keyOrNot(i))
                key+=row[i]+";";
        }
        String rowValue = "";

        if(getAgeIndex(this.headers) != null) rowValue += row[getAgeIndex(this.headers)] + ";";
        if(headers.contains("duration_year")) rowValue += row[headers.indexOf("duration_year")] + ";";
        if(headers.contains("calendar_year")) rowValue += row[headers.indexOf("calendar_year")] + ";";

        return  new Tuple2<>(key, rowValue);
    }

   /* private List<Integer> getDimensionsIndexs(){
        List<String> dimensions = DimensionPivot.getDimensionCols();
        List<Integer> Indexs = new ArrayList<>();
        for(int i=0;i<this.headers.size();i++){
            if(dimensions.contains(this.headers.get(i).toLowerCase().trim())
                    & !this.headers.get(i).equalsIgnoreCase("age_attained")
                    & !this.headers.get(i).equalsIgnoreCase("age_at_commencement")
                    & !this.headers.get(i).equalsIgnoreCase("insurance_age_attained ")
                    & !this.headers.get(i).equalsIgnoreCase("duration_year")
                    & !this.headers.get(i).equalsIgnoreCase("calendar_year")) {Indexs.add(i);}
        }
        return Indexs;
    } */

    private Integer getAgeIndex(List<String> headers){
        if(headers.contains("age_attained")) return headers.indexOf("age_attained");
        if(headers.contains("age_at_commencement")) return headers.indexOf("age_at_commencement");
        if(headers.contains("insurance_age_attained")) return headers.indexOf("insurance_age_attained");
        else return null;
    }

   private Boolean keyOrNot (int i) {
        return !headers.get(i).equalsIgnoreCase("age_attained")&&
                !headers.get(i).equalsIgnoreCase("age_at_commencement")&&
                !headers.get(i).equalsIgnoreCase("insurance_age_attained")&&
                !headers.get(i).equalsIgnoreCase("duration_year")&&
                !headers.get(i).equalsIgnoreCase("calendar_year")&&
                !headers.get(i).equalsIgnoreCase("data_line") &&
                !headers.get(i).equalsIgnoreCase("rate") &&
                !headers.get(i).equalsIgnoreCase("adjustment")&&
                !headers.get(i).equalsIgnoreCase("trend_adjustment");
   }

}
