package com.scor.sas.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sas.iom.SASIOMDefs.GenericError;
import com.scor.persistance.entities.RunCalcEntity;
import com.scor.sas.exception.SasException;
import com.scor.sas.services.RunSasService;

import jcifs.smb.SmbFile;

@RestController
public class SasController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -597953709212258110L;
    @Autowired
    RunSasService rr;

    @RequestMapping(value = "/run/start", method = RequestMethod.POST)
    public RunCalcEntity run(HttpEntity<String> req)
            throws JsonParseException, JsonMappingException, IOException, SasException, GenericError, Exception {

        HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
        String id = (String) req_map.get("run_id");
        String dim = (String) req_map.get("Alldimensions");
        String dimensions = (String) req_map.get("dimensions");
        return rr.run(id, dim,dimensions);
    }

    @RequestMapping(value = "/run/status/{run_id}", method = RequestMethod.GET)
    public RunCalcEntity runStatus(@PathVariable int run_id) throws IOException {

        return rr.getRunById(run_id);
    }

    @RequestMapping("/run/download/{runid}")
    public void downloadresult(HttpServletRequest request, HttpServletResponse response, @PathVariable("runid") int id) throws Exception {

        File sFile = rr.downloadXlsx(id);
        if (!sFile.exists()) {
            sFile = rr.download(id);
            if (!sFile.exists()) throw new Exception("File does not exist");
        }

        String mimeType = "application/octet-stream";
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + sFile.getName() + "\""));
        response.setContentLength((int) sFile.length());
        FileCopyUtils.copy(new FileInputStream(sFile), response.getOutputStream());

    }

    @RequestMapping("/run/download/error/{runid}")
    public void downloaderror(HttpServletRequest request, HttpServletResponse response, @PathVariable("runid") int id) throws Exception {

        File sFile = rr.downloadError(id);

        String mimeType = "application/octet-stream";
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + sFile.getName() + "\""));
        response.setContentLength((int) sFile.length());
        FileCopyUtils.copy(new FileInputStream(sFile), response.getOutputStream());

    }

    @RequestMapping("/run/download/error/check/{runid}")
    public boolean isFileErrorExist(HttpServletRequest request, HttpServletResponse response, @PathVariable("runid") int id) throws Exception {
       return rr.isFileErrorExist(id);
    }


    @RequestMapping(value = "/run/update", method = RequestMethod.POST)
    public RunCalcEntity updateStatus(HttpEntity<String> req) throws Exception {
        System.out.println("Update Status run");
        System.out.println(req);
        HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
        String id = (String) req_map.get("rclcId");
        String newStatus = (String) req_map.get("rclcStatus");
        String Description = (String) req_map.get("rclcDescription");
        String User = (String) req_map.get("rclcUserCreation");
        String EndDate = (String) req_map.get("rclcEndDate");
        String step = (String) req_map.get("rclcStep");

        return rr.update(Integer.parseInt(id), newStatus, Description, User, EndDate, step);
    }

    @RequestMapping(value = "/run/test", method = RequestMethod.POST)
    public void testInsert(HttpEntity<String> req) throws Exception {
        System.out.println("Update Status run");
        System.out.println(req);
        HashMap<String, Object> req_map = new ObjectMapper().readValue(req.getBody(), HashMap.class);
        String path = (String) req_map.get("path");

        rr.testInsert(path);
    }
    
    @RequestMapping(value = "/run/habilitation", method = RequestMethod.GET)
    public void calculateHabilitations() throws IOException {
        rr.calculateHabilitations();
    }

}
