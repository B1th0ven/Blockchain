package com.scor.persistance.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.persistance.entities.RefCedentNameEntity;
import com.scor.persistance.entities.RefCountryEntity;
import com.scor.persistance.entities.RefDataEntity;
import com.scor.persistance.entities.RefParentGroupEntity;
import com.scor.persistance.entities.RefTreatyEntity;
import com.scor.persistance.repositories.ClientGroupRepository;
import com.scor.persistance.repositories.ClientRepository;
import com.scor.persistance.repositories.CountryRepository;
import com.scor.persistance.repositories.RefDataRepository;
import com.scor.persistance.repositories.TreatyRepository;

@Service
public class RefDataService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1698688050286603669L;
	private static final Logger LOGGER = Logger.getLogger(RefDataService.class);
	
	@Autowired
	private RefDataRepository refDataRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ClientGroupRepository clientGroupRepository;

	@Autowired
	private TreatyRepository treatyRepository;

	@Autowired
	private CountryRepository countryRepository;

//	Map<String, List<String>> data = null;

	public Map<String, List<String>> getAll() {
//		if (data == null || data.isEmpty()) {
			Date dateBegin = new Date();
			LOGGER.info("Getting refData from database to controls");
			Map<String, List<String>> data = getAllFromDb();
			Date dateEnd = new Date();
			LOGGER.info("Getting refData from database to controls ends in : " + (dateEnd.getTime()-dateBegin.getTime()) + " ms");
//		}
		return data;
	}

	private Map<String, List<String>> getAllFromDb() {
		Iterable<RefDataEntity> refDataIt = refDataRepository.findAll();
		List<String> refDataSubsidiaryCode = new ArrayList<>();
		List<String> refDataSubsidiaryName = new ArrayList<>();
		List<String> refDataRetrocessionnaireCode = new ArrayList<>();
		List<String> refDataRetrocessionnaireName = new ArrayList<>();
		List<String> refDataDeals = new ArrayList<>();
		List<String> refDataCurrency = new ArrayList<>();
		List<String> refDataNaic = new ArrayList<>();
		List<String> refDataPortfolioOrigin = new ArrayList<>();
		List<String> refDataRegionOfResidence = new ArrayList<>();

		List<String> refDataClientId = new ArrayList<>();
		List<String> refDataClientShortName = new ArrayList<>();
		List<String> refDataClientGroupName = new ArrayList<>();
		List<String> refDataClientGroupCode = new ArrayList<>();
		List<String> refDataTreaty = new ArrayList<>();
		List<String> refDataCountry = new ArrayList<>();

		Iterable<RefCedentNameEntity> clientIt = clientRepository.findAll();
		clientIt.forEach(client -> {
			refDataClientId.add(client.getRcnCode().trim().toLowerCase());
			refDataClientShortName.add(client.getRcnShortName().trim().toLowerCase());
		});

		Iterable<RefParentGroupEntity> parentGroupIt = clientGroupRepository.findAll();
		parentGroupIt.forEach(clientGroup -> {
			if (clientGroup.getUltimateGroup() == null) {
				refDataClientGroupCode.add(clientGroup.getRpgCode().trim().toLowerCase());
				refDataClientGroupName.add(clientGroup.getRpgName().trim().toLowerCase());
			} else {
				refDataClientGroupCode.add(clientGroup.getUltimateGroup().getCode().trim().toLowerCase());
				refDataClientGroupName.add(clientGroup.getUltimateGroup().getName().trim().toLowerCase());
			}

		});
		Iterable<RefTreatyEntity> treatyIt = treatyRepository.findAll();
		treatyIt.forEach(treaty -> {
			refDataTreaty.add(treaty.getRtName().trim().toLowerCase());
		});

		Iterable<RefCountryEntity> countryIt = countryRepository.findAll();
		countryIt.forEach(country -> {
			refDataCountry.add(country.getRcCode().trim().toLowerCase());
		});
		refDataIt.forEach(refData -> {
			String code = refData.getCode();
			String name = refData.getName();
			switch (refData.getType()) {
			case "currency":
				refDataCurrency.add(code.trim().toLowerCase());
				break;
			case "naic":
				refDataNaic.add(code.trim().toLowerCase());
				break;
			case "subsidiary":
				String[] codeArr = code.split("\\.");
				if (codeArr[0].length() == 1) {
					codeArr[0] = "0" + codeArr[0];
				}
				if (codeArr[1].length() == 1) {
					codeArr[1] = "0" + codeArr[1];
				}
				refDataSubsidiaryCode.add(codeArr[0] + "-" + codeArr[1]);
				refDataSubsidiaryName.add(refData.getName().trim().toLowerCase());
				break;
			case "portfolio_origin":
				refDataPortfolioOrigin.add(code.trim().toLowerCase());
				break;
			case "deals" : 
				refDataDeals.add(code.trim().toLowerCase());
			case "retrocessionnaire":
				refDataRetrocessionnaireCode.add(code);
				refDataRetrocessionnaireName.add(refData.getName().trim().toLowerCase());
				break;
			case "region_of_residence" : 
				refDataRegionOfResidence.add(name.trim().toLowerCase());
			default:
				break;
			}
		});

		Map<String, List<String>> refData = new HashMap<>();
		refData.put("currency", refDataCurrency);
		refData.put("naic", refDataNaic);
		refData.put("legal_entity_code", refDataSubsidiaryCode);
		refData.put("legal_entity", refDataSubsidiaryName);
		refData.put("portfolio_origin", refDataPortfolioOrigin);
		refData.put("client_omega_code", refDataClientId);
		refData.put("client_risk_carrier_name", refDataClientShortName);
		refData.put("commercial_client_name", refDataClientShortName);
		refData.put("client_group", refDataClientGroupName);
		refData.put("client_group_omega_code", refDataClientGroupCode);
		refData.put("treaty_number_omega", refDataTreaty);
		refData.put("client_country", refDataCountry);
		refData.put("deal_id", refDataDeals);
		
		refData.put("client_omega_code", refDataClientId);
		refData.put("treaty_number_everest_no_amm", new ArrayList<>() );
		refData.put("country_of_residence", refDataCountry);
		refData.put("retro_legal_entity", refDataRetrocessionnaireName);
		refData.put("retro_legal_entity_omega_code", refDataRetrocessionnaireCode);
		refData.put("claim_currency", refDataCurrency);
		refData.put("region_of_residence", refDataRegionOfResidence);
		return refData;
	}
}
