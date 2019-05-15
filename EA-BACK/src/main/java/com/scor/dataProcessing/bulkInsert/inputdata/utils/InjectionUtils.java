package com.scor.dataProcessing.bulkInsert.inputdata.utils;

import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;

public class InjectionUtils {
	
	private static final int DECIMAL_SCALE_12 = 12;
	private static final int DECIMAL_PRECISION_38 = 38;
    private static final int STRING_PRECISION_255 = 255;
	
	public static DataType matchDataType(String type) throws Exception {
        switch (type) {
            case "string":
                return DataTypes.StringType;
            case "integer":
                return DataTypes.IntegerType;
            case "date":
            	return DataTypes.DateType;
            case "decimal":
                return DataTypes.createDecimalType(DECIMAL_PRECISION_38, DECIMAL_SCALE_12);
            default:
            	throw new Exception("Type not found");
        }
    }

	public static int matchDbDataType(String type) throws Exception {
        switch (type) {
            case "string":
                return java.sql.Types.VARCHAR;
            case "integer":
                return java.sql.Types.INTEGER;
            case "date":
                return java.sql.Types.DATE;
            case "decimal":
                return java.sql.Types.DECIMAL;
            default:
            	throw new Exception("Type not found");
        }
    }

	public static int findPrecision(String type) throws Exception {
        switch (type) {
            case "string":
                return STRING_PRECISION_255;
            case "integer":
            case "date":
            case "decimal":
                return DECIMAL_PRECISION_38;
            default:
            	throw new Exception("Type not found");
        }
    }
}
