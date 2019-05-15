//package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;
//
//import com.google.common.io.Resources;
//import com.scor.dataProcessing.dataChecker.DCFactory;
//import com.scor.dataProcessing.dataChecker.functionalChecker.*;
//
//import com.scor.dataProcessing.models.AffectedColumn;
//import com.scor.dataProcessing.models.ControlResults;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class Control38Test {
//
//    @Before
//    public void setUp() throws Exception {
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void validate() {
//
//        ControlResults result = DCFactory.getFunctionalChecker().runStudyFileChecker(Resources.getResource("data/TestDataPolicy.csv").getPath(),
//                Resources.getResource("data/TestDataproduct.csv").getPath(), "", "", "",
//                "", "", "", "", "", "", "");
//
//        for (AffectedColumn i : result.getControlResultsList().get(0).getAffectedColumns()) {
//            if ("Missing values_ckeck2".equalsIgnoreCase(i.getName()))
//                assertEquals(i.getErrorsNumber(), 15);
//
//
//        }
//    }}