package com.scor.persistance.services;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.scor.persistance.entities.RefCedentNameEntity;
import com.scor.persistance.entities.RefParentGroupEntity;
import com.scor.persistance.repositories.ClientGroupRepository;
import com.scor.persistance.repositories.ClientRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClientService  implements Serializable {
	
	private final static Logger LOGGER = Logger.getLogger(ClientService.class);

    @Autowired
    private ClientRepository repository;
    @Autowired
    private ClientGroupRepository clientGroupRepository;
    
//    private List<RefCedentNameEntity> clientsList = null;

	public Page<RefCedentNameEntity> findAll(Optional<Integer> pageNbr, Optional<Integer> size,
			Optional<String> searchCriteria) {

		Pageable page;
		if (pageNbr.isPresent() && size.isPresent()) {
			page = new PageRequest(pageNbr.get(), size.get(), new Sort(Direction.ASC, "rcnShortName"));
		} else {
			if (size.isPresent()) {
				page = new PageRequest(0, size.get(), new Sort(Direction.ASC, "rcnShortName"));
			} else {
				if (pageNbr.isPresent()) {
					page = new PageRequest(pageNbr.get(), 30, new Sort(Direction.ASC, "rcnShortName"));
				} else {
					page = new PageRequest(0, 30, new Sort(Direction.ASC, "rcnShortName"));
				}
			}
		}

		Date dateBegin = new Date();
		LOGGER.info("Begin getting all clients");
		Page<RefCedentNameEntity> list;
		if (!searchCriteria.isPresent()) {
			list = repository.findAll(page);
		} else {
			list = repository.findByRcnShortName(searchCriteria.get() , page) ; 
		}
		Date dateEnd = new Date();

		LOGGER.info("End getting all clients in :" + (dateEnd.getTime() - dateBegin.getTime()) + " ms");
		return list;
	}

    public List<RefCedentNameEntity> getAll()
    {
//    	if(this.clientsList != null) {
//    		return this.clientsList;
//    	}
    	Date dateBegin = new Date();
    	LOGGER.info("Begin getting all clients");
    	
        List<RefCedentNameEntity> list = new ArrayList<RefCedentNameEntity>();
        Map<String, List<RefParentGroupEntity>> pgsByCode = new HashMap<>();
        Map<Integer, RefParentGroupEntity> pgByCode = new HashMap<>();
        for ( RefCedentNameEntity st : repository.findAll())
        {
        	if (st.getRcnRpgId() != null) {
        		RefParentGroupEntity parentGroup = new RefParentGroupEntity();
        		if(pgByCode.containsKey(st.getRcnRpgId())) {
        			parentGroup = pgByCode.get(st.getRcnRpgId());
        		} else {
        			parentGroup = clientGroupRepository.findOne(st.getRcnRpgId());
        			pgByCode.put(st.getRcnRpgId(), parentGroup);
        		}
				if (parentGroup != null && parentGroup.getRpgParentId() != null) {
					st.setRcnRpgId(parentGroup.getRpgParentId().getRpgId());
				} else if(parentGroup != null && parentGroup.getRpgParentId() == null ) {
					List<RefParentGroupEntity> parentGroups = new ArrayList<>();
					if(pgsByCode.containsKey(parentGroup.getRpgCode())) {
						parentGroups = pgsByCode.get(parentGroup.getRpgCode());
					} else {
						parentGroups = clientGroupRepository.findByRpgCode(parentGroup.getRpgCode());
						pgsByCode.put(parentGroup.getRpgCode(), parentGroups);
					}
					for (RefParentGroupEntity pg : parentGroups) {
						if(StringUtils.isNotBlank(pg.getRpgDescription()) && pg.getRpgDescription().toLowerCase().equals("ug")) {
							st.setRcnRpgId(pg.getRpgId());
							break;
						}
					}
				}
			}

			list.add(st);
        }
        repository.save(list);
        Date dateEnd = new Date();
        LOGGER.info("End getting all clients in :" + (dateEnd.getTime()-dateBegin.getTime()) + " ms");
//        this.clientsList = list;
        return list;
    }

    public List<RefCedentNameEntity> getClientsByParentGroup(int parentId )
    {
    	RefParentGroupEntity ug = clientGroupRepository.findOne(parentId);
    	List<RefParentGroupEntity> pgsss = clientGroupRepository.findByRpgCode(ug.getRpgCode());
    	List<RefParentGroupEntity> pgs = clientGroupRepository.findByRpgParentId(ug);
        List<RefCedentNameEntity> list = new ArrayList<RefCedentNameEntity>();
        if(pgsss != null && pgsss.size()>1) {
        	for (RefParentGroupEntity pg : pgsss) {
				if(pg.getRpgId() != parentId) {
					for ( RefCedentNameEntity st : repository.findByRcnRpgId(pg.getRpgId()))
		            {
		        		st.setRcnRpgId(parentId);
		                list.add(st);
		            }
				}
			}
        }
    	if(pgs == null || pgs.isEmpty()) {
    		for ( RefCedentNameEntity st : repository.findByRcnRpgId(ug.getRpgId()))
            {
        		st.setRcnRpgId(parentId);
                list.add(st);
            }
    		return list;
    	}
    	
        for (RefParentGroupEntity pg : pgs) {
        	for ( RefCedentNameEntity st : repository.findByRcnRpgId(pg.getRpgId()))
            {
        		st.setRcnRpgId(parentId);
                list.add(st);
            }
		}
        
        return list;
    }
    
	 public RefCedentNameEntity getClientById( int id) {
		 return repository.findByRcnId(id); 
	 }
}
