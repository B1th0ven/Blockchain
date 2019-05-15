package com.scor.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.persistance.entities.RefTreatyEntity;
import com.scor.persistance.repositories.TreatyRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class TreatyService implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -3670148800911184930L;
	@Autowired
    private TreatyRepository treatyRepository;

    public List<RefTreatyEntity> getAllTreaties()
    {
        List<RefTreatyEntity> list = new ArrayList<RefTreatyEntity>();

        for ( RefTreatyEntity st : treatyRepository.findAll())
        {
            list.add(st);
        }
        return list;
    }

    public List<RefTreatyEntity> getAllTreatiesByClientId(int id) {
        List<RefTreatyEntity> list = new ArrayList<RefTreatyEntity>();

        for ( RefTreatyEntity st : treatyRepository.findByRtRcnId(id))
        {
            list.add(st);
        }
        return list;
    }

	public List<RefTreatyEntity> getTreatiesByName(String key) {
//		List<RefTreatyEntity> list = new ArrayList<RefTreatyEntity>();
//
//        for ( RefTreatyEntity st : treatyRepository.findByRtNameContaining(key))
//        {
//            list.add(st);
//        }
//        return list;
		return treatyRepository.findByRtNameContaining(key);
	}
}
