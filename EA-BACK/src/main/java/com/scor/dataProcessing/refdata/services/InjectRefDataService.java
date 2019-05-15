package com.scor.dataProcessing.refdata.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.io.Resources;
import com.scor.persistance.entities.RefCedentNameEntity;
import com.scor.persistance.entities.RefCountryEntity;
import com.scor.persistance.entities.RefDataEntity;
import com.scor.persistance.entities.RefParentGroupEntity;
import com.scor.persistance.entities.RefTreatyEntity;
import com.scor.persistance.entities.RefUltimateGroupEntity;
import com.scor.persistance.entities.RefUserEntity;
import com.scor.persistance.repositories.ClientGroupRepository;
import com.scor.persistance.repositories.ClientRepository;
import com.scor.persistance.repositories.RefDataRepository;
import com.scor.persistance.repositories.TreatyRepository;
import com.scor.persistance.repositories.UltimateGroupRepository;
import com.scor.persistance.repositories.UserRepository;
import com.scor.persistance.services.ClientGroupService;
import com.scor.persistance.services.CountryService;

@Service
public class InjectRefDataService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -700659253420582220L;

	private static final Logger LOGGER = Logger.getLogger(InjectRefDataService.class);

	@Autowired
	private CountryService countryService;

	@Autowired
	private ClientGroupService parentGroupService;

	@Autowired
	private TreatyRepository treatyRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private RefDataRepository refDataRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ClientGroupRepository clientGroupRepository;

	@Autowired
	private UltimateGroupRepository ultimateGroupRepository;

	public void injectRefData() throws Exception {
		Stream<String> lines = null;
		Map<String, RefTreatyEntity> treaties = new HashMap<>();
		try {
			lines = Files.lines(
					Paths.get(InjectRefDataService.class.getClassLoader()
							.getResource("omegaExtraction/OmegaExtraction05092018.csv").toURI()),
					Charset.forName("Cp1252"));
			Map<String, String> treatiesNameByYear = new HashMap<>();
			lines.skip(1).forEach(line -> {
				String[] lineArray = line.split(";");
				String year = treatiesNameByYear.get(lineArray[0]);
				String uwyear = lineArray[1].replace(" ", "");
				if (year == null || Integer.parseInt(year) < Integer.parseInt(uwyear)) {
					String cedentNameCode = lineArray[2].replace(" ", "");
					RefTreatyEntity treaty = new RefTreatyEntity();
					treaty.setRtName(lineArray[0]);
					RefCedentNameEntity client = new RefCedentNameEntity();

					client.setRcnCode(cedentNameCode);
					client.setRcnShortName(lineArray[3]);
					RefCountryEntity country = countryService.findByCode(lineArray[9]);
					if (country != null) {
						client.setRefCountryById(country);
						client.setRcnRcId(country.getRcId());
					}
					String parentGroupCode = lineArray[5].replace(" ", "");
					if (!parentGroupCode.equals("[NULL]")) {
						String codeUltimateGroup = lineArray[7].replace(" ", "");
						RefParentGroupEntity parentGroup = parentGroupService.findOrSaveByCode(parentGroupCode,
								lineArray[6], codeUltimateGroup, lineArray[8]);
						client.setRcnRpgId(parentGroup.getRpgId());
					}
					treaty.setRefCedentNameByRtRcnId(client);
					treatiesNameByYear.put(lineArray[0], uwyear);
					treaties.put(lineArray[0], treaty);
				}

				// Reference Data
				String naic = lineArray[4];
				String portfolioOrigin = lineArray[13];
				String currency = lineArray[15];
				String SubsidiaryCode = lineArray[16] + "." + lineArray[17];
				String SubsidiaryName = lineArray[18];
				if (StringUtils.isNotBlank(naic)) {
					RefDataEntity refnaic = refDataRepository.findByTypeAndCodeAndName("naic", naic, naic);
					if (refnaic == null) {
						refnaic = new RefDataEntity();
						refnaic.setCode(naic);
						refnaic.setName(naic);
						refnaic.setType("naic");
						refDataRepository.save(refnaic);
					}
				}

				RefDataEntity refPortfolio = refDataRepository.findByTypeAndCodeAndName("portfolio_origin",
						portfolioOrigin, portfolioOrigin);
				if (refPortfolio == null) {
					refPortfolio = new RefDataEntity();
					refPortfolio.setCode(portfolioOrigin);
					refPortfolio.setName(portfolioOrigin);
					refPortfolio.setType("portfolio_origin");
					refDataRepository.save(refPortfolio);
				}
				RefDataEntity refCurrency = refDataRepository.findByTypeAndCodeAndName("currency", currency, currency);
				if (refCurrency == null) {
					refCurrency = new RefDataEntity();
					refCurrency.setCode(currency);
					refCurrency.setName(currency);
					refCurrency.setType("currency");
					refDataRepository.save(refCurrency);
				}
				RefDataEntity refSubsidiary = refDataRepository.findByTypeAndCodeAndName("subsidiary", SubsidiaryCode,
						SubsidiaryName);
				if (refSubsidiary == null) {
					refSubsidiary = new RefDataEntity();
					refSubsidiary.setCode(SubsidiaryCode);
					refSubsidiary.setName(SubsidiaryName);
					refSubsidiary.setType("subsidiary");
					refDataRepository.save(refSubsidiary);
				}

			});
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (lines != null) {
				lines.close();
			}
		}
		System.out.println(
				"Reading File extrtact Omega : OmegaExtraction05092018.csv finished ---> saving treaty and client");
		Set<Entry<String, RefTreatyEntity>> entyset = treaties.entrySet();
		entyset.forEach(entry -> {
			RefTreatyEntity treaty = entry.getValue();
			RefCedentNameEntity client = clientRepository
					.findByRcnCode(treaty.getRefCedentNameByRtRcnId().getRcnCode());
			if (client == null) {
				client = clientRepository.save(treaty.getRefCedentNameByRtRcnId());
			}
			treaty.setRefCedentNameByRtRcnId(client);
			treaty.setRtRcnId(client.getRcnId());
			treatyRepository.save(treaty);
		});

	}

	/*
	 * public void upadatepg() { // Iterable<RefUltimateGroupEntity>
	 * refUltimateGroupEntities = ultimateGroupRepository.findAll(); // Map<Integer,
	 * RefUltimateGroupEntity> refUltimateGroupEntitiesMap = new HashMap<>(); //
	 * refUltimateGroupEntities.forEach(ug -> { //
	 * refUltimateGroupEntitiesMap.put(ug.getId(), ug); // });
	 * List<RefParentGroupEntity> parentGroupEntities =
	 * clientGroupRepository.findByUltimateGroupNotNull();
	 * parentGroupEntities.forEach(pg -> { // RefUltimateGroupEntity ug =
	 * refUltimateGroupEntitiesMap.get(pg.getUltimateGroup().getId());
	 * RefParentGroupEntity pgParent =
	 * clientGroupRepository.findByRpgCodeAndRpgDescription(pg.getUltimateGroup().
	 * getCode(), "UG"); pg.setRpgParentId(pgParent); });
	 * clientGroupRepository.save(parentGroupEntities); }
	 */

	public void injectRefDataRetrocessionnaire() throws Exception {
		Stream<String> lines = null;
		try {
			lines = Files.lines(Paths.get(InjectRefDataService.class.getClassLoader()
					.getResource("omegaExtraction/retrocessionnaire.csv").toURI()), Charset.forName("Cp1252"));
			lines.skip(1).forEach(line -> {
				String[] lineArray = line.split(";", -1);
				String retrocessionnaireCode = lineArray[0];
				String retrocessionnaireName = lineArray[1];
				RefDataEntity refretrocessionnaire = refDataRepository.findByTypeAndCodeAndName("retrocessionnaire",
						retrocessionnaireCode, retrocessionnaireName);
				if (refretrocessionnaire == null) {
					refretrocessionnaire = new RefDataEntity();
					refretrocessionnaire.setCode(retrocessionnaireCode);
					refretrocessionnaire.setName(retrocessionnaireName);
					refretrocessionnaire.setType("retrocessionnaire");
					refDataRepository.save(refretrocessionnaire);
				}
			});
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (lines != null) {
				lines.close();
			}
		}
	}

	public void injectProspectClients() throws Exception {
		int[] c = new int[2];
		c[0] = 0;
		c[1] = 0;
		Stream<String> lines = null;
		try {
			lines = Files.lines(Paths.get(InjectRefDataService.class.getClassLoader()
					.getResource("omegaExtraction/clientExtraction.csv").toURI()), Charset.forName("Cp1252"));
			lines.skip(1).forEach(a -> {
				String[] line = a.split(";", -1);
				RefCedentNameEntity ced = clientRepository.findByRcnCode(line[1].trim());
				if (ced == null) {
					ced = new RefCedentNameEntity();
					ced.setRcnShortName(line[0].trim());
					ced.setRcnCode(line[1].trim());
					List<RefParentGroupEntity> parentGroupEntity = clientGroupRepository.findByRpgCode(line[3].trim());
					RefCountryEntity country = countryService.findByCode(line[4].trim());

					if (line[3].trim().equalsIgnoreCase("independant")) {
						ced.setRcnRpgId(null);
					} else if (parentGroupEntity != null && !parentGroupEntity.isEmpty()) {
						ced.setRcnRpgId(parentGroupEntity.get(0).getRpgId());
					} else {
						RefParentGroupEntity newParentGroup = new RefParentGroupEntity();
						newParentGroup.setRpgCode(line[3].trim());
						newParentGroup.setRpgName(line[2].trim());
						RefParentGroupEntity savedParentGroup = clientGroupRepository.save(newParentGroup);
						ced.setRcnRpgId(savedParentGroup.getRpgId());
					}

					if (country != null) {
						ced.setRcnRcId(country.getRcId());
						clientRepository.save(ced);
						c[1]++;
					} else {
						LOGGER.warn("Line not saved : cannot find country. line : " + a);
					}
				} else {
					c[0]++;
				}
			});

			LOGGER.warn("Injection is finished. Number of new clients added : " + c[1]
					+ " Number of already existing clients : " + c[0]);

		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (lines != null) {
				lines.close();
			}
		}
	}

	public void injectUserMail() {
		String path = Resources.getResource("scope/UsersMail.csv").getPath();
		File file = new File(path);
		try {
			InputStream stream = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			br.lines().skip(1).forEach(line -> {

				if (StringUtils.isNotBlank(line.trim())) {
					List<String> splitLine = Arrays.asList(line.trim().split(",", -1));
					String uid = splitLine.get(0);
					String email = splitLine.get(1);
					RefUserEntity user = userRepository.findFirstByRuLogin(uid);
					if (user != null && StringUtils.isNotBlank(email)) {
						user.setRuMailAdresse(email);
						userRepository.save(user);
					}
				}

			});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
