package com.scor.dataProcessing.bulkInsert.resultdata.operations;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import com.scor.dataProcessing.common.RefResultColumn;

public class MapStringToRow implements Function<String, Row> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1753670859205561080L;
//	private String schemaString;
	private Object[] values;
	private java.util.Map<String, RefResultColumn> fixedSchema;
//	private Set<String> schemaNotFoundColumn;
	private String[] header;
	private static final Logger LOGGER = Logger.getLogger(MapStringToRow.class);

	public MapStringToRow(String schemaString, Object[] values, Map<String, RefResultColumn> fixedSchema) {
		super();
//		this.schemaString = schemaString;
		this.values = values;
		this.fixedSchema = fixedSchema;
//		this.schemaNotFoundColumn = schemaNotFoundColumn;
		this.header = schemaString.split(";");
	}

	@Override
	public Row call(String line) throws Exception {
//		Date dateBegin = new Date();
		String[] columnValues = line.split(";");
//		String[] header = schemaString.split(";");
		Object[] a = values.clone();
		int index = 0;
		for (String columnValue : columnValues) {
			RefResultColumn refColumn = fixedSchema.get(header[index]);
			index++;
			if (refColumn == null || StringUtils.isBlank(columnValue)) {
//				schemaNotFoundColumn.add(header[index - 1]);
				continue;
			}
//			if(StringUtils.isBlank(columnValue)) {
//				continue;
//			}
			switch (refColumn.getType()) {
			case "string":
				if(columnValue.length()>50) {
					LOGGER.warn("String value is trancated for column : " + refColumn.getColumn() + " / original value : " + columnValue);
					columnValue = columnValue.substring(0, 49);
				}
				a[refColumn.getIndex() - 1] = columnValue;
				break;
			case "integer":
				try {
					Integer v = Integer.parseInt(columnValue);
					a[refColumn.getIndex() - 1] = v;
				} catch (Exception e) {
					LOGGER.warn("cannot convert string to integer in column : " + refColumn.getColumn());
				}
				break;
			case "real":
				try {
					String real = columnValue.replace(",", ".");
					Float r = Float.valueOf(real);// .parseFloat(real);
					a[refColumn.getIndex() - 1] = r;
				} catch (Exception e) {
					LOGGER.warn("cannot convert string to real in column : " + refColumn.getColumn());
				}
				break;
			case "float":
				try {
					String real = columnValue.replace(",", ".");
					BigDecimal r = new BigDecimal(real);
					a[refColumn.getIndex() - 1] = r;
				} catch (Exception e) {
					LOGGER.warn("cannot convert string to decimal in column : " + refColumn.getColumn());
				}
			default:
				break;
			}

		}
		Row row = RowFactory.create(a);
//		Date dateEnd = new Date();
//		LOGGER.info("Line "+line+" is executed in : " + (dateEnd.getTime()-dateBegin.getTime()) + " ms");
		return row;
	}

}
