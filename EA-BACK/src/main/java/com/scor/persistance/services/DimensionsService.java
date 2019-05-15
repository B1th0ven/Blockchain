package com.scor.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.dataProcessing.common.DimensionPivot;
import com.scor.persistance.entities.RefLobEntity;
import com.scor.persistance.repositories.BusinessRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class DimensionsService implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6659594220358374570L;

	public List<String> getAll()
    {
        return DimensionPivot.getDimensionCols();
    }
}
