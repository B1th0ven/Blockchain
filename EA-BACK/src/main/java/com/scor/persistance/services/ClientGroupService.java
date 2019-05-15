package com.scor.persistance.services;

import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.persistance.entities.RefParentGroupEntity;
import com.scor.persistance.entities.RefUltimateGroupEntity;
import com.scor.persistance.repositories.ClientGroupRepository;
import com.scor.persistance.repositories.UltimateGroupRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientGroupService  implements Serializable {
	
	public static final Logger LOGGER = Logger.getLogger(ClientGroupService.class);

	@Autowired
	private ClientGroupRepository repository;
	@Autowired 
	private UltimateGroupRepository ultimateGroupRepository;
	
//	private List<RefParentGroupEntity> clientGroupList = null;

		public List<RefParentGroupEntity> getAll() {
			return IteratorUtils.toList(repository.findAll().iterator());
		}


//	public List<RefParentGroupEntity> getAll() {
//		Date dateBegin = new Date();
////		if(this.clientGroupList != null) {
////			return this.clientGroupList;
////		}
//		List<String> codes = new ArrayList<>();
////		Iterable<RefParentGroupEntity> findAll = repository.findAll();
//
//		List<RefParentGroupEntity> ug = repository.findByRpgDescription("UG");
//		for (RefParentGroupEntity refParentGroupEntity : ug) {
//			codes.add(refParentGroupEntity.getRpgCode());
//		}
//		List<RefParentGroupEntity> pg = repository.findByRpgDescription(null);
//		for (RefParentGroupEntity st : pg) {
//			if(st.getRpgParentId() != null || codes.contains(st.getRpgCode())) {
//				continue;
//			}
//			ug.add(st);
//		}
////		this.clientGroupList = ug;
////		return this.clientGroupList;
//		Date dateEnd = new Date();
//        LOGGER.info("End getting all client groups in :" + (dateEnd.getTime()-dateBegin.getTime()) + " ms");
//		return ug;
//	}

	public RefParentGroupEntity findOrSaveByCode(String code, String name, String codeUltimateGroup,
			String nameUltimateGroup) {
		List<RefParentGroupEntity> parentGroups = repository.findByRpgCode(code);
		RefParentGroupEntity parentGroup = null;
		if(parentGroups != null && !parentGroups.isEmpty()) {
			parentGroup = parentGroups.get(0);
		}
		if (parentGroup != null
				&& (parentGroup.getRpgParentId() != null || !codeUltimateGroup.equalsIgnoreCase("[NULL]"))) {
			return parentGroup;
		}
		if (parentGroup == null) {
			parentGroup = new RefParentGroupEntity();
			parentGroup.setRpgCode(code);
			parentGroup.setRpgName(name);
		}
		if (!codeUltimateGroup.equalsIgnoreCase("[NULL]")) {
			RefParentGroupEntity ultimateGroup = repository.findByRpgCodeAndRpgDescription(codeUltimateGroup,"UG");

			if(ultimateGroup == null) {
				ultimateGroup = new RefParentGroupEntity();
				ultimateGroup.setRpgCode(codeUltimateGroup);
				ultimateGroup.setRpgName(nameUltimateGroup);
				ultimateGroup.setRpgDescription("UG");
			}
			parentGroup.setRpgParentId(ultimateGroup);
		}

		return repository.save(parentGroup);

	}
}
