package com.scor.ea.omega.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.ea.omega.entities.CedentNameEntity;
import com.scor.ea.omega.entities.CountryEntity;
import com.scor.ea.omega.entities.ParentGroupEntity;
import com.scor.ea.omega.repository.CedentNameRepository;
import com.scor.ea.omega.repository.ParentGroupRepository;
import com.scor.ref_client.schemas._1.FindClientByIdResponse;

@Service
public class RefCedentNameService {

	private static final Logger LOGGER = Logger.getLogger(RefCedentNameService.class);

	@Autowired
	private ScorClientService scorClientService;

	@Autowired
	private RefCountryService countryService;

	@Autowired
	private CedentNameRepository cedentNameRepository;
	
	@Autowired
	private ParentGroupRepository parentGroupRepository;

	public void injectClient(String batchDate) {
		Date dateBegin = new Date();
		LOGGER.info("Begin synchronizing Omega WS Clients");
		try {
			List<FindClientByIdResponse> clients = scorClientService.scorClientsInfo(batchDate);
			Map<String, CountryEntity> countryMap = countryService.getAll();
			List<CedentNameEntity> cedentNames = new ArrayList<>();
			clients.forEach(client -> {
				try {
					CedentNameEntity cedentNameEntity = mappingWsToEntity(client, countryMap);
					cedentNames.add(cedentNameEntity);
					cedentNameRepository.save(cedentNameEntity);
					LOGGER.info("Saving Clinet : " + client.getCliNf());
				} catch (Exception e) {
					LOGGER.error("Error while saving client : " + client.getCliNf());
				}
			});
//			cedentNameRepository.saveAll(cedentNames);
			LOGGER.info("Synchronizing Omega WS Clients Finished Successfully ("+ cedentNames.size()+")");
		} catch (Exception e) {
			LOGGER.error("Synchronizing Omega WS Clients Finished with errors" + e.getMessage());
		} finally {
			Date dateEnd = new Date();
			LOGGER.info("End Synchronizing Omega WS Clients in : " + (dateEnd.getTime() - dateBegin.getTime()));
		}
	}

	public CedentNameEntity mappingWsToEntity(FindClientByIdResponse client, Map<String, CountryEntity> countryMap) {
		List<CedentNameEntity> cedentNameEntities = cedentNameRepository.findByCode(String.valueOf(client.getCliNf()));
		CedentNameEntity cedentNameEntity = new CedentNameEntity();
		if (cedentNameEntities != null && cedentNameEntities.size() > 0)
			cedentNameEntity = cedentNameEntities.get(0);
		cedentNameEntity.setName(client.getCliShoNamLd());
		cedentNameEntity.setCode(String.valueOf(client.getCliNf()));
		cedentNameEntity.setCommercialClientName((client.getCliIlgst1La() != null ? client.getCliIlgst1La() + " " : "")
				+ (client.getCliIlgst2La() != null ? client.getCliIlgst2La() : ""));
		String countryCode = client.getCliCtyCf();
		CountryEntity country = countryService.findCountryByCode(countryCode, countryMap);
		cedentNameEntity.setDescription("omega");
		if (country == null) {
			// TODO saving country
			// TODO Country short name ?????!!!!
		}
		cedentNameEntity.setCountry(country);
		//Parent Group
		//code = hldNf
		//Name = hldNam1Ld
		if (client.getHldNf() != null) {
			List<ParentGroupEntity> parentGroup = parentGroupRepository.findByCode(String.valueOf(client.getHldNf()));
			LOGGER.info("Searching group");
			ParentGroupEntity parentGroupEntity = null;
			if (parentGroup == null || parentGroup.size() < 1) {
				ParentGroupEntity group = new ParentGroupEntity();
				group.setCode(String.valueOf(client.getHldNf()));
				group.setName(client.getHldNam1Ld());
				LOGGER.info("saving Parent group : " + client.getHldNf() + " name : " + client.getHldNam1Ld());
				parentGroupEntity = parentGroupRepository.save(group);
			} else {
				parentGroupEntity = parentGroup.get(0);
				LOGGER.info("Parent group found  : " + parentGroup.get(0).getId());
			}
			cedentNameEntity.setParentGroup(parentGroupEntity);
		}
		return cedentNameEntity;
	}

}
