package com.scor.fileManager.services;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scor.fileManager.entities.file;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FileService implements Serializable {

    private final static Logger LOGGER = Logger.getLogger(FileService.class);

    @Value("${custom.upload.path}")
    private String UPLOADED_FOLDER_BASE;

    private static final String FILE_DIRECTORY = "${file.directory}";
    @Value(FILE_DIRECTORY)
    private String fileDirectory;


    public List<file> getLoclaFiles(int st_id,String login) {

        File folder;
        folder = new File(UPLOADED_FOLDER_BASE + "/files/" + st_id + "/");
        if (!folder.exists())
            folder.mkdirs();
        //System.out.println(folder);
        File[] listOfFiles = folder.listFiles();
        List<file> res = scanFolder(listOfFiles,false);
         res.addAll(getUserFiles(login));
         return res;
    }
    
    public List<file> getLoclaFiles(String path) {

        File folder;
        folder = new File(UPLOADED_FOLDER_BASE + "/"+ path +"/");
        if (!folder.exists())
            folder.mkdirs();
        //System.out.println(folder);
        File[] listOfFiles = folder.listFiles();
        List<file> res = scanFolder(listOfFiles,false);
         return res;
    }

    public List<file> getUserFiles(String userId) {
        File folder;
        folder = new File(fileDirectory + "/" + userId.toUpperCase().trim() + "/");
        if (!folder.exists())
            folder.mkdirs();
        File[] listOfFiles = folder.listFiles();
        return scanFolder(listOfFiles,true);
    }

    private ArrayList<file> scanFolder(File[] listOfFiles,boolean isUser) {
        ArrayList<file> listFiles = new ArrayList<>();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String type = FilenameUtils.getExtension(listOfFiles[i].getName());
                    if (type.equals("csv") || type.equals("xlsx") || type.equals("xls")) {
                        file file = new file(
                                listOfFiles[i].getName(),
                                listOfFiles[i].getPath(),
                                (int) listOfFiles[i].length(),
                                type
                        );
                        if(isUser)
                        file.setUserFile(true);
                        listFiles.add(file);
                    }

                }
            }
        }
        return listFiles;
    }

    public file CopieFileToStudy(String Path,int stId) throws IOException  {
        file res = new file("","",0,"");
        File in = new File(Path);
        File folder = new File(UPLOADED_FOLDER_BASE + "/files/" + stId + "/");
        Date dateBegin = new Date();
        LOGGER.info("Begin Copying User File : "+in.getName()+" to Study : "+stId);
        if (!folder.exists())
            folder.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(Calendar.getInstance().getTime());
        String filename = in.getName();
        List<String> a = new ArrayList(Arrays.asList(filename.split("\\.",-1)));
        String ext = a.get(a.size() - 1);
        if ( a.size() >= 2 )
        {
            a.remove(a.size() - 1);
            filename = String.join("", a)+"_" + timeStamp + "." + ext ;
        }
        res.setType(ext);
        res.setPath(UPLOADED_FOLDER_BASE + "/files/" + stId + "/"+filename);
        res.setName(filename);

        File out = new File(UPLOADED_FOLDER_BASE + "/files/" + stId + "/"+filename);
        FileChannel inChannel = new
                FileInputStream(in).getChannel();
        FileChannel outChannel = new
                FileOutputStream(out).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(),
                    outChannel);
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
            Date dateEnd = new Date();
            LOGGER.info("Done Copying User File : "+in.getName()+" to Study : "+stId+" in "+(dateEnd.getTime()-dateBegin.getTime()));
        }

         return res;

    }

}
