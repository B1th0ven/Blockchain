package com.scor.dataProcessing.bulkInsert.inputdata.operations;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import com.scor.dataProcessing.bulkInsert.resultdata.operations.MapStringToRow;
import com.scor.dataProcessing.common.RefResultColumn;

public class MapStringLineToRow implements Function<String, Row> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 680937661668940554L;
	
	private String schemaString;
	private Object[] values;
	private java.util.Map<String, RefResultColumn> fixedSchema;
	private int id;
	private static final Logger LOGGER = Logger.getLogger(MapStringToRow.class);

	public MapStringLineToRow(String schemaString, Object[] values, Map<String, RefResultColumn> fixedSchema,
			int id) {
		super();
		this.schemaString = schemaString;
		this.values = values;
		this.fixedSchema = fixedSchema;
		this.id = id;
	}

	@Override
	public Row call(String line) throws Exception {
		String[] columnValues = line.split(";");
		String[] header = schemaString.split(";");
		Object[] a = values.clone();
		int index = 0;
		a[0] = this.id;
		for (String columnValue : columnValues) {
			RefResultColumn refColumn = fixedSchema.get(header[index]);
			index++;
			if (refColumn == null) {
				continue;
			}
			if (StringUtils.isBlank(columnValue)) {
				continue;
			}
			switch (refColumn.getType()) {
			case "string":
				if (columnValue.length() > 255) {
					LOGGER.warn("String value is trancated for column : " + refColumn.getColumn()
							+ " / original value : " + columnValue);
					columnValue = columnValue.substring(0, 254);
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
			case "decimal":
				try {
					String real = columnValue.replace(",", ".");
					BigDecimal r = new BigDecimal(real);
					a[refColumn.getIndex() - 1] = r;
				} catch (Exception e) {
					LOGGER.warn("cannot convert string to decimal in column : " + refColumn.getColumn());
				}
				break;
			case "date":
				try {
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					Date parsed = format.parse(columnValue);
					java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
					a[refColumn.getIndex() - 1] = sqlDate;
				} catch (Exception e) {
					LOGGER.warn("cannot convert string to date in column : " + refColumn.getColumn());
				}
				break;
			default:
				break;
			}

		}
		return RowFactory.create(a);
	}
}
