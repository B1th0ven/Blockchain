package com.scor.dataProcessing.common;


import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DimensionPivot implements Serializable {

	private static List<String> dimCols = null ;

	public static void getPivot()
	{

		if(dimCols == null) {

			dimCols = new ArrayList<String>();


			Path path = null;
			try {
				path = Paths.get( DimensionPivot.class.getClassLoader().getResource("data/pivot_dimensions.csv").toURI() );
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

			Stream<String> lines;
			try {
				lines = Files.lines(path,Charset.forName("Cp1252"));
				lines.forEach(line ->{
					if(!line.equals("")) {
						String[] arr = line.toLowerCase().split(";" , -1) ;
						//System.out.println(arr[3]);
						String name = arr[0].toLowerCase().trim() ;
						boolean is_mandatory = arr[7].contains("yes") ;
						if (is_mandatory)
						{
							dimCols.add(name) ;
						}
					}
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	public static List<String> getDimensionCols(){
		if(dimCols == null || dimCols.isEmpty()) {
			getPivot();
		}
		return dimCols;
	}
}
