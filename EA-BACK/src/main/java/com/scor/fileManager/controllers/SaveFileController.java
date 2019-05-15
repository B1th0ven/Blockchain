package com.scor.fileManager.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scor.fileManager.services.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.HashMap;


@RestController
public class SaveFileController {

    @Autowired
    private FileService fileService;

}
