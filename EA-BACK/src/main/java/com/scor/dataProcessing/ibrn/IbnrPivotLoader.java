package com.scor.dataProcessing.ibrn;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.scor.dataProcessing.common.DimensionPivot;

public class IbnrPivotLoader {

    static List<IbnrPivotEntity> ibnrUdf;
    static List<IbnrPivotEntity> ibnrAmount;
    static List<IbnrPivotEntity> ibnrAllocation;

    static public List<IbnrPivotEntity> getByName(String name) throws Exception {
       if (name.toLowerCase().equals("amount"))
           return getAmount();
       if (name.toLowerCase().equals("allocation"))
            return getAllocation();
       if (name.toLowerCase().equals("udf"))
            return getUdf();

       throw new Exception("No Pivot Found by the Name '" + name+"'");
    }

    static public List<IbnrPivotEntity> getUdf() throws Exception {
        if ( ibnrUdf == null )
            ibnrUdf = loadIbnrFile("ibnr/pivot_ibnr_udf.csv");
        return ibnrUdf;
    }

    static public List<IbnrPivotEntity> getAmount() throws Exception {
        if ( ibnrAmount == null )
            ibnrAmount = loadIbnrFile("ibnr/pivot_ibnr_amount.csv");
        return ibnrAmount;
    }

    static public List<IbnrPivotEntity> getAllocation() throws Exception {
        if ( ibnrAllocation == null )
            ibnrAllocation = loadIbnrFile("ibnr/pivot_ibnr_allocation.csv");
        return ibnrAllocation;
    }

    static private List<IbnrPivotEntity> loadIbnrFile(String ibnrFilePath) throws Exception {
        List<IbnrPivotEntity> pivots = new ArrayList<IbnrPivotEntity>();

        try
        {
            Path path = Paths.get(DimensionPivot.class.getClassLoader().getResource(ibnrFilePath).toURI());
            Stream<String> lines = Files.lines(path);
            List<List<String>> values = lines.skip(1).map(line -> Arrays.asList(line.split(";",-1))).collect(Collectors.toList());
            values.forEach(value -> {
                if ( value.size() > 5 )
                {
                    String  name = value.get(0).toLowerCase();
                    boolean unique = value.get(3).toLowerCase().contains("yes*");
                    boolean mandatory = unique || value.get(3).toLowerCase().contains("yes");
                    String type = value.get(2);
                    String regex = value.get(4).toLowerCase();
                    List<String> list = null;
                    int excludeId;
                    try{
                         excludeId = Integer.parseInt(value.get(6));
                    }catch (Exception e)
                    {
                         excludeId = 0;
                    }

                    pivots.add(new IbnrPivotEntity( name, mandatory, unique, type, regex ,list,excludeId));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Loading Stream Exception");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new Exception("No File Found with the path " + ibnrFilePath);
        }

        return pivots;
    }

}
