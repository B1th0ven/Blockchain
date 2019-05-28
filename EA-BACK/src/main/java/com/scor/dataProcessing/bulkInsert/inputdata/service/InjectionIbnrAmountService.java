package com.scor.dataProcessing.bulkInsert.inputdata.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.stereotype.Service;

import com.microsoft.azure.sqldb.spark.bulkcopy.BulkCopyMetadata;
import com.microsoft.azure.sqldb.spark.config.Config;
import com.microsoft.azure.sqldb.spark.connect.DataFrameFunctions;
import com.scor.dataProcessing.bulkInsert.config.ResultProperties;
import com.scor.dataProcessing.bulkInsert.inputdata.operations.MapStringLineToRow;
import com.scor.dataProcessing.bulkInsert.inputdata.utils.InjectionUtils;
import com.scor.dataProcessing.common.RefResultColumn;
import com.scor.dataProcessing.common.ResultColumnPivot;
import com.scor.dataProcessing.sparkConnection.Connection;

import scala.Predef;
import scala.collection.JavaConverters;
import scala.collection.immutable.Map;

@Service
public class InjectionIbnrAmountService implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -5074105758837023057L;

    private static final int DECIMAL_SCALE_12 = 12;
	
    private final static Logger LOGGER = Logger.getLogger(InjectionIbnrAmountService.class);
	
	private java.util.Map<String, RefResultColumn> fixedSchema;

    private Object[] values;

    private BulkCopyMetadata metadata;

    private StructType structType;

    private Config config;

    private JavaSparkContext sc;

    private SparkSession sparkSession;
    
	public InjectionIbnrAmountService(ResultColumnPivot resultColumnPivot, ResultProperties resultProperties) throws Exception {
        fixedSchema = resultColumnPivot.getIbnrAmountPivot();
        Set<Entry<String, RefResultColumn>> a = fixedSchema.entrySet();
        String[] column = new String[fixedSchema.size()];
        for (Entry<String, RefResultColumn> entry : a) {
            column[entry.getValue().getIndex() - 1] = entry.getKey();
        }
        List<StructField> schema = new ArrayList<>();
        values = new Object[fixedSchema.size()];
        metadata = new BulkCopyMetadata();
        for (String col : column) {
            RefResultColumn refColumn = fixedSchema.get(col);
            String type = refColumn.getType();
            DataType typeData = InjectionUtils.matchDataType(type);
            StructField field = DataTypes.createStructField(col, typeData, true);
            schema.add(field);
            int dbDataType = InjectionUtils.matchDbDataType(type);
            int precision = InjectionUtils.findPrecision(type);
            metadata.addColumnMetadata(refColumn.getIndex(), col, dbDataType, precision, DECIMAL_SCALE_12);
            switch (type) {
                case "string":
                    values[refColumn.getIndex() - 1] = null;
                    break;
                case "integer":
                    Integer integer = null;
                    values[refColumn.getIndex() - 1] = integer;
                    break;
                case "date":
                    java.sql.Date date = null;
                    values[refColumn.getIndex() - 1] = date;
                    break;
                case "decimal":
                    BigDecimal decimal = null;
                    values[refColumn.getIndex() - 1] = decimal;
                    break;
                default:
                	throw new Exception("cannot initiate table");
            }
        }

        structType = DataTypes.createStructType(schema);

        sc = Connection.getContext();
        sparkSession = Connection.getSession();

        java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
        map.put("url", resultProperties.dbServer);
        map.put("databaseName", resultProperties.dbName);
        map.put("user", resultProperties.dbUser);
        map.put("password", resultProperties.dbPassword);
        map.put("dbTable", "dbo.IBNR_AMOUNT_INPUT_DATA");
        map.put("bulkCopyBatchSize", "200000");
        map.put("bulkCopyTableLock", "false");
        map.put("bulkCopyTimeout", "600");
        Map<String, Object> props = JavaConverters.mapAsScalaMapConverter(map).asScala().toMap(Predef.$conforms());
        config = new com.microsoft.azure.sqldb.spark.config.SqlDBConfigBuilder(props).build();
    }
	
	public void inject(String path, int runid) {
        LOGGER.info("Inserting IBNR Amount File Begin");
        Date dateBegin = new Date();
        JavaRDD<String> data = sc.textFile(path);
        String schemaString = data.first().toLowerCase();
        LOGGER.info("Load Files from :" + path);
        Function<String, Row> mapperStringToRow = new MapStringLineToRow(schemaString, values, fixedSchema,
        		runid);
        JavaRDD<Row> rowRDD = data.filter(s -> !s.toLowerCase().trim().contains(schemaString)).map(mapperStringToRow);
        Dataset<Row> dataFrame = sparkSession.createDataFrame(rowRDD, structType);
        LOGGER.info("Creating dataset to insert in Database : " + path);
        DataFrameFunctions<Row> dataFrameFunctions = new DataFrameFunctions<Row>(dataFrame);

        LOGGER.info("BEGIN INSERT IN BULK: " + path);
        dataFrameFunctions.bulkCopyToSqlDB(config, metadata,false);
        LOGGER.info("END INSERT IN BULK: " + path);
        Date dateEnd = new Date();

        LOGGER.info("Inserting IBNR Amount File finished: " + path + " in : " + (dateEnd.getTime() - dateBegin.getTime()) + " ms");

    }
}
