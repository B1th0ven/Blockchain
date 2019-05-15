package com.scor.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.persistance.entities.RefCalculationEngineTypeEntity;
import com.scor.persistance.repositories.CalculationEngineRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculationEngineService  implements Serializable {

    @Autowired
    private CalculationEngineRepository repository;

    public List<RefCalculationEngineTypeEntity> getAll()
    {
        List<RefCalculationEngineTypeEntity> list = new ArrayList<RefCalculationEngineTypeEntity>();

        for ( RefCalculationEngineTypeEntity st : repository.findAll())
        {
            list.add(st);
        }
        return list;
    }
}
