package com.scor.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.persistance.entities.RefParentGroupEntity;
import com.scor.persistance.entities.RefScopeEntity;
import com.scor.persistance.repositories.RefScopeRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScopeService implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3856349072202863988L;
	@Autowired
    private RefScopeRepository rs;

    public List<RefScopeEntity> getAll()
    {
        List<RefScopeEntity> list = new ArrayList<RefScopeEntity>();

        for ( RefScopeEntity st : rs.findAll())
        {
            list.add(st);
        }
        return list;
    }


}
