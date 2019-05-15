package com.scor.fileManager.controllers;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import com.scor.fileManager.entities.file;

@RestController
public class DownloadController {

    @Value("${custom.upload.path}")
    private String UPLOADED_FOLDER_BASE;

    @RequestMapping("/download/attached/{fileName:.+}")
    public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("fileName") String fileName, @RequestParam("type") Optional<String> type) throws Exception {

        String UPLOADED_FOLDER;
        if(type.isPresent()) {
            if(type.get().trim().equalsIgnoreCase("expected"))
            	UPLOADED_FOLDER = UPLOADED_FOLDER_BASE+"/expectedfiles/";
            else if(type.get().trim().equalsIgnoreCase("ibnr"))
            	UPLOADED_FOLDER = UPLOADED_FOLDER_BASE+"/ibnr/";
            else throw new Exception("Error : Type not recognized");
        }
        else
            UPLOADED_FOLDER = UPLOADED_FOLDER_BASE+"/attachedfiles/";

        //System.out.println();
        File file = new File(UPLOADED_FOLDER+fileName);

        if (file.exists()) {

            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);


            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));


            response.setContentLength((int) file.length());

            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            FileCopyUtils.copy(inputStream, response.getOutputStream());

        } else throw new Exception("File does not exist");
    }

    @RequestMapping("/checkfilepath/{fileName:.+}")
    public boolean isFileExist(@PathVariable("fileName") String fileName, @RequestParam("type") Optional<String> type) throws Exception {
        String Path;
        if(type.isPresent()){
            if(type.get().trim().equalsIgnoreCase("expected"))
                Path = UPLOADED_FOLDER_BASE+"/expectedfiles/";
            else if(type.get().trim().equalsIgnoreCase("ibnr"))
                Path = UPLOADED_FOLDER_BASE+"/ibnr/";
            else Path = UPLOADED_FOLDER_BASE+"/attachedfiles/";
        } else throw new Exception("Error : Type not recognized");

        File file = new File(Path+fileName);
        if(file.exists()) return true;
        else return false;
    }

    @RequestMapping("/download/{rep}/{fileName:.+}")
    public void downloadNda(HttpServletRequest request, HttpServletResponse response, @PathVariable("rep") String rep,
                                    @PathVariable("fileName") String fileName, @RequestParam("type") Optional<String> type) throws Exception {
    	String UPLOADED_FOLDER = null;
    	if(rep.equals("nda")) {
            UPLOADED_FOLDER = DownloadController.class.getClassLoader().getResource("NDA/").getPath();
    	} else if(rep.equals("pivots")){
            UPLOADED_FOLDER = DownloadController.class.getClassLoader().getResource("pivots/").getPath();
    	} else if(rep.equals("docs")){
            UPLOADED_FOLDER = DownloadController.class.getClassLoader().getResource("docs/").getPath();
        }

        //System.out.println();
        File file = new File(UPLOADED_FOLDER+fileName);

        if (file.exists()) {

            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);


            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));


            response.setContentLength((int) file.length());

            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            FileCopyUtils.copy(inputStream, response.getOutputStream());

        } else throw new Exception("File does not exist");
    }



}
