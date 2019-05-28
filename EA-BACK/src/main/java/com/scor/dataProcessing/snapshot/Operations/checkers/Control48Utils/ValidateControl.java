package com.scor.dataProcessing.snapshot.Operations.checkers.Control48Utils;

import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.snapshot.Operations.Utils.CheckReportingPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service

public class ValidateControl implements Serializable {

    public boolean TestControl(String[] row_arr, List<String> names, String reportingMax) {


        String variableTime;
        if (names.contains(Headers.REPORTING_MONTH)) {
            variableTime = Headers.REPORTING_MONTH;
        } else if (names.contains(Headers.REPORTING_YEAR)) {
            variableTime = Headers.REPORTING_YEAR;
        } else {
            variableTime = Headers.REPORTING_QUARTER;
        }


        if ((row_arr[names.indexOf(Headers.MAIN_RISK_TYPE)].equalsIgnoreCase("life") ||
                row_arr[names.indexOf(Headers.MAIN_RISK_TYPE)].equalsIgnoreCase("ci") ||
                row_arr[names.indexOf(Headers.MAIN_RISK_TYPE)].equalsIgnoreCase("tpd"))) {

            if (row_arr[names.indexOf(Headers.STATUS_END_CURRENT_CONDITION)].equalsIgnoreCase("active")) {


                if (!row_arr[names.indexOf(variableTime)].equalsIgnoreCase(reportingMax)) {


                    return false;

                }


            }
        }

        if (names.contains(Headers.CLAIM_PAYMENT_MODE)) {
        if ((row_arr[names.indexOf(Headers.MAIN_RISK_TYPE)].equalsIgnoreCase("di") ||
        row_arr[names.indexOf(Headers.MAIN_RISK_TYPE)].equalsIgnoreCase("ltc")) &&
        !row_arr[names.indexOf(Headers.CLAIM_PAYMENT_MODE)].equalsIgnoreCase("income")) {
            if (row_arr[names.indexOf(Headers.STATUS_END_CURRENT_CONDITION)].equalsIgnoreCase("active")) {

                if (!row_arr[names.indexOf(variableTime)].equalsIgnoreCase(reportingMax)) {

                    return false; 
                }

            }
        }}


        if (names.contains(Headers.CLAIM_PAYMENT_MODE)) {

        if ((row_arr[names.indexOf(Headers.MAIN_RISK_TYPE)].equalsIgnoreCase("di") ||
                row_arr[names.indexOf(Headers.MAIN_RISK_TYPE)].equalsIgnoreCase("ltc")) &&
                !row_arr[names.indexOf(Headers.CLAIM_PAYMENT_MODE)].equalsIgnoreCase("income")) {

            if (row_arr[names.indexOf(Headers.STATUS_END_CURRENT_CONDITION)].equalsIgnoreCase("active") ||
                    row_arr[names.indexOf(Headers.STATUS_END_CURRENT_CONDITION)].equalsIgnoreCase("claimant")
            ) {

                if (!row_arr[names.indexOf(variableTime)].equalsIgnoreCase(reportingMax)) {


                    return false;

                }


            }
        }
        }

        return true;
    }
}





