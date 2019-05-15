package com.scor.ea.omega.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.ea.omega.entities.CedentNameEntity;
import com.scor.ea.omega.entities.CountryEntity;
import com.scor.ea.omega.entities.RefTreatyEntity;
import com.scor.ea.omega.repository.CedentNameRepository;
import com.scor.ea.omega.repository.TreatyRepository;
import com.scor.ea.salesfoce.entities.RefDataEntity;
import com.scor.ea.salesfoce.repository.RefDataRepository;
import com.scor.ref_client.schemas._1.FindClientByIdResponse;
import com.scor.ref_treaty.schemas._1.FindTtyByLastModifiedDateResponse;
import com.scor.ref_treaty.schemas._1.FindTtyHeaderByIdResponse;

@Service
public class RefTreatyService {

	private static final Logger LOGGER = Logger.getLogger(RefTreatyService.class);

	@Autowired
	private ScorTreatyService scorTreatyService;

	@Autowired
	private TreatyRepository treatyRepository;

	@Autowired
	private CedentNameRepository cedentNameRepository;
	
	@Autowired
	private RefDataRepository refDataRepository;
	
	@Autowired
	private ScorClientService scorClientService;
	
	@Autowired
	private RefCedentNameService cedentNameService;
	
	@Autowired
	private RefCountryService countryService;

	public void injectTreaties(String batchDate) {
		Date dateBegin = new Date();
		LOGGER.info("Begin synchronizing Omega WS Treaties");
		try {
			List<RefTreatyEntity> MappedTreaties = new ArrayList<>();
			FindTtyByLastModifiedDateResponse ttyIds = scorTreatyService.findTtyByLastModifiedDate(batchDate, null,
					null, null, null);
			Map<String, CountryEntity> countryMap = countryService.getAll();
			ttyIds.getCtrNf().stream().forEach(treaty -> {
				try {
					RefTreatyEntity treatyMapped = updateTreaty(treaty,countryMap);
					MappedTreaties.add(treatyMapped);
					treatyRepository.save(treatyMapped);
					LOGGER.info("Save Treaty : " + treaty);
				} catch (Exception e) {
					LOGGER.error("Error while saving treaty : " + treaty);
				}
			});
//			treatyRepository.saveAll(MappedTreaties);
			LOGGER.info("Synchronizing Omega WS Treaty Finished Successfully (" + MappedTreaties.size() + ")");
		} catch (Exception e) {
			LOGGER.error("Synchronizing Omega WS Treaty Finished with errors" + e.getMessage());
		} finally {
			Date dateEnd = new Date();
			LOGGER.info("End Synchronizing Omega WS Treaties in : " + (dateEnd.getTime() - dateBegin.getTime()));
		}
	}

	public RefTreatyEntity updateTreaty(String name, Map<String, CountryEntity> countryMap) {
		RefTreatyEntity treatyEntity = null;
		List<RefTreatyEntity> treatyEntities = treatyRepository.findByRtName(name);
		if (treatyEntities != null && treatyEntities.size() > 0)
			treatyEntity = treatyEntities.get(0);
		else
			treatyEntity = new RefTreatyEntity();
		FindTtyHeaderByIdResponse treatInfo = scorTreatyService.findTtyHeaderById(name);
		CedentNameEntity cedentNameEntity = getCedent(treatInfo.getCedNf(), treatInfo.getClishonamLd(),countryMap);
		treatyEntity.setRtName(name);
		if (cedentNameEntity != null) {
			int rcnId = cedentNameEntity.getRcnId();
			treatyEntity.setRtRcnId(rcnId);
		}
		extractRefData(treatInfo);
		
		return treatyEntity;
	}

	private void extractRefData(FindTtyHeaderByIdResponse treatInfo) {
		String subsidiaryCode = null;
		String subsidiaryName = null;
		if (treatInfo.getSsdCf() != 0 && treatInfo.getAccesbCf() != 0) {
			subsidiaryCode = treatInfo.getSsdCf() + "." + treatInfo.getAccesbCf();
		}
		if(StringUtils.isNotBlank(treatInfo.getEsbLl())) {
			subsidiaryName = treatInfo.getEsbLl();
		}
		if(StringUtils.isNotBlank(subsidiaryCode) && StringUtils.isNotBlank(subsidiaryName)) {
			List<RefDataEntity> refDataEntities = refDataRepository.findByTypeAndCodeAndName("subsidiary", subsidiaryCode, subsidiaryName);
			if(refDataEntities != null && refDataEntities.size()>0) {
				return;
			}
			RefDataEntity refData = new RefDataEntity();
			refData.setCode(subsidiaryCode);
			refData.setName(subsidiaryName);
			refData.setType("subsidiary");
			refDataRepository.save(refData);
		}
	}

	public CedentNameEntity getCedent(Integer code, String name, Map<String, CountryEntity> countryMap) {
		List<CedentNameEntity> cedents = cedentNameRepository.findByCode(String.valueOf(code));
		if (cedents != null && cedents.size() > 0) {
			LOGGER.info("Client found : " + code);
			return cedents.get(0);
		} else {
			try {
				FindClientByIdResponse client = scorClientService.findClientById(code);
				CedentNameEntity cedentName = cedentNameService.mappingWsToEntity(client, countryMap);
				
				return cedentNameRepository.save(cedentName);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	}

}
