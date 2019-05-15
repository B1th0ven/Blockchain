package com.scor.persistance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scor.persistance.entities.RefDataSourceEntity;
import com.scor.persistance.services.DataSourceService;

import java.util.List;

@RestController
public class DataSourceController {

    @Autowired
    private DataSourceService service;

    @RequestMapping("/datasources")
    public List<RefDataSourceEntity> getSources()
    {
        return service.getAll();
    }
}
