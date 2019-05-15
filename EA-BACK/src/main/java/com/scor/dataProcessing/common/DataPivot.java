package com.scor.dataProcessing.common;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.scor.dataProcessing.models.PivotCol;

public class DataPivot {
	
	private static String policyPivotFile = "data/pivot.csv" ; 	
	private static List<String> compCols = null ;
	private static List<PivotCol> pivotCols = null ;
	
	private static String productPivotFile= "data/pivotProd.csv" ; 
	private static List<String> compColsProduct = null ;
	private static List<PivotCol> pivotColsProduct = null ;
	
	public static void getPivot() {
		if(pivotCols == null) {
			
			pivotCols = new ArrayList<PivotCol>() ;
			

			Stream<String> lines = null ;
			try {
				lines = Files.lines( Paths.get( DataPivot.class.getClassLoader().getResource(policyPivotFile).toURI() ), Charset.forName("Cp1252"));

				lines.forEach(line ->{
					if(!line.equals("")) {
						System.out.println("-------------------pivot "+line);
						String[] arr = line.toLowerCase().split(";" , -1) ;
						//System.out.println(Arrays.toString(arr));
						String name = arr[0] ;
						String type = arr[4] ;
						boolean is_mandatory = arr[5].contains("yes") ;
						List<String> possiblesValues = Arrays.asList(arr[6].trim().split("\\/",-1));
						pivotCols.add(new PivotCol(name, type, is_mandatory, possiblesValues)) ;
					}
				});
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			compCols = new ArrayList<>() ;
			for (PivotCol pcol : pivotCols) {
				if(pcol.isMandatory()) compCols.add(pcol.getName()) ;
			}
		}
	}
	
	public static String getPolicyPivotFile() {
		return policyPivotFile;
	}

	public static void setPolicyPivotFile(String policyPivotFile) {
		DataPivot.policyPivotFile = policyPivotFile;
	}

	public static String getProductPivotFile() {
		return productPivotFile;
	}

	public static void setProductPivotFile(String productPivotFile) {
		DataPivot.productPivotFile = productPivotFile;
	}

	public static void getPivotProduct() {
		if(pivotColsProduct == null) {
			
			pivotColsProduct = new ArrayList<PivotCol>() ;
			
			Stream<String> lines;
			try {
//				lines = Files.lines( Paths.get(ClassLoader.getSystemResource("data/pivotProd.csv").toURI()) );
				lines = Files.lines( Paths.get( DataPivot.class.getClassLoader().getResource(productPivotFile).toURI() ) , Charset.forName("Cp1252"));
				
//				lines = Files.lines(Paths.get("/scor/data/expan/files/pivotProd.csv")) ;
				
				lines.forEach(line ->{
					if(!line.equals("")) {
						String[] arr = line.toLowerCase().trim().split(";" , -1) ;
						String name = arr[0] ;
						String type = arr[3] ;
						boolean is_mandatory = arr[4].contains("yes") ;
						List<String> possiblesValues = Arrays.asList(arr[5].split("\\/",-1)) ;
						pivotColsProduct.add(new PivotCol(name, type, is_mandatory, possiblesValues)) ;
					}
				});
			} catch (IOException | URISyntaxException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			compColsProduct = new ArrayList<>() ;
			for (PivotCol pcol : pivotColsProduct) {
				if(pcol.isMandatory()) compColsProduct.add(pcol.getName()) ;
			}
		}
	}

	public static List<String> getCompCols(){
		if(pivotCols == null) {
			getPivot();
		}
		return compCols;
	}
	
	public static List<String> getCompColsProduct(){
		if(pivotColsProduct == null) {
			getPivotProduct();
		}
		return compColsProduct;
	}

	
	public static List<PivotCol> getPivotCols(){
		if(pivotCols == null) {
			getPivot();
		}
		return pivotCols;
	}
	
	public static List<PivotCol> getPivotColsProduct(){
		if(pivotColsProduct == null) {
			getPivotProduct();
		}
		return pivotColsProduct;
	}
	

}
