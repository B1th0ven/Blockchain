package com.scor.dataProcessing.dataChecker.functionalChecker.Operations;

import com.scor.persistance.entities.ViewOmegaEntity;
import com.scor.persistance.repositories.ViewOmegaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OmegaData {

    public static List<ViewOmegaEntity> viewOmegaEntities = new ArrayList<>(

    );

    @Autowired
    ViewOmegaRepository viewOmegaRepository ;


    public OmegaData() {
    }

    public void updateReferencial(){
        viewOmegaEntities = (List<ViewOmegaEntity>) viewOmegaRepository.findAll();
    }
}
