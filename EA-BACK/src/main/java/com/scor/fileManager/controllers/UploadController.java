package com.scor.fileManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scor.fileManager.entities.file;
import com.scor.fileManager.services.UploadService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @RequestMapping(value="/upload/{id}", method=RequestMethod.POST)
    public file upload(HttpServletRequest request,@PathVariable("id") int st_id) {
        return uploadService.upload(request,"dataset",st_id);

    }

    @RequestMapping(value="/uploadattached", method=RequestMethod.POST)
    public file uploadattached(HttpServletRequest request) {
        return uploadService.upload(request,"attached",0);

    }

    @RequestMapping(value="/uploadIBNR", method=RequestMethod.POST)
    public file uploadIBNR(HttpServletRequest request) {
        return uploadService.upload(request,"ibnr",0);

    }

    @RequestMapping(value="/uploadEXP", method=RequestMethod.POST)
    public file uploadEXP(HttpServletRequest request) {
        return uploadService.upload(request,"expected",0);

    }

}
