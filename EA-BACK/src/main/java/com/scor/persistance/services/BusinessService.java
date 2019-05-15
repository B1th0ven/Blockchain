package com.scor.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.persistance.entities.RefLobEntity;
import com.scor.persistance.repositories.BusinessRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessService implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8673241557633126707L;
	@Autowired
    private BusinessRepository repository;

    public List<RefLobEntity> getAll()
    {
        List<RefLobEntity> list = new ArrayList<RefLobEntity>();

        for ( RefLobEntity st : repository.findAll())
        {
            list.add(st);
        }
        return list;
    }
}
