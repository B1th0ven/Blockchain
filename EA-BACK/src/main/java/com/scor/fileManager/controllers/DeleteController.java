package com.scor.fileManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.scor.fileManager.entities.file;
import com.scor.fileManager.services.DeleteService;
import com.scor.fileManager.services.UploadService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DeleteController {

    @Autowired
    private DeleteService service;

    @RequestMapping(value="/file", method=RequestMethod.DELETE)
    public void delete(
            @RequestParam("path") String path
    ) throws Exception{
        service.delete(path);
    }
}
