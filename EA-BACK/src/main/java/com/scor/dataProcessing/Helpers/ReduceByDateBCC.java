package com.scor.dataProcessing.Helpers;

import org.apache.spark.api.java.function.Function2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReduceByDateBCC implements Function2<String[], String[], String[]>{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4539463380133129117L;
    private static final String REGEX_DATE_FORMAT = "^[0-9]{2}/[0-9]{2}/[0-9]{4}$";
    private List<String> names;

    public ReduceByDateBCC(List<String> names){
        this.names = names;
    }

    @Override
    public String[] call(String[] row1, String[] row2) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date1;
        Date date2;
        if (names.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION) == -1
                || !row1[names.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)]
                .matches(REGEX_DATE_FORMAT))
            date1 = formatter.parse("01/01/1000");
        else
            date1 = formatter.parse(row1[names.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)]);

        if (names.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION) == -1
                || !row2[names.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)]
                .matches(REGEX_DATE_FORMAT))
            date2 = formatter.parse("01/01/1000");
        else
            date2 = formatter.parse(row2[names.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)]);

        if (date1.getTime() > date2.getTime())
            return row1;
        else
            return row2;

    }
}
