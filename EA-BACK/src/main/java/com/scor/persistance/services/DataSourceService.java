package com.scor.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.persistance.entities.RefDataSourceEntity;
import com.scor.persistance.repositories.DataSourceRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataSourceService  implements Serializable {

    @Autowired
    private DataSourceRepository repository;

    public List<RefDataSourceEntity> getAll()
    {
        List<RefDataSourceEntity> list = new ArrayList<RefDataSourceEntity>();

        for ( RefDataSourceEntity st : repository.findAll())
        {
            list.add(st);
        }
        return list;
    }
}
