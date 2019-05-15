package com.scor.dataProcessing.bulkInsert.resultdata.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.microsoft.azure.sqldb.spark.bulkcopy.BulkCopyMetadata;
import com.microsoft.azure.sqldb.spark.config.Config;
import com.microsoft.azure.sqldb.spark.connect.DataFrameFunctions;
import com.scor.dataProcessing.bulkInsert.config.ResultProperties;
import com.scor.dataProcessing.bulkInsert.resultdata.operations.MapStringToRow;
import com.scor.dataProcessing.common.RefResultColumn;
import com.scor.dataProcessing.common.ResultColumnPivot;
import com.scor.dataProcessing.sparkConnection.Connection;

import scala.Predef;
import scala.collection.JavaConverters;
import scala.collection.immutable.Map;

@Service
public class ResultService implements Serializable {

    private static final int BUFFER_SIZE = 16 * 1024 * 1024;
	private static final int DECIMAL_SCALE_12 = 12;
	private static final int DECIMAL_PRECISION_38 = 38;
    private static final int STRING_PRECISION_50 = 50;

	/**
     *
     */
    private static final long serialVersionUID = -4497730378028340930L;

    private final static Logger LOGGER = Logger.getLogger(ResultService.class);

    private java.util.Map<String, RefResultColumn> fixedSchema;

    private Object[] values;

    private BulkCopyMetadata metadata;

    private StructType structType;

    private Config config;

    private JavaSparkContext sc;

    private SparkSession sparkSession;

//    @Autowired
//    private ResultProperties resultProperties;
//
//    @Autowired
//    private RunSasService runSasService;
    
    @Value("${sas-shared-folder}")
    private String sas_shared_folder;



    public ResultService(ResultColumnPivot resultColumnPivot, ResultProperties resultProperties) {
        fixedSchema = resultColumnPivot.getResultColumn();
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
            DataType typeData = matchDataType(type);
            StructField field = DataTypes.createStructField(col, typeData, true);
            schema.add(field);
            int dbDataType = matchDbDataType(type);
            int precision = findPrecision(type);
            metadata.addColumnMetadata(refColumn.getIndex(), col, dbDataType, precision, DECIMAL_SCALE_12);
            switch (type) {
                case "string":
                    values[refColumn.getIndex() - 1] = null;
                    break;
                case "integer":
                    Integer v = null;
                    values[refColumn.getIndex() - 1] = v;
                    break;
                case "real":
                    Float r = null;
                    values[refColumn.getIndex() - 1] = r;
                    break;
                default:
                    break;
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
        map.put("dbTable", "dbo.RESULT_DATA");
        map.put("bulkCopyBatchSize", "200000");
        map.put("bulkCopyTableLock", "false");
        map.put("bulkCopyTimeout", "600");
        Map<String, Object> props = JavaConverters.mapAsScalaMapConverter(map).asScala().toMap(Predef.$conforms());
        config = new com.microsoft.azure.sqldb.spark.config.SqlDBConfigBuilder(props).build();

    }

    public void onStatusDone(int runId, int decId) throws Exception {
//        SmbFile sasFile = runSasService.download(runId, decId);
//        if (sasFile.exists()) {
//            double length = sasFile.length() / 1024;
//            LOGGER.info("Downloading Result file from sas server : " + sasFile.getPath() + " size = " + length + "Ko");
//            File eaFile = copySmbFileToDirectory(sasFile, resultProperties.workspace);
            try {
                insertResult(sas_shared_folder + runId + "/output/EA_result_" + runId + ".csv");
            } catch (Exception e) {
                LOGGER.error("An error occured while saving results for run : " + runId);
                LOGGER.error(e.getMessage());
                throw new Exception("An error occured while saving results for run : " + runId);
            }
//            eaFile.delete();
//        } else {
//            throw new Exception("File not found on sas server");
//        }
    }

    public void insertResult(String path) {
        LOGGER.info("Inserting Result File Begin");
        Date dateBegin = new Date();
        JavaRDD<String> data = sc.textFile(path);
        String schemaString = data.first();
        LOGGER.info("Load Files from :" + path);
//        Set<String> schemaNotFoundColumn = new HashSet<>();
        Function<String, Row> mapperStringToRow = new MapStringToRow(schemaString.toLowerCase(), values, fixedSchema);
        JavaRDD<Row> rowRDD = data.filter(s -> !s.equals(schemaString)).map(mapperStringToRow);
        Dataset<Row> dataFrame = sparkSession.createDataFrame(rowRDD, structType);
        LOGGER.info("Creating dataset to insert in Database : " + path);
        DataFrameFunctions<Row> dataFrameFunctions = new DataFrameFunctions<Row>(dataFrame);

        LOGGER.info("BEGIN INSERT IN BULK: " + path);
        dataFrameFunctions.bulkCopyToSqlDB(config, metadata,false);
        LOGGER.info("END INSERT IN BULK: " + path);
        Date dateEnd = new Date();

        LOGGER.info("Inserting Result File finished: " + path + " in : " + (dateEnd.getTime() - dateBegin.getTime()) + " ms");

    }

    private DataType matchDataType(String type) {
        switch (type) {
            case "string":
                return DataTypes.StringType;
            case "integer":
                return DataTypes.IntegerType;
            case "real":
                return DataTypes.FloatType;
            case "float":
                return DataTypes.createDecimalType(DECIMAL_PRECISION_38, DECIMAL_SCALE_12);

            default:
                break;
        }
        return null;
    }

    private int matchDbDataType(String type) {
        switch (type) {
            case "string":
                return java.sql.Types.VARCHAR;
            case "integer":
                return java.sql.Types.INTEGER;
            case "real":
                return java.sql.Types.REAL;
            case "float":
                return java.sql.Types.DECIMAL;

            default:
                break;
        }
        return 0;
    }

    private int findPrecision(String type) {
        switch (type) {
            case "string":
                return STRING_PRECISION_50;
            case "integer":
            case "real":
            case "float":
            default:
                return DECIMAL_PRECISION_38;
        }
    }

//	private static File copySmbFileToDirectory(SmbFile smbFile, String directoryPath) {
//		Date dateBegin = new Date();
//		LOGGER.info("Begin getting file from sas server");
//		if (smbFile == null) {
//			throw new RuntimeException("Invalid null File :" + smbFile);
//		}
//		File dir = new File(directoryPath);
//		if (!dir.exists()) {
//			dir.mkdir();
//		}
//		if (!dir.isDirectory()) {
//			throw new RuntimeException("Invalid directory :" + directoryPath);
//		}
//		String filePath = dir.getAbsolutePath() + File.separator + smbFile.getName();
//		File existantFile = new File(filePath);
//		if(existantFile.exists()) {
//			existantFile.delete();
//		}
//		try {
//			Path a = Paths.get(filePath);
//			InputStream inputStream = new SmbFileInputStream(smbFile);
//			Files.copy(inputStream, a);
//			inputStream.close();
//			Date dateEnd = new Date();
//			LOGGER.info("End getting file from sas server in " + (dateEnd.getTime() - dateBegin.getTime()) + " ms");
//			return new File(filePath);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

//    private static File copySmbFileToDirectory(SmbFile smbFile, String directoryPath) {
//        Date dateBegin = new Date();
//        LOGGER.info("Begin getting file from sas server");
//        if (smbFile == null) {
//            throw new RuntimeException("Invalid null File :" + smbFile);
//        }
//        File dir = new File(directoryPath);
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//        if (!dir.isDirectory()) {
//            throw new RuntimeException("Invalid directory :" + directoryPath);
//        }
//        String filePath = dir.getAbsolutePath() + File.separator + smbFile.getName();
//        File existantFile = new File(filePath);
//        if (existantFile.exists()) {
//            existantFile.delete();
//        }
//        FileOutputStream fileOutputStream = null;
//        InputStream fileInputStream = null;
//        try {
//            fileOutputStream = new FileOutputStream(filePath);
//            fileInputStream = smbFile.getInputStream();
//            byte[] buf = new byte[BUFFER_SIZE];
//            int len;
//            while ((len = fileInputStream.read(buf)) > 0) {
//                fileOutputStream.write(buf, 0, len);
//            }
//            Date dateEnd = new Date();
//            LOGGER.info("End getting file from sas server " + smbFile.getPath() + " in " + (dateEnd.getTime() - dateBegin.getTime()) + " ms");
//            return new File(filePath);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (fileInputStream != null) {
//                try {
//                    fileInputStream.close();
//                } catch (IOException e) {
//                    LOGGER.error("cannot close file input stream");
//                }
//            }
//            if (fileOutputStream != null) {
//                try {
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    LOGGER.error("cannot close file output stream");
//                }
//            }
//        }
//    }

}
