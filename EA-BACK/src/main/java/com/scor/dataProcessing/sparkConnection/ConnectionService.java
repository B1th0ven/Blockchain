package com.scor.dataProcessing.sparkConnection;
//package com.scor.ea.spark;
//
//import java.io.Serializable;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.sql.SparkSession;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ConnectionService implements Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -2862513264309884578L;
//	
//	//A name for the spark instance. Can be any string
//		private static String appName = "EA";
//		//Pointer / URL to the Spark instance - embedded
//		private static String sparkMaster = "local[*]";
//		
//		private JavaSparkContext spContext = null;
//		private SparkSession sparkSession = null;
//		private static String tempDir = "file:///c:/temp/spark-warehouse";
//		
//		private void getConnection() {
//			
//			if ( spContext == null) {	
//				//Setup Spark configuration
//				SparkConf conf = new SparkConf()
//						.setAppName(appName)
//						.setMaster(sparkMaster);
//				
//				//Make sure you download the winutils binaries into this directory
//				//from https://github.com/srccodes/hadoop-common-2.2.0-bin/archive/master.zip
//				System.setProperty("hadoop.home.dir", "c:\\winutils\\");
//
//				//Create Spark Context from configuration
//				spContext = new JavaSparkContext(conf);
//				
//				 sparkSession = SparkSession
//						  .builder()
//						  .appName(appName)
//						  .master(sparkMaster)
//						  .config("spark.sql.warehouse.dir", tempDir)
//						  .getOrCreate();
//				 
//			}
//			
//		}
//		
//		public JavaSparkContext getContext() {
//			
//			if ( spContext == null ) {
//				getConnection();
//			}
//			return spContext;
//		}
//		
//		public SparkSession getSession() {
//			if ( sparkSession == null) {
//				getConnection();
//			}
//			return sparkSession;
//		}
//
//}
