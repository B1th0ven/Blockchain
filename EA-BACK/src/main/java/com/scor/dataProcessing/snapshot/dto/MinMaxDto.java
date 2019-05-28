package com.scor.dataProcessing.snapshot.dto;

public class MinMaxDto {
    String minReportingPeriod;
    String maxReportingPeirod;

    public String getMinReportingPeriod() {
        return minReportingPeriod;
    }

    public String getMaxReportingPeirod() {
        return maxReportingPeirod;
    }


    public MinMaxDto(String minReportingPeriod, String maxReportingPeirod) {
        this.minReportingPeriod = minReportingPeriod;
        this.maxReportingPeirod = maxReportingPeirod;
    }
}
