package com.scor.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.persistance.entities.RefRequesterEntity;
import com.scor.persistance.repositories.ClientTypeRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientTypeService  implements Serializable {

    @Autowired
    private ClientTypeRepository clientRepository;

    public List<RefRequesterEntity> getAllStudies()
    {
        List<RefRequesterEntity> list = new ArrayList<RefRequesterEntity>();

        for ( RefRequesterEntity st : clientRepository.findAll())
        {
            list.add(st);
        }
        return list;
    }

    public RefRequesterEntity getClient(int id )
    {
        return clientRepository.findOne(id);
    }

    public void postClient( RefRequesterEntity st )
    {
        clientRepository.save( st );
    }
}
