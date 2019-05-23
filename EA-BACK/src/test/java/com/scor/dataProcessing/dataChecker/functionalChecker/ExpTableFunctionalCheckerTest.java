package com.scor.dataProcessing.dataChecker.functionalChecker;

import com.google.common.io.Resources;
import com.scor.dataProcessing.dataChecker.DCFactory;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResults;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import com.scor.dataProcessing.dataChecker.schemaChecker.ExpTableSchemaCheker;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
@Ignore
public class ExpTableFunctionalCheckerTest {

    @Test
    public void runStudyFileChecker() {
    }

  //  @Test
  public void runExpTableChecker(String PathPolicy) {
      HashMap<String ,Integer> maxValues = new HashMap<String ,Integer>();
      {
          maxValues.put("toto",1);

      }
      HashMap<String ,Integer> minValues = new HashMap<String ,Integer>();
      {

          minValues.put("titi",2);
      }

      ControlResults result = DCFactory.getFunctionalChecker().runExpTableChecker(Resources.getResource(PathPolicy).getPath() ,"split",maxValues,minValues);
        int size_of_controls = result.getControlResultsList().size() ;

        for (int i = 0; i < size_of_controls; i++) {
            String control = result.getControlResultsList().get(i).getControl() ;
            if (("Date Comparison").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("date of commencement & date of event incurred".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);

    }}}}

    @Test
    public void runCalculator() {
    }

    @Test
    public void getInstance() {
    }

    @Test
    public void run() {
    }

    @Test
    public void checkCombination() {
    }

    @Test
    public void getDimensionsIndexs() {

    }

    @Test
    public void runStudyFileChecker1() {
    }

    @Test
    public void runExpTableChecker1() {
    }

    @Test
    public void runCalculator1() {
    }

    @Test
    public void getInstance1() {
    }

    @Test
    public void run1() {
    }

    @Test
    public void checkCombination1() {
    }

    @Test
    public void getDimensionsIndexs1() {
        List  headers = new ArrayList();
        List<Integer>  list = ExpTableFunctionalChecker.getDimensionsIndexs(headers);
        assertEquals( list,list);
    }
}