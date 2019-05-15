package com.scor.persistance.services;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
//import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import com.scor.dataProcessing.sparkConnection.Connection;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.scor.dataProcessing.bulkInsert.service.ResultRunService;
import com.scor.fileManager.services.DeleteService;
import com.scor.persistance.entities.DataSetEntity;
import com.scor.persistance.entities.DecrementParametersEntity;
import com.scor.persistance.entities.RefUserEntity;
import com.scor.persistance.entities.RunEntity;
import com.scor.persistance.entities.StudyEntity;
import com.scor.persistance.repositories.ClientTypeRepository;
import com.scor.persistance.repositories.CountryRepository;
import com.scor.persistance.repositories.StudyRepository;
import com.scor.persistance.specifications.StudyEntityPage;

@Service
public class StudyService implements Serializable{

	private final static Logger LOGGER = Logger.getLogger(StudyService.class);

	static final JavaSparkContext sc = Connection.getContext();

	final String QUERY_SEPERATOR = ";";

	@Value("${custom.upload.path}")
	private String UPLOADED_FOLDER_BASE;

	@Autowired
	private StudyRepository studyRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private ClientTypeRepository clientTypeRepository;

	@Autowired
	private RunService runService;

	@Autowired
	private DatasetService datasetService;
	
	@Autowired
	private ResultRunService resultRunService;
	
	@Autowired
    private DeleteService deleteService;

	@PersistenceContext
	private EntityManager em;

	public StudyEntityPage searchByQuery(int page, int limit, Optional<String> code, Optional<String> country,
			Optional<String> group, Optional<String> client, Optional<String> brand, Optional<String> treaty,
			Optional<String> lob, Optional<String> status, Optional<String> statusDate, Optional<String> createdBy,
			Optional<String> sort, Optional<Boolean> desc) {
		// Builder
		CriteriaBuilder cb = em.getCriteriaBuilder();

		// Criteria Queries
		CriteriaQuery<StudyEntity> q = cb.createQuery(StudyEntity.class);

		// Study Metadata
		Metamodel m = em.getMetamodel();
		EntityType<StudyEntity> StudyEntity_ = m.entity(StudyEntity.class);

		// Root
		Root<StudyEntity> studyEntities = q.from(StudyEntity_);

		// Predicate
		Predicate p = cb.and();// Always true

		// Code
		p = filter(code, "stCode", cb, studyEntities, p);
		// Brand
		p = filter(brand, "stDistributionBrand", cb, studyEntities, p);
		// Status
		p = filter(status, "stStatus", cb, studyEntities, p);
		// Status date
		p = filter(statusDate, "stLastStatusModificationDate", cb, studyEntities, p);

		// Country
		p = filterJoin(country, "refCountryById", "rcName", cb, studyEntities, p);

		p = filterJoinCreatedBy(createdBy, "refUserByStCreatedById", "ruFirstName", "ruLastName", cb, studyEntities, p);

		// Parent Group
		p = filterJoinMOV(group, "refParentGroupByStRpgId", "rpgName", "stMultiParentGroup", "stOtherParentGroup", cb,
				studyEntities, p);
		// Clients
		p = filterJoinMOV(client, "refCedentNameByStRcnId", "rcnShortName", "stMultiCedent", "stOtherCedent", cb,
				studyEntities, p);
		// Treaty
		p = filterJoinMOV(treaty, "refTreatyByStRtId", "rtName", "stMultiTreaty", "stOtheriTreaty", cb, studyEntities,
				p);
		// lob
		p = filterJoinMultiple(lob, "refLobsById", "rlobName", cb, studyEntities, p, q);

		// not deleted studies
		// Code
		p = filter(Optional.ofNullable("not_deleted"), "stFlag", cb, studyEntities, p);

		q.where(p);

		if (!sort.isPresent()) {
			sort(desc, cb, q, studyEntities, "stLastStatusModificationDate");
		} else {
			String sortColumnName = null;
			String sortMultiColumnName = null;
			String sortOtherColumnName = null;
			String joinColumnName = null;
			String multiSortColumnName = null;

			switch (sort.get()) {
			case "group":
				sortColumnName = "rpgName";
				joinColumnName = "refParentGroupByStRpgId";
				sortMultiColumnName = "stMultiParentGroup";
				sortOtherColumnName = "stOtherParentGroup";
				break;
			case "client":
				sortColumnName = "rcnShortName";
				joinColumnName = "refCedentNameByStRcnId";
				sortMultiColumnName = "stMultiCedent";
				sortOtherColumnName = "stOtherCedent";
				break;
			case "treaty":
				sortColumnName = "rtName";
				joinColumnName = "refTreatyByStRtId";
				sortMultiColumnName = "stMultiTreaty";
				sortOtherColumnName = "stOtheriTreaty";
				break;
			case "country":
				sortColumnName = "rcName";
				joinColumnName = "refCountryById";
				break;
			case "createdBy":
				sortColumnName = "ruFirstName";
				joinColumnName = "refUserByStCreatedById";
				break;
			case "lob":
				multiSortColumnName = "rlobName";
				joinColumnName = "refLobsById";
				break;
			case "code":
				sortColumnName = "stCode";
				break;
			case "status":
				sortColumnName = "stStatus";
				break;
			case "brand":
				sortColumnName = "stDistributionBrand";
				break;
			case "statusdate":
			default:
				sortColumnName = "stLastStatusModificationDate";
			}

			if (multiSortColumnName != null && joinColumnName != null) {
				sortJoinMultiple(desc, cb, q, studyEntities, joinColumnName, multiSortColumnName);
			} else if (sortMultiColumnName != null && sortOtherColumnName != null) {
				sortJoinMOV(desc, cb, q, studyEntities, joinColumnName, sortColumnName, sortMultiColumnName,
						sortOtherColumnName);
			} else if (joinColumnName != null) {
				sortJoin(desc, cb, q, studyEntities, joinColumnName, sortColumnName);
			} else {
				sort(desc, cb, q, studyEntities, sortColumnName);
			}

		}

		TypedQuery<StudyEntity> tq = em.createQuery(q).setFirstResult(page * limit).setMaxResults(limit);
		List<StudyEntity> allStudents = tq.getResultList();

		Long totalPages;
		TypedQuery<StudyEntity> tpq = em.createQuery(q.select(studyEntities.get("stId")));
		if (limit == 0)
			totalPages = 1L;
		else
			totalPages = (long) Math.ceil(((float) tpq.getResultList().size()) / limit);

		StudyEntityPage returnPage = new StudyEntityPage(allStudents, totalPages);

		return returnPage;
	}

	private Predicate filterJoinMOV(Optional<String> query, String joinColumnName, String columnName,
			String multiColumn, String otherColumn, CriteriaBuilder cb, Root<StudyEntity> studyEntities, Predicate p) {
		if (query.isPresent()) {
			Predicate multi = cb.not(cb.and());
			Predicate other = cb.not(cb.and());

			String[] queryList = query.get().split(QUERY_SEPERATOR);

			final Join<Object, Object> join = studyEntities.join(joinColumnName, JoinType.LEFT);
			Predicate orp = cb.or();
			for (String q : queryList) {
				if ("multiple".contains(q.trim().toLowerCase()))
					multi = cb.equal(studyEntities.get(multiColumn), true);
				if ("other".contains(q.trim().toLowerCase()))
					other = cb.equal(studyEntities.get(otherColumn), true);

				orp = cb.or(orp, cb.like(join.get(columnName), "%" + q.trim() + "%"));
			}
			p = cb.and(p, cb.or(orp, cb.or(multi, other)));
		}
		return p;
	}

	private Predicate filterJoin(Optional<String> query, String joinColumnName, String columnName, CriteriaBuilder cb,
			Root studyEntities, Predicate p) {
		if (query.isPresent()) {
			String[] queryList = query.get().split(QUERY_SEPERATOR);
			final Join<Object, Object> join = studyEntities.join(joinColumnName, JoinType.LEFT);
			Predicate orp = cb.or();
			for (String q : queryList) {
				orp = cb.or(orp, cb.like(join.get(columnName), "%" + q.trim() + "%"));
			}
			p = cb.and(p, orp);
		}
		return p;
	}

	private Predicate filterJoinCreatedBy(Optional<String> query, String joinColumnName, String columnName1,
			String columnName2, CriteriaBuilder cb, Root studyEntities, Predicate p) {
		if (query.isPresent()) {
			String[] queryList = query.get().split(" ");
			final Join<Object, Object> join = studyEntities.join(joinColumnName, JoinType.LEFT);
			Predicate orp = cb.or();
			for (String q : queryList) {
				if (StringUtils.isNotBlank(q)) {
					orp = cb.or(orp, cb.like(join.get(columnName1), "%" + q.trim() + "%"));
					orp = cb.or(orp, cb.like(join.get(columnName2), "%" + q.trim() + "%"));
				}
			}
			p = cb.and(p, orp);
		}
		return p;
	}

	private Predicate filterJoinMultiple(Optional<String> query, String joinColumnName, String columnName,
			CriteriaBuilder cb, Root studyEntities, Predicate p, CriteriaQuery cq) {
		if (query.isPresent()) {
			String[] queryList = query.get().split(QUERY_SEPERATOR);
			Subquery<Long> sq = cq.subquery(Long.class);
			Root<StudyEntity> subStudy = sq.from(StudyEntity.class);
			final Join<Object, Object> join = studyEntities.join(joinColumnName, JoinType.LEFT);
			final Join<Object, Object> subjoin = subStudy.join(joinColumnName, JoinType.LEFT);

			Predicate andp = cb.or();// cb.equal(studyEntities.get("stId"),subStudy.get("stId"));
			// System.out.println(Arrays.asList(queryList));
			for (String q : queryList) {
				andp = cb.or(andp, cb.like(subjoin.get(columnName), "%" + q.trim() + "%"));
			}

			sq.select(cb.count(subjoin.get("rlobId")))
					.where(cb.and(cb.equal(studyEntities.get("stId"), subStudy.get("stId")), andp))
					.groupBy(subStudy.get("stId"));

			p = cb.and(p, cb.greaterThanOrEqualTo(sq, Long.valueOf(queryList.length)));

			// cq.distinct(true);
			// groupStudy(studyEntities,join,columnName, cq);

		}

		return p;
	}

	private void groupStudy(Root<StudyEntity> studyEntities, Join<Object, Object> join, String columnName,
			CriteriaQuery cq) {
		cq.groupBy(studyEntities.get("stId"), studyEntities.get("stFlag"),
				studyEntities.get("stLastFlagModificationDate"),studyEntities.get("stCode"),
				studyEntities.get("stRdsId"), studyEntities.get("stRpgId"), studyEntities.get("stRcnId"),
				studyEntities.get("stRcId"), studyEntities.get("stRlobId"), studyEntities.get("stRcetId"),
				studyEntities.get("stStcId"), studyEntities.get("stRtId"), studyEntities.get("stStatus"),
				studyEntities.get("stLastStatusModifiedBy"), studyEntities.get("stLastStatusModificationDate"),
				studyEntities.get("stStartObservationDate"), studyEntities.get("stEndObservationDate"),
				studyEntities.get("stMultiParentGroup"), studyEntities.get("stMultiCedent"),
				studyEntities.get("stMultiTreaty"), studyEntities.get("stOtherParentGroup"),
				studyEntities.get("stOtherCedent"), studyEntities.get("stOtheriTreaty"),
				studyEntities.get("stShortName"), studyEntities.get("stQualityDataProvider"),
				studyEntities.get("stDistributionBrand"), studyEntities.get("stAttachedFilePath"),
				studyEntities.get("stCreatedById"), studyEntities.get("stCreatedDate"), studyEntities.get("stComment"),
				join.get(columnName));
	}

	private void groupStudy(Root<StudyEntity> studyEntities, CriteriaQuery cq) {
		cq.groupBy(studyEntities.get("stId"), studyEntities.get("stFlag"),
				studyEntities.get("stLastFlagModificationDate"), studyEntities.get("stId"), studyEntities.get("stCode"),
				studyEntities.get("stRdsId"), studyEntities.get("stRpgId"), studyEntities.get("stRcnId"),
				studyEntities.get("stRcId"), studyEntities.get("stRlobId"), studyEntities.get("stRcetId"),
				studyEntities.get("stStcId"), studyEntities.get("stRtId"), studyEntities.get("stStatus"),
				studyEntities.get("stLastStatusModifiedBy"), studyEntities.get("stLastStatusModificationDate"),
				studyEntities.get("stStartObservationDate"), studyEntities.get("stEndObservationDate"),
				studyEntities.get("stMultiParentGroup"), studyEntities.get("stMultiCedent"),
				studyEntities.get("stMultiTreaty"), studyEntities.get("stOtherParentGroup"),
				studyEntities.get("stOtherCedent"), studyEntities.get("stOtheriTreaty"),
				studyEntities.get("stShortName"), studyEntities.get("stQualityDataProvider"),
				studyEntities.get("stDistributionBrand"), studyEntities.get("stAttachedFilePath"),
				studyEntities.get("stCreatedById"), studyEntities.get("stCreatedDate"), studyEntities.get("stComment"));
	}

	private Predicate filter(Optional<String> query, String columnName, CriteriaBuilder cb,
			Root<StudyEntity> studyEntities, Predicate p) {

		if (query.isPresent()) {
			String[] queryList = query.get().split(QUERY_SEPERATOR);
			Predicate orp = cb.or();
			for (String q : queryList) {
				orp = cb.or(orp, cb.like(studyEntities.get(columnName).as(String.class), "%" + q.trim() + "%"));
			}
			p = cb.and(p, orp);
		}
		return p;
	}

	private void sort(Optional<Boolean> desc, CriteriaBuilder cb, CriteriaQuery<StudyEntity> q,
			Root<StudyEntity> studyEntities, String sortColumnName) {
		Expression<Object> expression = cb.selectCase().when(cb.isNull(studyEntities.get(sortColumnName)), 3)
				.otherwise(2);
		if (desc.isPresent() && desc.get() == true) {
			q.orderBy(cb.desc(expression), cb.desc(studyEntities.get(sortColumnName)),
					cb.desc(studyEntities.get("stId")));
		} else {
			q.orderBy(cb.asc(expression), cb.asc(studyEntities.get(sortColumnName)), cb.asc(studyEntities.get("stId")));
		}
		groupStudy(studyEntities, q);
		// q.select(studyEntities.get(sortColumnName)).select(studyEntities.get("stId"));
	}

	private void sortJoin(Optional<Boolean> desc, CriteriaBuilder cb, CriteriaQuery<StudyEntity> q,
			Root<StudyEntity> studyEntities, String joinColumnName, String sortColumnName) {
		Join<Object, Object> join = studyEntities.join(joinColumnName, JoinType.LEFT);
		if (desc.isPresent() && desc.get() == true) {
			q.orderBy(cb.desc(join.get(sortColumnName)), cb.desc(studyEntities.get("stId")));
		} else {
			q.orderBy(cb.asc(join.get(sortColumnName)), cb.asc(studyEntities.get("stId")));
		}
		groupStudy(studyEntities, join, sortColumnName, q);
	}

	private void sortJoinMultiple(Optional<Boolean> desc, CriteriaBuilder cb, CriteriaQuery<StudyEntity> q,
			Root<StudyEntity> studyEntities, String joinColumnName, String sortColumnName) {
		Join<Object, Object> join = studyEntities.join(joinColumnName, JoinType.LEFT);

		groupStudy(studyEntities, join, sortColumnName, q);

		if (desc.isPresent() && desc.get() == true) {
			q.orderBy(cb.desc(cb.function("dbo.getLobsCount", Integer.class, studyEntities.get("stId"))),
					cb.desc(cb.function("dbo.getLobsString", String.class, studyEntities.get("stId"))));
		} else {
			q.orderBy(cb.asc(cb.function("dbo.getLobsCount", Integer.class, studyEntities.get("stId"))),
					cb.asc(cb.function("dbo.getLobsString", String.class, studyEntities.get("stId"))));
		}

		// groupStudy(studyEntities,join,"rlobName" ,q);
		groupStudy(studyEntities, q);
	}

	private void sortJoinMOV(Optional<Boolean> desc, CriteriaBuilder cb, CriteriaQuery<StudyEntity> q,
			Root<StudyEntity> studyEntities, String joinColumnName, String sortColumnName, String sortMultiColumnName,
			String sortOtherColumnName) {
		Join<Object, Object> join = studyEntities.join(joinColumnName, JoinType.LEFT);
		Expression<Object> expression = cb.selectCase().when(cb.isTrue(studyEntities.get(sortOtherColumnName)), 1)
				.when(cb.isTrue(studyEntities.get(sortMultiColumnName)), 2)
				.when(cb.isNull(cb.trim(join.get(sortColumnName))), 3).otherwise(0);
		if (desc.isPresent() && desc.get() == true) {
			q.orderBy(cb.asc(expression), cb.desc(join.get(sortColumnName)));
		} else {
			q.orderBy(cb.asc(expression), cb.asc(join.get(sortColumnName)));
		}
		groupStudy(studyEntities, join, sortColumnName, q);
	}

	public Page<StudyEntity> getPage(Pageable p) {
		return studyRepository.findAll(p);
	}

	public List<StudyEntity> getAllStudies() {
		List<StudyEntity> list = new ArrayList<StudyEntity>();

		for (StudyEntity st : studyRepository.findAll()) {
			list.add(st);
		}
		return list;
	}

	public StudyEntity getStudy(int id) {
		return studyRepository.findOne(id);
	}

	public StudyEntity postStudy(StudyEntity st) {
		File st_folder = null;
		String cCode = countryRepository.findOne(st.getStRcId()).getRcCode().trim();
		String cType = clientTypeRepository.findOne(st.getStStcId()).getStcCode().trim();
		String sName = st.getStShortName().trim();
		String year;
		StudyEntity res = null;

		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		year = df.format(st.getStCreatedDate());

		String prefix = cCode + "_" + sName + "_" + cType + "_" + year + "_";

		if (st.getStCode() != null && st.getStCode().substring(0, st.getStCode().length() - 3).equals(prefix)) {
			res = studyRepository.save(st);
			st_folder = new File(UPLOADED_FOLDER_BASE + "/files/" + st.getStId());
			st_folder.mkdirs();
			return res;
		}

		Optional<String> last = studyRepository.findLatestIdPrefix(prefix);

		if (last.isPresent()) {
			prefix = last.get();

			try {
				int num = Integer.parseInt(prefix.substring(prefix.length() - 3, prefix.length()));
				num++;
				st.setStCode(prefix.substring(0, prefix.length() - 3) + String.format("%03d", num));
			} catch (NumberFormatException e) {
				st.setStCode(prefix + "001");
			}
		} else {
			st.setStCode(prefix + "001");
		}

		res = studyRepository.save(st);
		st_folder = new File(UPLOADED_FOLDER_BASE + "/files/" + st.getStId());
		st_folder.mkdirs();
		return res;
	}

	public StudyEntity changeStudyStatus(int id, String status, int ruId) {
		StudyEntity st = studyRepository.findOne(id);
		st.setStStatus(status);
		st.setStLastFlagModificationDate(new Timestamp(System.currentTimeMillis()));
		st.setStLastStatusModificationDate(new Date(new java.util.Date().getTime()));
		st.setStLastStatusModifiedBy(ruId);
		StudyEntity studyResponse = studyRepository.save(st);
		this.recalculateReportingHabilitation(studyResponse);
		return studyResponse;
	}

	public StudyEntity deleteStudy(int id, String flag, Boolean rminput) throws Exception {
		StudyEntity st = studyRepository.findOne(id);
		st.setStFlag(flag);
		if (!st.getStStatus().trim().equalsIgnoreCase("progress")) {
			Long diff = new Timestamp(System.currentTimeMillis()).getTime()
					- st.getStLastFlagModificationDate().getTime();
			diff = diff / 1000 / 60 / 60 / 24;
			if (!st.getStStatus().trim().equalsIgnoreCase("cancelled") || diff < 180)
				throw new Exception("Delete Denied");
		}
		if (rminput) {
			String UPLOADED_FOLDER = UPLOADED_FOLDER_BASE + "\\files\\" + id + "\\";
			File Folder = new File(UPLOADED_FOLDER);
			String[] entries = Folder.list();
			if (Folder.exists()) {
				for (String s : entries) {
					File currentFile = new File(Folder.getPath(), s);
					currentFile.delete();
				}
				Folder.delete();

			}
		}
		new Thread(() -> {
			resultRunService.manageResultAfterStudyDeletion(st.getStId());
		}).start();
		List<DataSetEntity> datasets = datasetService.getDatasetsByStudyId(st.getStId());
		datasets.forEach(dataset-> {
			try {
				datasetService.deleteDataset(dataset.getDsId());
			} catch (Exception e) {
				LOGGER.info("cannot delete dataset : " + dataset.getDsId() + "   " + e.getMessage());
			}
		});
		StudyEntity studyResponse = studyRepository.save(st);
		return studyResponse;
	}

	public StudyEntity validateStudy(int studyId, int masterRunQx, int masterRunIx, int masterRunWx, int masterRunIxQx,
			List<Integer> runsRetained, boolean deleteUnusedDatasets, Integer ruId) throws Exception {
		StudyEntity study = studyRepository.findOne(studyId);
		if (study == null) {
			throw new Exception();
		}
		List<RunEntity> runs = runService.manageStudyMasterRuns(studyId, masterRunQx, masterRunIx, masterRunWx, masterRunIxQx,
				runsRetained);
		study.setStStatus("validated");
		study.setStLastStatusModificationDate(new Date(new java.util.Date().getTime()));
		study.setStLastStatusModifiedBy(ruId);
//		resultRunService.validateStudy(studyId);
		StudyEntity studyResponse = studyRepository.save(study);
		manageObservationPeriode(study, runs);
		if (deleteUnusedDatasets) {
			cleanUnusedDatasets(study, runs);
		}
		this.recalculateReportingHabilitation(studyResponse);
		return studyResponse;
	}

	private void manageObservationPeriode(StudyEntity study, List<RunEntity> runs) {
		Date startObservationDate = null;
		Date endObservationDate = null;
		for (RunEntity run : runs) {
			if ((run.getMasterRunQx() != null && run.getMasterRunQx())
					|| (run.getMasterRunIx() != null && run.getMasterRunIx())
					|| (run.getMasterRunWx() != null && run.getMasterRunWx())) {
				Collection<DecrementParametersEntity> decrements = run.getDecrementParametersByRunId();

				if (decrements != null && !decrements.isEmpty()) {
					for (DecrementParametersEntity decrement : decrements) {
						if (startObservationDate == null
								|| startObservationDate.after(decrement.getDpStudyPeriodStartDate())) {
							startObservationDate = decrement.getDpStudyPeriodStartDate();
						}
						if (endObservationDate == null
								|| endObservationDate.before(decrement.getDpStudyPeriodEndDate())) {
							endObservationDate = decrement.getDpStudyPeriodEndDate();
						}
					}
				}
			}
		}
		if (startObservationDate != null) {
			study.setStStartObservationDate(startObservationDate);
		}
		if (endObservationDate != null) {
			study.setStEndObservationDate(endObservationDate);
		}

	}

	private void cleanUnusedDatasets(StudyEntity study, List<RunEntity> runs) {
		List<DataSetEntity> dataSetEntities = datasetService.getDatasetsByStudyId(study.getStId());
		Set<Integer> setDatasetsUsedIds = new HashSet<>();
		runs.forEach(run -> {
			setDatasetsUsedIds.add(run.getRunDsId());
		});
		dataSetEntities.forEach(dataset -> {
			if (!setDatasetsUsedIds.contains(dataset.getDsId())) {
				try {
					datasetService.deleteDataset(dataset.getDsId());
				} catch (Exception e) {
					LOGGER.error("Delete dataset : " + dataset.getDsId() + " encoutred a problem");
				}
			}
		});
	}

	public void deleteAttachedFile(String path, int id) throws IOException {
		deleteService.delete(path.trim());
		if(id != 0) {
			StudyEntity study = getStudy(id);
			study.setStAttachedFilePath(null);
			studyRepository.save(study);
		}
	}
	
	public void recalculateReportingHabilitation(StudyEntity study) {
		List<RunEntity> runs = runService.getByStudyId(study.getStId());
		runs.forEach(run-> {
			resultRunService.recalculateHabilitation(run.getRunId());
		});
		List<DataSetEntity> datasets = datasetService.getDatasetsByStudyId(study.getStId());
		datasets.forEach(dataset-> {
			datasetService.recalculateHabilitation(dataset.getDsId());
		});
	}

	public void addExceptionReporting(StudyEntity study, RefUserEntity user) {
		List<RunEntity> runs = runService.getByStudyId(study.getStId());
		runs.forEach(run-> {
			resultRunService.recalculateHabilitationForUser(study,user, run.getRunId());
		});
		List<DataSetEntity> datasets = datasetService.getDatasetsByStudyId(study.getStId());
		datasets.forEach(dataset-> {
			datasetService.recalculateHabilitationForUser(study,user, dataset.getDsId());
		});		
	}





}
