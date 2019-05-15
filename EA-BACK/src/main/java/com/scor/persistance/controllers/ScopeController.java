package com.scor.persistance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scor.persistance.entities.RefScopeEntity;
import com.scor.persistance.services.ScopeService;

import java.util.List;

@RestController
public class ScopeController {

    @Autowired
    private ScopeService ss;

    @RequestMapping(value="/scopes", method = RequestMethod.GET)
    public List<RefScopeEntity> getScopes()
    {
        return ss.getAll();

    }
}
