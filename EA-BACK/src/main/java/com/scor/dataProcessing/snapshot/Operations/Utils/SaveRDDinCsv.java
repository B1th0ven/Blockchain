package com.scor.dataProcessing.snapshot.Operations.Utils;


import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SaveRDDinCsv implements Serializable {

    private static final long serialVersionUID = 588474985065223869L;
	private static final Logger LOGGER = Logger.getLogger(SaveRDDinCsv.class);

    public String saveCsv(Dataset<Row> m, String studyid , String datasetid ) throws IOException {
        String path = null;



        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh-mm-ss");
        String strDate = dateFormat.format(date);
        try {
        m.coalesce(1).write().mode(SaveMode.Overwrite).format("com.databricks.spark.csv").option("header", "true").option("delimiter", ";")
                .csv("files\\snapshot\\" + studyid  + "\\" + datasetid + strDate );
        LOGGER.info("donnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnne");
        }
        catch (Exception e) {
        	LOGGER.error("noooooo file");
		}
        try {

            path = displayCSV("files\\snapshot\\"  + studyid  + "\\" + datasetid + strDate );

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        String destination = "files\\snapshot\\" + studyid  + "\\" + datasetid + "\\" + strDate+ "policy.csv" ;

        File directory = new File("files\\snapshot\\" + studyid  + "\\" + datasetid );
        if(!directory.exists()) {

            directory.mkdir();

        }
        File file = new File(path);
        if(file.renameTo
                (new File(destination)))
        {
            file.delete();
            System.out.println("File moved successfully");
        }
        else
        {
            System.out.println("Failed to move the file");
        }

        File sparkJunk = new File("files\\snapshot\\"  + studyid  + "\\" + datasetid + strDate);

        for (File junk : sparkJunk.listFiles()){
            junk.delete();
        }
        sparkJunk.delete();

        return destination;
    }

    private static String displayCSV(String chemin) throws IOException {
        List<Path> k = new ArrayList<>();
        Files.newDirectoryStream(
                Paths.get(chemin),
                path -> path.toString().endsWith(".csv"))
                .forEach(l -> k.add(l));
        //Path path = Paths.get("/");
     //   if (!k.isEmpty()){
     Path   path = k.get(0);
    //}
        return path.toString();
    }




}
