package com.scor.dataProcessing.dataChecker.functionalChecker;

import com.google.common.io.Resources;
import com.scor.dataProcessing.dataChecker.DCFactory;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResults;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class StudyFunctionalCheckerTest {

    @Test
    public void runStudyFileChecker() {
    }

    @Test
    public void runExpTableChecker() {
        runCalculator ("data/policy1.csv"
               ,"data/prod.csv");
    }


    public void runCalculator(String PathPolicy, String PathProcuct) {
        ControlResults result = DCFactory.getFunctionalChecker().runStudyFileChecker(Resources.getResource(PathPolicy).getPath() ,
                Resources.getResource(PathProcuct).getPath(), "split","08/04/2016","11/04/2019",
                "( ex effe vita)","unipolsai","","","ita","","");
int size_of_controls = result.getControlResultsList().size() ;
        //List list = new ArrayList();
        //list.add("object 1");

        for (int i = 0; i < size_of_controls; i++) {

        String control = result.getControlResultsList().get(i).getControl() ;
        if (("Date Comparison").equalsIgnoreCase(control)) {
            for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
            if ("date of commencement & date of event incurred".equalsIgnoreCase(j.getName()))
                assertTrue (j.getErrorsNumber()> 0);

            if ("date of commencement & benefit end date".equalsIgnoreCase(j.getName()))
                assertTrue (j.getErrorsNumber()> 0);

            if ("date of commencement & date of begin current condition".equalsIgnoreCase(j.getName()))
                assertTrue (j.getErrorsNumber()> 0);

            if ("date of event incurred & benefit end date".equalsIgnoreCase(j.getName()))
                assertTrue (j.getErrorsNumber()> 0);

            if ("start of observation period <= benefit end date".equalsIgnoreCase(j.getName()))
                assertTrue (j.getErrorsNumber()> 0);

            if ("start of observation period <= date of event incurred".equalsIgnoreCase(j.getName()))
                assertTrue (j.getErrorsNumber()> 0);

            if ("date of event incurred & date of event notified & date of event settled & date of event paid".equalsIgnoreCase(j.getName()))
                assertTrue (j.getErrorsNumber()> 0);
            if ("min age at commencement <= age at commencement <= max age at commencement".equalsIgnoreCase(j.getName()))
                assertTrue (j.getErrorsNumber()> 0);
            if ("product start date <= date of commencement <= product end date".equalsIgnoreCase(j.getName()))
                assertTrue (j.getErrorsNumber()> 0);
            if ("date of birth & date of commencement".equalsIgnoreCase(j.getName()))
                assertTrue (j.getErrorsNumber()> 0);
            }

        }
            if (("Date of Event = Date of End Current Condition").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("Date of Event = Date of End Current Condition".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);

                }}

            if (("Lump sum").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("when death / withdrawal (lump sum), risk amount = event amount".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);

                } }
            if (("Amount Comparison").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("min face amount <= risk amount reinsurer".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);

                }}

            if (("Missing Values Check Blocking").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("temp_add_extra_rating_term_2/temp_add_extra_rating_2".equalsIgnoreCase(j.getName()))
                        assertTrue(j.getErrorsNumber() > 0);
                }}

            if (("Events should be coherent with the main risk type and the acceleration risk type").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("Events should be coherent with the main risk type and the acceleration risk type".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);

                }}
            if (("Event Amount Reinsurer <= Event Amount Insurer").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("Event Amount Reinsurer <= Event Amount Insurer".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);
                }}
            if (("Consistent date of last medical selection").equalsIgnoreCase(control)) {

            for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                if ("Consistent date of last medical selection".equalsIgnoreCase(j.getName()))
                    assertTrue (j.getErrorsNumber()> 0);
                if ("Consistent date of last medical selection".equalsIgnoreCase(j.getName()))
                    assertTrue (j.getErrorsNumber()> 0);

            }}

            if (("Incidence_Death_Check").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("claim termination".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);

                }}
            if (("Claimant_Dead_Check").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("dead".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);
                    if ("claimant_dead".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);

                }}
            if (("Missing Values Check_2").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("date_of_birth/date_of_birth".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);
                    if ("age_at_commencement/age_at_commencement".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);
                    if ("date_of_Claim_Commencement/date_of_Claim_Commencement".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);
                    if ("age_at_commencement/age_at_commencement".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);

                }
            }
            if (("Checking if Product Id exists in product file").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("Product Id doesn't exists in product file".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);
                }}
            if (("Incidence_Death XOR Incidence/Death").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("status_end_current_condition/status_end_current_condition".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);
                } }
            if (("Consistent date of last medical selection").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("Consistent date of last medical selection".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);
                }}
            if (("Status coherent with event type").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("Type of Event & Status at End Current Condition".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);
                }}

            if (("Amount Comparison").equalsIgnoreCase(control)) {
                for (AffectedColumn j : result.getControlResultsList().get(i).getAffectedColumns()) {
                    if ("risk amount insurer <= max face amount".equalsIgnoreCase(j.getName()))
                        assertTrue (j.getErrorsNumber()> 0);
                }}


        }
            }


    @Test
    public void getInstance() {
    }

    @Test
    public void run() {
        ControlResults result = DCFactory.getFunctionalChecker().runStudyFileChecker(Resources.getResource("data/Policy.csv").getPath() ,
                Resources.getResource("data/prod.csv").getPath(), "split","08/04/2016","11/04/2019",
                "( ex effe vita)","unipolsai","","","ita","","");

        for (AffectedColumn i : result.getControlResultsList().get(0).getAffectedColumns()) {
            if ("date of commencement & date of event incurred".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);

            if ("date of commencement & benefit end date".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);

            if ("date of commencement & date of begin current condition".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);

            if ("date of event incurred & benefit end date".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);

            if ("start of observation period <= benefit end date".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);

            if ("start of observation period <= date of event incurred".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);

            if ("date of event incurred & date of event notified & date of event settled & date of event paid".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);
            if ("min age at commencement <= age at commencement <= max age at commencement".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 2);
            if ("product start date <= date of commencement <= product end date".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 2);
            if ("date of birth & date of commencement".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 13);

        }
        for (AffectedColumn i : result.getControlResultsList().get(1).getAffectedColumns()) {
            if ("Date of Event = Date of End Current Condition".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);

        }

        for (AffectedColumn i : result.getControlResultsList().get(2).getAffectedColumns()) {
            if ("when death / withdrawal (lump sum), risk amount = event amount".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);

        }
        for (AffectedColumn i : result.getControlResultsList().get(3).getAffectedColumns()) {
            if ("min face amount <= risk amount reinsurer".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 2);

        }

        for (AffectedColumn i : result.getControlResultsList().get(4).getAffectedColumns()) {
            if ("temp_add_extra_rating_term_2/temp_add_extra_rating_2".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);

        }
        for (AffectedColumn i : result.getControlResultsList().get(5).getAffectedColumns()) {
            if ("Events should be coherent with the main risk type and the acceleration risk type".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);

        }
        for (AffectedColumn i : result.getControlResultsList().get(6).getAffectedColumns()) {
            if ("Event Amount Reinsurer <= Event Amount Insurer".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);

        }
        /*
        for (AffectedColumn i : result.getControlResultsList().get(7).getAffectedColumns()) {
            if ("Consistent date of last medical selection".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 15);
            if ("Consistent date of last medical selection".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 1);

        }

         */
        for (AffectedColumn i : result.getControlResultsList().get(8).getAffectedColumns()) {
            if ("claim termination".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 16);

        }
        for (AffectedColumn i : result.getControlResultsList().get(9).getAffectedColumns()) {
            if ("dead".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 15);
            if ("claimant_dead".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 1);

        }
        for (AffectedColumn i : result.getControlResultsList().get(10).getAffectedColumns()) {
            if ("date_of_birth/date_of_birth".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 2);
            if ("age_at_commencement/age_at_commencement".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 1);

        }

        for (AffectedColumn i : result.getControlResultsList().get(11).getAffectedColumns()) {
            if ("Product Id doesn't exists in product file".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 13);
        }
        for (AffectedColumn i : result.getControlResultsList().get(12).getAffectedColumns()) {
            if ("status_end_current_condition/status_end_current_condition".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 1);
        }
        for (AffectedColumn i : result.getControlResultsList().get(13).getAffectedColumns()) {
            if ("Consistent date of last medical selection".equalsIgnoreCase(i.getName()))
                assertEquals(i.getErrorsNumber(), 1);
        }
    }






    @Test
    public void control21() {
    }

    @Test
    public void statusConsistencyCheck() {
    }

    @Test
    public void checkClientAndClientGroupAndTreaties() {
    }
}