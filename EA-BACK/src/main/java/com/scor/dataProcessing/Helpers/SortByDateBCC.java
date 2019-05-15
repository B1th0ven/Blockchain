package com.scor.dataProcessing.Helpers;

import org.apache.spark.api.java.function.Function;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SortByDateBCC implements Function<String, Long> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5957921383672178668L;
	private List<String> names;

    public SortByDateBCC(List<String> names){
        this.names = names;
    }

    @Override
    public Long call(String line) throws Exception {

        String sdate = "";
        if (0 <= names.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)
                && names.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION) <= line.split(";").length - 1) {
            sdate = line.split(";")[names.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)].trim();
        }
        if (!sdate.isEmpty() && sdate.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(sdate);
            return date.getTime();
        } else
        {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse("31/12/2900");
            return date.getTime();
        }
    }
}
