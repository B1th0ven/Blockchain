package com.scor.dataProcessing.Helpers.filters;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.Function;

import com.scor.dataProcessing.Helpers.Headers;

public class FilterForClaimsExistance implements Function<String, Boolean> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4335666893749199026L;
	private static final String REGEX_DATE_FORMAT = "^[0-9]{2}/[0-9]{2}/[0-9]{4}$";
	private static List<String> names;
    private static int typeOfEventIndex;
    private static int dateOfEventIncurredindex;
    private static int dateOfBeginCurrentConditionIndex;
    private static int dateOfEndCurrentConditionIndex;
    private static String type;
    private static int exposureorEventindex;

    public FilterForClaimsExistance(List<String> names, String type){
    	this.names = names;
        this.typeOfEventIndex = names.indexOf(Headers.TYPE_OF_EVENT);
        this.dateOfEventIncurredindex = names.indexOf(Headers.DATE_OF_EVENT_INCURRED);
        this.dateOfBeginCurrentConditionIndex = names.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION);
        this.dateOfEndCurrentConditionIndex = names.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION);
        this.exposureorEventindex = names.indexOf(Headers.EXPOSURE_OR_EVENT);
        this.type = type;
    }

	@Override
	public Boolean call(String f) throws Exception {
		if (f.toLowerCase().trim().replace(";", "").equalsIgnoreCase(""))
			return false;
		if (f.toLowerCase().trim().equalsIgnoreCase(String.join(";", names)))
			return false;
		String[] line = f.toLowerCase().split(";",-1);
		if(exposureorEventindex >-1 && type.equals("split") && !(line[exposureorEventindex].equalsIgnoreCase("exposure") || line[exposureorEventindex].equalsIgnoreCase("exposure + event"))) {
			return false;
		}
//		if(typeOfEventIndex<0 || dateOfBeginCurrentConditionIndex<0 || dateOfEndCurrentConditionIndex<0 || dateOfEventIncurredindex<0) {
//			return false;
//		}
//		String[] arr = f.split(";",-1);
//		boolean toe = StringUtils.isNotBlank(arr[typeOfEventIndex])
//				&& (arr[typeOfEventIndex].equalsIgnoreCase("death")
//						|| arr[typeOfEventIndex].equalsIgnoreCase("incidence"));
//		if (!toe)
//			return false;
//		boolean dates = StringUtils.isNotBlank(arr[dateOfBeginCurrentConditionIndex])
//				&& arr[dateOfBeginCurrentConditionIndex].matches(REGEX_DATE_FORMAT)
//				&& StringUtils.isNotBlank(arr[dateOfEndCurrentConditionIndex])
//				&& arr[dateOfEndCurrentConditionIndex].matches(REGEX_DATE_FORMAT)
//				&& StringUtils.isNotBlank(arr[dateOfEventIncurredindex])
//				&& arr[dateOfEventIncurredindex].matches(REGEX_DATE_FORMAT);
//		return dates;
		return true;
	}
	
}
