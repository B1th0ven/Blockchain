package com.scor.dataProcessing.common;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import com.scor.dataProcessing.models.PivotCol;

public class ExpTablePivot {
    private static  List<String> compCols_base = null ;
    private static  List<PivotCol> pivotCols_base = null ;
    private static  List<String> compCols_adj = null ;
    private static  List<PivotCol> pivotCols_adj = null ;
    private static  List<String> compCols_trend = null ;
    private static  List<PivotCol> pivotCols_trend = null ;
    private static  List<String> compCols_calibration = null ;
    private static  List<PivotCol> pivotCols_calibration = null ;
    private static  List<String> compCols_policy = null ;
    private static  List<PivotCol> pivotCols_policy = null ;




    private static  List<PivotCol> getPivot(String exptype ) {


        List<PivotCol> pivotCols = new ArrayList<PivotCol>() ;


            Stream<String> lines = null ;
            try {
                lines = Files.lines( Paths.get( DataPivot.class.getClassLoader().getResource("data/pivot_exptable_"+exptype+".csv").toURI()));

                lines.forEach(line ->{
                    if(!line.equals("")) {

                        String[] arr = line.toLowerCase().split(";" , -1) ;
                        //System.out.println(Arrays.toString(arr));
                        String name = arr[0].toLowerCase().trim() ;
                        String type = arr[2].toLowerCase().trim() ;
                        boolean is_mandatory = arr[3].contains("yes") ;
                        List<String> possiblesValues = new ArrayList<>();
                        if(StringUtils.isNotBlank(arr[4]))
                        	possiblesValues = Arrays.asList(arr[4].trim().toLowerCase().split("\\/",-1)) ;
                        pivotCols.add(new PivotCol(name, type, is_mandatory, possiblesValues)) ;
                    }
                });
            } catch (IOException | URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                return pivotCols;
            }

    }

    private static  List<String> getCompPivot(List<PivotCol> pivotCols) {
        List<String> compCols = new ArrayList<>() ;
        for (PivotCol pcol : pivotCols) {
            if(pcol.isMandatory()) compCols.add(pcol.getName()) ;
        }
        return compCols;
    }

    public static List<String> getCompCols(String type) throws Exception {
        switch (type.trim().toLowerCase()){
            case "base":
                if(compCols_base == null) {
                    compCols_base = new ArrayList<String>();
                    compCols_base = getCompPivot(getPivotCols(type));
                }
                return compCols_base;
            case "trend":
                if(compCols_trend== null) {
                    compCols_trend = new ArrayList<String>();
                    compCols_trend = getCompPivot(getPivotCols(type));
                }
                return compCols_trend ;
            case "adjustment":
                if(compCols_adj == null) {
                    compCols_adj = new ArrayList<String>();
                    compCols_adj = getCompPivot(getPivotCols(type));
                }
                return compCols_adj;
            case "calibration":
                if(compCols_calibration == null) {
                	compCols_calibration = new ArrayList<String>();
                	compCols_calibration = getCompPivot(getPivotCols(type));
                }
                return compCols_calibration;
            case "policy":
                if(compCols_policy == null) {
                	compCols_policy = new ArrayList<String>();
                	compCols_policy = getCompPivot(getPivotCols(type));
                }
                return compCols_policy;
            default:
                throw new Exception("Invalid Type Of File");
        }
    }

    public static List<PivotCol> getPivotCols(String type) throws Exception {
        switch (type.trim().toLowerCase()){
            case "base":
                if(pivotCols_base == null) {
                    pivotCols_base = new ArrayList<PivotCol>();
                    pivotCols_base = getPivot(type);
                }
                return pivotCols_base;
            case "trend":
                if(pivotCols_trend== null) {
                    pivotCols_trend = new ArrayList<PivotCol>();
                    pivotCols_trend = getPivot(type);
                }
                return pivotCols_trend;
            case "adjustment":
                if(pivotCols_adj == null) {
                    pivotCols_adj = new ArrayList<PivotCol>();
                    pivotCols_adj = getPivot(type);
                }
                return pivotCols_adj;
            case "calibration":
                if(pivotCols_calibration == null) {
                    pivotCols_calibration = new ArrayList<PivotCol>();
                    pivotCols_calibration = getPivot(type);
                }
                return pivotCols_calibration;
            case "policy":
                if(pivotCols_policy == null) {
                	pivotCols_policy = new ArrayList<PivotCol>();
                	pivotCols_policy = getPivot(type);
                }
                return pivotCols_policy;
             default:
                 throw new Exception("Invalid Type Of File");
        }

    }
}
