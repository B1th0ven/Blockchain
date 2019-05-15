package com.scor.persistance.services;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scor.dataProcessing.common.TableRoles;
import com.scor.persistance.entities.DecrementExpectedTableEntity;
import com.scor.persistance.entities.DecrementParametersEntity;
import com.scor.persistance.entities.RefExpectedTableEntity;
import com.scor.persistance.entities.RefLobEntity;
import com.scor.persistance.entities.RefUserEntity;
import com.scor.persistance.entities.RunEntity;
import com.scor.persistance.entities.StudyEntity;
import com.scor.persistance.filters.FilterBase;
import com.scor.persistance.filters.PageBase;
import com.scor.persistance.filters.SortBase;
import com.scor.persistance.repositories.BusinessRepository;
import com.scor.persistance.repositories.CountryRepository;
import com.scor.persistance.repositories.DecrementExpectedTableRepository;
import com.scor.persistance.repositories.ExpectedTableRepository;
import com.scor.persistance.repositories.UserRepository;
import com.scor.persistance.specifications.StudyEntityPage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ExpectedTableService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8670661204775230130L;
    private static final Logger LOGGER = Logger.getLogger(ExpectedTableService.class);
    @Autowired
    private ExpectedTableRepository repository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private DecrementExpectedTableRepository DecrementExpectedTableRepository;
    @Autowired
    private RoleManagerService roleManagerService;
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager em;

    public RefExpectedTableEntity getOne(int id) {
        return repository.findOne(id);
    }

    public RefExpectedTableEntity getByCode(String code) {
        List<RefExpectedTableEntity> list = repository.findByRetCode(code);

        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public PageBase<RefExpectedTableEntity> searchByQuery(
            int page,
            int limit,
            //FILTER PARAMS
            Optional<String> country,
            Optional<String> name,
            Optional<String> version,
            Optional<String> decrement,
            Optional<String> type,
            Optional<String> origin,
            Optional<String> application,
            Optional<String> publication,
            Optional<String> source,
            Optional<String> code,
            Optional<String> creation_date,
            //SORT PARAMS
            Optional<String> sort,
            Optional<Boolean> desc,
            Optional<Integer> userId
    ) {

        //Builder
        CriteriaBuilder cb = em.getCriteriaBuilder();

        //Criteria Queries
        CriteriaQuery<RefExpectedTableEntity> q = cb.createQuery(RefExpectedTableEntity.class);

        //Study Metadata
        Metamodel m = em.getMetamodel();
        EntityType<RefExpectedTableEntity> ExpectedTableEntity_ = m.entity(RefExpectedTableEntity.class);

        //Root
        Root<RefExpectedTableEntity> entities = q.from(ExpectedTableEntity_);

        //Predicate
        Predicate p = cb.and();//Always true

        p = FilterBase.filter(name, "retName", cb, entities, p);
        p = FilterBase.filter(version, "retVersion", cb, entities, p);
        p = FilterBase.filter(decrement, "retDecrement", cb, entities, p);
        p = FilterBase.filter(type, "retType", cb, entities, p);
        p = FilterBase.filter(origin, "retOrigin", cb, entities, p);
        p = FilterBase.filter(application, "retApplicationYear", cb, entities, p);
        p = FilterBase.filter(publication, "retPublicationYear", cb, entities, p);
        p = FilterBase.filter(source, "retSource", cb, entities, p);
        p = FilterBase.filter(code, "retCode", cb, entities, p);
        p = FilterBase.filter(creation_date, "retCreationDate", cb, entities, p);

        p = FilterBase.filterJoin(country, "country", "rcName", cb, entities, p);

        //System.out.println(p);
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // IMPORTANT ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        q.where(p);
        // IMPORTANT ----------------------------------------------------------------------------------
        // --------------------------------------------------------------------------------------------


        // SORTING ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        String sortColumnName = null;
        String sortJoinColumn = null;
        String defaultSortColumn = "retName";

        if (sort.isPresent()) {
            switch (sort.get()) {
                case "name":
                    sortColumnName = "retName";
                    break;
                case "version":
                    sortColumnName = "retVersion";
                    break;
                case "decrement":
                    sortColumnName = "retDecrement";
                    break;
                case "type":
                    sortColumnName = "retType";
                    break;
                case "origin":
                    sortColumnName = "retOrigin";
                    break;
                case "application":
                    sortColumnName = "retApplicationYear";
                    break;
                case "publication":
                    sortColumnName = "retPublicationYear";
                    break;
                case "source":
                    sortColumnName = "retSource";
                    break;
                case "code":
                    sortColumnName = "retCode";
                    break;
                case "creation_date":
                    sortColumnName = "retCreationDate";
                    break;
                case "country":
                    sortColumnName = "rcName";
                    sortJoinColumn = "country";
                    break;
                default:
                    sortColumnName = "retName";
            }

            if (sortColumnName == defaultSortColumn) {
                SortBase.sort(desc, cb, q, entities, sortColumnName);
            } else if (sortJoinColumn != null) {
                SortBase.sortJoin(desc, cb, q, entities, sortJoinColumn, sortColumnName, defaultSortColumn);
            } else {
                SortBase.sort(desc, cb, q, entities, sortColumnName, defaultSortColumn);
            }
        }
        // SORTING ------------------------------------------------------------------------------------


        TypedQuery<RefExpectedTableEntity> tq = em.createQuery(q).setFirstResult(page * limit).setMaxResults(limit);
        List<RefExpectedTableEntity> all = tq.getResultList();

        Long totalPages;
        TypedQuery<RefExpectedTableEntity> tpq = em.createQuery(q.select(entities.get("retId")));
        if (limit == 0) totalPages = 1L;
        else totalPages = (long) Math.ceil(((float) tpq.getResultList().size()) / limit);

        return new PageBase<RefExpectedTableEntity>(all, totalPages);
    }

    public List<RefExpectedTableEntity> getAll() {
        List<RefExpectedTableEntity> list = new ArrayList<RefExpectedTableEntity>();

        for (RefExpectedTableEntity t : repository.findAll()) {
            list.add(t);
        }
        return list;
    }

    public RefExpectedTableEntity save(RefExpectedTableEntity t) {
        String tCodePrefix = getPrefixString(t);
        String tOldCodePrefix = getOldPrefix(t);
        List<RefExpectedTableEntity> latestTable = repository.getLatestVersion(tCodePrefix);

        int v = 1;

        // IN CASE CODE CHANGED UPDATE OLD TABLES TO BE NEWER ONES
        //System.out.println( tCodePrefix +"     "+ tOldCodePrefix );
        if (tOldCodePrefix != null && !tOldCodePrefix.equals(tCodePrefix)) {
            t.setRetCode("");
            repository.save(t);
            List<RefExpectedTableEntity> oldTable = repository.getLatestVersion(tOldCodePrefix);
            if (oldTable.size() >= 1) {
                for (RefExpectedTableEntity pt : oldTable) {
                    pt.setRetLatestVersion(true);
                    //System.out.println("OLD -> NEW "+pt.getRetId()+ " " + pt.getRetCode() + " " +pt.getRetName());
                }
            }
            repository.save(oldTable);
            t.setRetCode(null);
        }

        // UPDATE NEWER TABLES TO BE OLDER ONES
        if (t.getRetVersion() == 0 || t.getRetCode() == null) {

            if (latestTable.size() >= 1) {
                for (RefExpectedTableEntity pt : latestTable) {
                    pt.setRetLatestVersion(false);
                    //System.out.println("NEW -> OLD "+pt.getRetId()+ " " + pt.getRetCode() + " " +pt.getRetName());
                }
                v = latestTable.get(0).getRetVersion() + 1;
                repository.save(latestTable);
            }
            t.setRetCode(tCodePrefix + "_" + v);
            t.setRetVersion(v);
            t.setRetLatestVersion(true);
        }

        return this.repository.save(t);
    }

    public int getLatestVersion(RefExpectedTableEntity t) {
        if (!t.isRetLatestVersion()) return 0;

        String tCodePrefix = getPrefixString(t);
        String tOldCodePrefix = getOldPrefix(t);
        List<RefExpectedTableEntity> latestTable = repository.getLatestVersion(tCodePrefix);

        if (tCodePrefix.equals(tOldCodePrefix) && latestTable.size() >= 1) {
            return (latestTable.get(0).getRetVersion() == t.getRetVersion()) ? 0 : 1;
        }

        return latestTable.size();
    }

    private String getPrefixString(RefExpectedTableEntity t) {
        String cCode = countryRepository.findOne(t.getRetRcId()).getRcCode().trim();
        String cType = (t.getRetType() != null && t.getRetType().length() >= 1) ? t.getRetType().toUpperCase().substring(0, 1) : "N/A";

        String tCodePrefix = cCode + "_" + t.getRetDecrement() + "_" + cType + "_" + t.getRetName() + "_" + t.getRetPublicationYear();
        tCodePrefix = tCodePrefix.toUpperCase();
        return tCodePrefix;
    }

    private String getOldPrefix(RefExpectedTableEntity t) {
        String code = t.getRetCode();
        if (code == null) return null;
        else {
            ArrayList<String> strings = new ArrayList(Arrays.asList(code.split("_", -1)));
            strings.remove(strings.size() - 1);
            return String.join("_", strings);
        }
    }

    public Set<RunEntity> runsAssociatedToTable(int id, String type) {
        List<DecrementExpectedTableEntity> decrementExpectedTableList = new ArrayList<>();
        switch (type) {
            case "base":
                decrementExpectedTableList = DecrementExpectedTableRepository.findByRetBase(id);
                break;
            case "trend":
                decrementExpectedTableList = DecrementExpectedTableRepository.findByRetTrend(id);
                break;
            case "adjustment":
                decrementExpectedTableList = DecrementExpectedTableRepository.findByRetAdjustment(id);
                break;
            default:
                break;
        }
        if (decrementExpectedTableList == null || decrementExpectedTableList.isEmpty()) {
            return new HashSet<>();
        }
        Set<RunEntity> runs = new HashSet<>();
        decrementExpectedTableList.forEach(decrementExpectedTable -> {
            DecrementParametersEntity decrementParameters = decrementExpectedTable.getDecrementParametersByRetBase();
            RunEntity run = decrementParameters.getRunByDpRunId();
            if (run != null) {
                runs.add(run);
            }
        });

        return runs;
    }

    public boolean deleteTable(int id) {
        repository.delete(id);
        return true;
    }

    public PageBase<RefExpectedTableEntity> getTablesByDecrementAndType(Optional<String> decrement,
                                                                        Optional<String> type, Optional<Integer> userId) {
        String dec = decrement.get();
        String typeOfTable = type.get();
        List<RefExpectedTableEntity> all = repository.findByRetDecrementAndRetType(dec, typeOfTable);
        if (userId.isPresent()) {
            List<RefExpectedTableEntity> filtredTablesByAccesRight = filterTablesByAccessRights(all, userId.get());
            return new PageBase<RefExpectedTableEntity>(filtredTablesByAccesRight, 1L);
        }
        return new PageBase<RefExpectedTableEntity>(new ArrayList<>(), 1L);
    }

    private List<RefExpectedTableEntity> filterTablesByAccessRights(List<RefExpectedTableEntity> all, Integer userId) {
        List<RefExpectedTableEntity> filtred = new ArrayList<>();
        RefUserEntity user = userRepository.findOne(userId);
        all.forEach(table -> {
            try {
//    			LOGGER.info("Calculating role for table : "  + table.getRetCode() + " for user : " + user.getRuLogin());
                TableRoles roles = roleManagerService.tableRolesCalculator(table, user);
//				LOGGER.info("Expected Table role calculating => " + roles.getPrimaryRole() );
                if (roles != null && roles.getSecondaryRoles() != null && roles.getSecondaryRoles().contains(TableRoles.SecondaryRole.RUN_TABLE_READER)) {
//					LOGGER.info("User : " + user.getRuLogin() + " can select table : " + table.getRetCode());
                    filtred.add(table);
                }
            } catch (Exception e) {
                LOGGER.info("Error while Calculating role for table : " + table.getRetCode() + " for user : " + user.getRuLogin());
            }
        });
        return filtred;
    }

    public Map<String, Boolean> checkAgeAttained(List<Integer> ids) {
    	boolean ageAttained = false;
    	boolean insuranceAgeAttained = false;
        boolean durationYear = false;
        if (ids != null && ids.size() >= 1) {
        	ageAttained = findHeaders(ids, "age_attained");
        	insuranceAgeAttained = findHeaders(ids, "insurance_age_attained");
        	durationYear = findHeaders(ids,"duration_year");
        }
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("age_attained", ageAttained);
        result.put("insurance_age_attained", insuranceAgeAttained);
        result.put("duration_year",durationYear);
        return result;
    }

	private boolean findHeaders(List<Integer> ids, String header) {
		boolean result;
		result = ids.stream().anyMatch(s -> {
		    RefExpectedTableEntity exp = null;
		    if (s != null)
		        exp = repository.findOne(s);
		    if (exp != null && exp.getRetTechReport() != null) {
		        JSONObject jsonObj = new JSONObject(exp.getRetTechReport().toLowerCase());
		        String headers = jsonObj.getString("header");
		        String[] headersList = headers.split(";",-1);
//                  return Arrays.asList(headersList).contains("age_attained") || Arrays.asList(headersList).contains("insurance_age_attained");
		      return Arrays.asList(headersList).contains(header);
		    } else return false;
		});
		return result;
	}
    
    public boolean checkColumnExistence(RefExpectedTableEntity expectedTableEntity,String column) {
    	if(expectedTableEntity == null || StringUtils.isBlank(expectedTableEntity.getRetTechReport()) ) {
    		return false;
    	}
    	JSONObject jsonObj = new JSONObject(expectedTableEntity.getRetTechReport().toLowerCase());
        String headers = jsonObj.getString("header");
        String[] headersList = headers.split(";",-1);
        return Arrays.asList(headersList).contains(column.toLowerCase());
    	
    }

}
