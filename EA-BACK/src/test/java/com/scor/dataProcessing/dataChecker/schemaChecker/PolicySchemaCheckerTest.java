package com.scor.dataProcessing.dataChecker.schemaChecker;

import com.google.common.io.Resources;
import com.scor.dataProcessing.dataChecker.DCFactory;
import com.scor.dataProcessing.models.ControlResults;
import org.junit.Test;
import com.scor.dataProcessing.dataChecker.schemaChecker.PolicySchemaChecker ;

import java.util.List;

import static org.junit.Assert.*;

public class PolicySchemaCheckerTest {

    @Test
    public void run1() {
        runproduct("data/Policy.csv");
    }

    @Test
    public void getInstance() {

    }

    @Test
    public void run() {
        List<List<String>> result = DCFactory.getSchemaChecker().run(Resources.getResource("data/Policy.csv").getPath() ,
               "fff", "policy", "","");

        List<List<String>> k = PolicySchemaChecker.getInstance().run(Resources.getResource("data/Policy.csv").getPath(), "combine"
                );


       // if( type.equalsIgnoreCase("combine")) {
            List cols =   result.get(0) ;
            assertEquals( cols.size(), 40);
       // } else {
          //  List cols =   result.get(0) ;
          //  assertEquals( cols.size(), 40);
        //}

      List missingcols =   result.get(1) ;
        assertEquals( missingcols.size(), 0);
      List ignoredCols =   result.get(2) ;
        assertEquals( ignoredCols.size(), 0);
      List duplicatedCols =   result.get(3) ;
        assertEquals( duplicatedCols.size(), 0);



    }
    public void runproduct(String path ) {
       // List<List<String>> result = DCFactory.getSchemaChecker().run(Resources.getResource("data/Policy.csv").getPath() ,
              //  "fff", "policy", "","");

        List<List<String>> result = ProductSchemaCheker.getInstance().run(Resources.getResource(path).getPath()
        );


            List cols =   result.get(0) ;

            assertEquals( cols.size(), 2);


        List missingcols =   result.get(1) ;
        assertEquals( missingcols.size(), 1);
        List ignoredCols =   result.get(2) ;
        assertEquals( ignoredCols.size(), 38);
        List duplicatedCols =   result.get(3) ;
        assertEquals( duplicatedCols.size(), 0);



    }
}