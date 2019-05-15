package com.scor.persistance.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.dataProcessing.bulkInsert.service.UserHabilitationService;
import com.scor.dataProcessing.common.CountriesScopes;
import com.scor.dataProcessing.common.MarketScopes;
import com.scor.dataProcessing.common.RefCountryScope;
import com.scor.dataProcessing.common.StudyRoles;
import com.scor.dataProcessing.common.TableRoles;
import com.scor.persistance.entities.RefCountryEntity;
import com.scor.persistance.entities.RefExpectedTableEntity;
import com.scor.persistance.entities.RefRequesterEntity;
import com.scor.persistance.entities.RefScopeEntity;
import com.scor.persistance.entities.RefUserEntity;
import com.scor.persistance.entities.RoleExceptionEntity;
import com.scor.persistance.entities.StudyEntity;
import com.scor.persistance.repositories.RoleExceptionRepository;
import com.scor.persistance.repositories.StudyRepository;
import com.scor.persistance.repositories.UserRepository;

@Service
public class RoleManagerService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7935946029312531635L;

	private static final Logger LOGGER = Logger.getLogger(RoleManagerService.class);

	@Autowired
	private StudyRepository studyRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StudyService studyService;

	@Autowired
	private RoleExceptionRepository roleRepository;

	@Autowired
	private ExpectedTableService expectedTableService;
	
	@Autowired
	private UserHabilitationService userHabilitationService;

	public StudyRoles StudyPrevligesCalCulator(int user_id, int study_id) throws Exception {
		RefUserEntity user = userRepository.findOne(user_id);
		StudyEntity study = studyRepository.findOne(study_id);
		RoleExceptionEntity role_entity = this.getByStIdAndUsrId(study_id, user_id);
		StudyRoles.PrimaryRole userStudyRole = this.StudyRoleCalculator(user, study, role_entity);
		String studyStatus = this.studyRepository.findOne(study_id).getStStatus();
		StudyRoles roles = new StudyRoles(userStudyRole, this.StudyDefPrev(userStudyRole, studyStatus));
		LOGGER.info("Study Privilege Calculator +++++ Primary Role = " + userStudyRole + " / study Status = "
				+ studyStatus);
		return roles;
	}
	
	public StudyRoles StudyPrevligesCalCulator(RefUserEntity user, StudyEntity study) throws Exception {
//		RefUserEntity user = userRepository.findOne(user_id);
//		StudyEntity study = studyRepository.findOne(study_id);
		RoleExceptionEntity role_entity = this.getByStIdAndUsrId(study.getStId(), user.getRuId());
		StudyRoles.PrimaryRole userStudyRole = this.StudyRoleCalculator(user, study, role_entity);
		String studyStatus = study.getStStatus();
		StudyRoles roles = new StudyRoles(userStudyRole, this.StudyDefPrev(userStudyRole, studyStatus));
		LOGGER.info("Study Privilege Calculator +++++ Primary Role = " + userStudyRole + " / study Status = "
				+ studyStatus);
		return roles;
	}

	public StudyRoles.PrimaryRole StudyRoleCalculator(RefUserEntity usr, StudyEntity st,
			RoleExceptionEntity role_exception) throws Exception {
		/*
		 * RoleExceptionEntity role_entity = this.getByStIdAndUsrId(st.getStId(),
		 * usr.getRuId());
		 * 
		 * 
		 * if(role_entity != null && StringUtils.isNotBlank(role_entity.getUreRole())){
		 * return PrimaryRole.valueOf(role_entity.getUreRole().toUpperCase()); }
		 */

		if (role_exception != null && StringUtils.isNotBlank(role_exception.getUreRole())) {

			return StudyRoles.PrimaryRole.valueOf(role_exception.getUreRole().toUpperCase());
		}

		if (st == null || usr == null)
			throw new Exception("invalid study or user id");

		String cuserScope = st.getRefUserByStCreatedById().getScope().getRsName();
		String cuserFunction = st.getRefUserByStCreatedById().getRuFunction();

		String userRole = usr.getRuRole();
		String userScope = usr.getScope().getRsName();
		String userFunction = usr.getRuFunction();
		if (userScope.equalsIgnoreCase(cuserScope) && userFunction.equalsIgnoreCase(cuserFunction)) {
			if ((userRole.toLowerCase().trim().equalsIgnoreCase("creator"))
					|| (userRole.toLowerCase().trim().equalsIgnoreCase("admin"))) {
				return StudyRoles.PrimaryRole.PRODUCER;
			} else {
				return StudyRoles.PrimaryRole.REVIEWER;
			}
		}
		if(userRole.toLowerCase().trim().equalsIgnoreCase("admin")) {
			return StudyRoles.PrimaryRole.REVIEWER;
		}
		if (checkPrivateClient(usr, st)) {
			return StudyRoles.PrimaryRole.PRIVATE_CLIENT;
		}
		return StudyRoles.PrimaryRole.OTHER;

	}

	private boolean checkPrivateClient(RefUserEntity user, StudyEntity st) {
		RefUserEntity studyCreator = st.getRefUserByStCreatedById();
		String studyCeatorFunction = studyCreator.getRuFunction();
		RefScopeEntity studyCreatorScope = studyCreator.getScope();
		String studyCreatorScopeType = studyCreatorScope.getRsType();

		String studyCreatorScopeName = studyCreatorScope.getRsName();
		if (("other".equalsIgnoreCase(studyCeatorFunction) && ("local".equalsIgnoreCase(studyCreatorScopeType)
				|| "regional".equalsIgnoreCase(studyCreatorScopeType)))
				|| ("global".equalsIgnoreCase(studyCreatorScopeType)
						&& "global".equalsIgnoreCase(studyCreatorScopeName))) {
			//return true
			return false;
		}
		RefRequesterEntity studyRequester = st.getRefRequesterByStStcId();
		RefCountryEntity studyCountry = st.getRefCountryById();
		RefScopeEntity userScope = user.getScope();
		String userScopeName = userScope.getRsName();
		String userScopeType = userScope.getRsType();
		String userFunction = user.getRuFunction();
		switch (studyCreatorScopeType.trim().toLowerCase()) {
		case "local":
			switch (studyCeatorFunction.trim().toLowerCase()) {
			case "pricing":
//				(Scope A & (Marketing OR EA)) OR (Regional scope mapped to A & (Pricing OR EA OR Risk Management))
				return (studyCreatorScopeName.trim().equalsIgnoreCase(userScopeName.trim())
						&& ("marketing".equalsIgnoreCase(userFunction) || "ea".equalsIgnoreCase(userFunction)))
						|| (compareCalculatedRegionalMarketScope(userScope, studyCreatorScope)
								&& ("pricing".equalsIgnoreCase(userFunction) || "ea".equalsIgnoreCase(userFunction)
										|| "risk management".equalsIgnoreCase(userFunction)));
			case "reserving":
//				(Scope A & (Marketing OR EA)) OR (Regional scope mapped to A & (Reserving OR EA))
				return (studyCreatorScopeName.equalsIgnoreCase(userScopeName)
						&& ("marketing".equalsIgnoreCase(userFunction) || "ea".equalsIgnoreCase(userFunction)))
						|| (compareCalculatedRegionalMarketScope(userScope, studyCreatorScope)
								&& ("reserving".equalsIgnoreCase(userFunction) || "ea".equalsIgnoreCase(userFunction)));
			case "ea":
				if (studyRequester.getStcName().equalsIgnoreCase("pricing")) {
//					(Scope A & (Marketing OR Pricing)) OR (Regional scope mapped to A & (Pricing OR EA OR Risk Management))
					return (studyCreatorScopeName.equalsIgnoreCase(userScopeName)
							&& ("marketing".equalsIgnoreCase(userFunction) || "pricing".equalsIgnoreCase(userFunction)))
							|| (compareCalculatedRegionalMarketScope(userScope, studyCreatorScope)
									&& ("pricing".equalsIgnoreCase(userFunction) || "ea".equalsIgnoreCase(userFunction)
											|| "risk management".equalsIgnoreCase(userFunction)));
				}
				if (studyRequester.getStcName().equalsIgnoreCase("reserving")) {
//					(Scope A & (Marketing OR Reserving)) OR (Regional scope mapped to A & (Reseving OR EA))
					return (studyCreatorScopeName.equalsIgnoreCase(userScopeName)
							&& ("marketing".equalsIgnoreCase(userFunction)
									|| "reserving".equalsIgnoreCase(userFunction)))
							|| (compareCalculatedRegionalMarketScope(userScope, studyCreatorScope)
									&& ("reserving".equalsIgnoreCase(userFunction)
											|| "ea".equalsIgnoreCase(userFunction)));
				}
				break;
			default:
				return false;
			}

			break;
		case "regional":
			RefCountryScope refCountryScope = CountriesScopes.getCountryScope(studyCountry.getRcCode());
			switch (studyCeatorFunction.trim().toLowerCase()) {
			case "pricing":
//				(Local Scope mapped to C & (Marketing OR Pricing OR EA)) OR (Scope B & (EA OR Risk Management))
				return ("local".equalsIgnoreCase(userScopeType)
						&& userScopeName.equalsIgnoreCase(refCountryScope.getLocalScope())
						&& ("pricing".equalsIgnoreCase(userFunction) || "ea".equalsIgnoreCase(userFunction)
								|| "marketing".equalsIgnoreCase(userFunction))
						|| (studyCreatorScopeName.equalsIgnoreCase(userScopeName) && userScopeType.equals(studyCreatorScopeType)
								&& ("ea".equalsIgnoreCase(userFunction)
										|| "risk management".equalsIgnoreCase(userFunction))));
			case "reserving":
//				(Local Scope mapped to C & (Marketing OR Reserving OR EA)) OR (Scope B & EA)
				return ("local".equalsIgnoreCase(userScopeType)
						&& userScopeName.equalsIgnoreCase(refCountryScope.getLocalScope())
						&& ("reserving.".equalsIgnoreCase(userFunction) || "ea".equalsIgnoreCase(userFunction)
								|| "marketing".equalsIgnoreCase(userFunction))
						|| (studyCreatorScopeName.equalsIgnoreCase(userScopeName) && userScopeType.equals(studyCreatorScopeType)
								&& "ea".equalsIgnoreCase(userFunction)));
			case "ea":
				if (studyRequester.getStcName().equalsIgnoreCase("pricing")) {
//					(Local Scope mapped to C & (Marketing OR Pricing OR EA)) OR (Scope B & (Pricing OR Risk Management))
					return ("local".equalsIgnoreCase(userScopeType)
							&& userScopeName.equalsIgnoreCase(refCountryScope.getLocalScope())
							&& ("pricing".equalsIgnoreCase(userFunction) || "ea".equalsIgnoreCase(userFunction)
									|| "marketing".equalsIgnoreCase(userFunction))
							|| (studyCreatorScopeName.equalsIgnoreCase(userScopeName) && userScopeType.equals(studyCreatorScopeType)
									&& ("pricing".equalsIgnoreCase(userFunction)
											|| "risk management".equalsIgnoreCase(userFunction))));
				}
				if (studyRequester.getStcName().equalsIgnoreCase("reserving")) {
//					(Local Scope mapped to C & (Marketing OR Reseving OR EA)) OR (Scope B & (Pricing OR EA OR Risk Management))
					return ("local".equalsIgnoreCase(userScopeType)
							&& userScopeName.equalsIgnoreCase(refCountryScope.getLocalScope())
							&& ("reserving".equalsIgnoreCase(userFunction) || "ea".equalsIgnoreCase(userFunction)
									|| "marketing".equalsIgnoreCase(userFunction))
							|| (studyCreatorScopeName.equalsIgnoreCase(userScopeName) && userScopeType.equals(studyCreatorScopeType)
									&& ("pricing".equalsIgnoreCase(userFunction) || "ea".equalsIgnoreCase(userFunction)
											|| "risk management".equalsIgnoreCase(userFunction))));
				}
				break;
			default:
				return false;
			}
			break;

		default:
			return false;
		}

		return false;
	}

	private boolean compareCalculatedRegionalMarketScope(RefScopeEntity scopeUser, RefScopeEntity scopeCreator) {
//		String regionalScopeCalculatedUser = MarketScopes.getRegionalScopes(scopeUser.getRsName(),
//				scopeUser.getRsType());
//		if (StringUtils.isBlank(regionalScopeCalculatedUser)) {
//			return false;
//		}
		if(!"regional".equals(scopeUser.getRsType().toLowerCase())) {
			return false;
		}
		String regionalScopeCalculatedUser = scopeUser.getRsName();
		String regionalScopeCalculatedCreator = MarketScopes.getRegionalScopes(scopeCreator.getRsName(),
				scopeCreator.getRsType());
		if (StringUtils.isBlank(regionalScopeCalculatedCreator)) {
			return false;
		}
		return regionalScopeCalculatedUser.equalsIgnoreCase(regionalScopeCalculatedCreator);
	}

	private List<StudyRoles.SecondaryRole> StudyDefPrev(StudyRoles.PrimaryRole StudyRole, String StudyStatus) {
		List<StudyRoles.SecondaryRole> secondaryRoles = new ArrayList<>();
		switch (StudyRole) {
		case PRODUCER:
			secondaryRoles = getProducerRoles(StudyStatus);
			break;
		case REVIEWER:
			secondaryRoles = getReviewerRoles(StudyStatus);
			break;
		case PRIVATE_CLIENT:
			secondaryRoles = getPrivateClientrRoles(StudyStatus);
			break;
		default:
			break;
		}

		return secondaryRoles;
	}

	private List<StudyRoles.SecondaryRole> getReviewerRoles(String studyStatus) {
		List<StudyRoles.SecondaryRole> secondaryRoles = new ArrayList<>();
		switch (studyStatus.trim().toLowerCase()) {
		case "progress":
		case "validated":
		case "published":
			secondaryRoles.addAll(
					Arrays.asList(StudyRoles.SecondaryRole.STUDY_READER, StudyRoles.SecondaryRole.DATASET_READER,
							StudyRoles.SecondaryRole.RUN_IBNR_READER, StudyRoles.SecondaryRole.RUN_READER,
							StudyRoles.SecondaryRole.TABLE_INPUT_READER, StudyRoles.SecondaryRole.TABLE_RESULT_READER));
			break;
		case "cancelled":
			secondaryRoles.addAll(Arrays.asList(StudyRoles.SecondaryRole.TABLE_INPUT_READER,
					StudyRoles.SecondaryRole.TABLE_RESULT_READER));
			break;
		default:
			break;
		}
		return secondaryRoles;
	}

	private List<StudyRoles.SecondaryRole> getProducerRoles(String studyStatus) {
		List<StudyRoles.SecondaryRole> secondaryRoles = new ArrayList<>();
		switch (studyStatus.trim().toLowerCase()) {
		case "progress":
			secondaryRoles = StudyRoles.SecondaryRole.addAllToList();
			break;
		case "cancelled":
			secondaryRoles
					.addAll(Arrays.asList(StudyRoles.SecondaryRole.STUDY_WRITER, StudyRoles.SecondaryRole.STUDY_READER,
							StudyRoles.SecondaryRole.DATASET_READER, StudyRoles.SecondaryRole.RUN_IBNR_READER,
							StudyRoles.SecondaryRole.RUN_READER, StudyRoles.SecondaryRole.RUN_DELETER,
							StudyRoles.SecondaryRole.TABLE_INPUT_READER, StudyRoles.SecondaryRole.TABLE_RESULT_READER));
			break;
		case "validated":
		case "published":
			secondaryRoles.addAll(Arrays.asList(StudyRoles.SecondaryRole.STUDY_WRITER,
					StudyRoles.SecondaryRole.STUDY_READER, StudyRoles.SecondaryRole.DATASET_READER,
					StudyRoles.SecondaryRole.RUN_IBNR_READER, StudyRoles.SecondaryRole.RUN_READER, StudyRoles.SecondaryRole.RUN_EDITOR,
					StudyRoles.SecondaryRole.STUDY_CANCELLER, StudyRoles.SecondaryRole.RUN_DELETER,
					StudyRoles.SecondaryRole.TABLE_INPUT_READER, StudyRoles.SecondaryRole.TABLE_RESULT_READER,
					StudyRoles.SecondaryRole.DATASET_CREATOR, StudyRoles.SecondaryRole.DATASET_EDITOR, StudyRoles.SecondaryRole.DATASET_CONTROLS_EXECUTER, StudyRoles.SecondaryRole.DATASET_UPLOADER));
			break;

		default:
			break;
		}
		return secondaryRoles;
	}

	private List<StudyRoles.SecondaryRole> getPrivateClientrRoles(String studyStatus) {
		List<StudyRoles.SecondaryRole> secondaryRoles = new ArrayList<>();
		switch (studyStatus.trim().toLowerCase()) {
		case "validated":
		case "published":
			secondaryRoles.addAll(
					Arrays.asList(StudyRoles.SecondaryRole.STUDY_READER, StudyRoles.SecondaryRole.DATASET_READER,
							StudyRoles.SecondaryRole.RUN_IBNR_READER, StudyRoles.SecondaryRole.RUN_READER,
							StudyRoles.SecondaryRole.TABLE_RESULT_READER));
			break;
		case "progress":
		case "cancelled":
			// No Secondary Roles added
			break;

		default:
			break;
		}
		return secondaryRoles;
	}

	public RoleExceptionEntity addException(int st_id, int usr_id, String role) {
		StudyEntity study = studyRepository.findOne(st_id);
		RefUserEntity user = userRepository.findOne(usr_id);
		RoleExceptionEntity ree = roleRepository.findByUreRuIdAndUreStId(user,
				study);
		if (ree == null)
			ree = new RoleExceptionEntity();
		ree.setUreRuId(user);
		ree.setUreStId(study);
		ree.setUreRole(role);
		RoleExceptionEntity roleExceptionEntity = roleRepository.save(ree);
		studyService.addExceptionReporting(study, user);
		return roleExceptionEntity;
	}

	public RoleExceptionEntity getByStIdAndUsrId(int st_id, int usr_id) {
		return roleRepository.findByUreRuIdAndUreStId(userRepository.findOne(usr_id), studyRepository.findOne(st_id));
	}

	public List<RoleExceptionEntity> getExceptionsByStId(int st_id) {
		return roleRepository.findByUreStId(studyRepository.findOne(st_id));

	}

	public List<RoleExceptionEntity> getRoles(int id) throws Exception {
		StudyEntity st = studyRepository.findOne(id);
		List<RoleExceptionEntity> res = new ArrayList<>();
		List<RefUserEntity> users = this.getAll();
		List<RoleExceptionEntity> roles = roleRepository.findByUreStId(st);
		for (int i = 0; i < users.size(); i++) {
			RoleExceptionEntity re = new RoleExceptionEntity();
			RefUserEntity user = users.get(i);
			List<RoleExceptionEntity> role = roles.stream().filter(s -> s.getUreRuId().getRuId() == user.getRuId())
					.collect(Collectors.toList());
			re.setUreRuId(users.get(i));
			String userRole = this.StudyRoleCalculator(users.get(i), st, null).name();
			if (role.size() >= 1) {
				re.setUreDefaultRole(userRole);
				re.setUreRole(role.get(0).getUreRole());
				re.setDefault(false);
			} else {
				re.setUreDefaultRole(userRole);
				re.setUreRole(userRole);
			}
			res.add(re);
		}
		return res;
	}

	public List<RefUserEntity> getAll() {
		// List<RefUserEntity> list = new ArrayList<>();

		return (List<RefUserEntity>) userRepository.findAll();
	}

	/**
	 * Table Library Access Rights
	 * 
	 * @param userId
	 * @param tableId
	 * @throws Exception
	 **/
	public TableRoles tableRolesCalculator(int tableId, int userId) throws Exception {
		RefUserEntity user = userRepository.findOne(userId);
		RefExpectedTableEntity expectedTable = expectedTableService.getOne(tableId);
		TableRoles.PrimaryRole primaryRole = tablePrimaryRoleCalculator(expectedTable, user);
		List<TableRoles.SecondaryRole> secondaryRoles = tableSecondaryRolesCalculator(expectedTable, user, primaryRole);
		return new TableRoles(primaryRole, secondaryRoles);
	}
	
	public TableRoles tableRolesCalculator(RefExpectedTableEntity expectedTable, RefUserEntity user) throws Exception {
		TableRoles.PrimaryRole primaryRole = tablePrimaryRoleCalculator(expectedTable, user);
		List<TableRoles.SecondaryRole> secondaryRoles = tableSecondaryRolesCalculator(expectedTable, user, primaryRole);
		return new TableRoles(primaryRole, secondaryRoles);
	}

	private TableRoles.PrimaryRole tablePrimaryRoleCalculator(RefExpectedTableEntity expectedTable, RefUserEntity user)
			throws Exception {
		RefUserEntity tableCreator = expectedTable.getRetCreatedBy();
		if (tableCreator == null) {
			throw new Exception("No Creator of the table found");
		}
		if (isTableProducer(tableCreator, user)) {
			return TableRoles.PrimaryRole.PRODUCER;
		}
		if (isTableConsumer(expectedTable, user)) {
			return TableRoles.PrimaryRole.CONSUMER;
		}
		if(user.getRuRole().toLowerCase().trim().equalsIgnoreCase("admin")) {
			return TableRoles.PrimaryRole.CONSUMER;
		}
		return TableRoles.PrimaryRole.OTHER;
	}

	private boolean isTableProducer(RefUserEntity tableCreator, RefUserEntity user) {
		String creatorScope = tableCreator.getScope().getRsName();
		String userScope = user.getScope().getRsName();
		String creatorFunction = tableCreator.getRuFunction();
		String userFunction = user.getRuFunction();
		String userRole = user.getRuRole();
		return creatorFunction.equalsIgnoreCase(userFunction) && creatorScope.equalsIgnoreCase(userScope)
				&& ((userRole.toLowerCase().trim().equalsIgnoreCase("creator"))
						|| (userRole.toLowerCase().trim().equalsIgnoreCase("admin")));
	}

	private boolean isTableConsumer(RefExpectedTableEntity expectedTable, RefUserEntity user) {
		String userScopeName = user.getScope().getRsName();
		if (userScopeName.trim().toLowerCase().equalsIgnoreCase("global")) {
			return true;
		}
		RefUserEntity tableCreator = expectedTable.getRetCreatedBy();
		String creatorScopeName = tableCreator.getScope().getRsName();
		String creatorScopeType = tableCreator.getScope().getRsType();
		String userScopeType = user.getScope().getRsType();
		RefCountryEntity tableCountry = expectedTable.getCountry();
		RefCountryScope refCountryScope = CountriesScopes.getCountryScope(tableCountry.getRcCode());

		switch (creatorScopeType.trim().toLowerCase()) {
		case "local":
			if (userScopeName.equalsIgnoreCase(creatorScopeName)) {
				return true;
			}
			String regionalScopeCalculatedUser = MarketScopes.getRegionalScopes(userScopeName, userScopeType);
			if (StringUtils.isBlank(regionalScopeCalculatedUser)) {
				return false;
			}
			String regionalScopeCalculatedCreator = MarketScopes.getRegionalScopes(creatorScopeName, creatorScopeType);
			if (StringUtils.isBlank(regionalScopeCalculatedCreator)) {
				return false;
			}
			return regionalScopeCalculatedUser.equalsIgnoreCase(regionalScopeCalculatedCreator);
//			return (userScopeName.equalsIgnoreCase(creatorScopeName)
//					|| (userScopeType.trim().toLowerCase().equalsIgnoreCase("regional")
//							&& userScopeName.equalsIgnoreCase(creatorScopeName)));
		case "regional":
			return (userScopeName.equalsIgnoreCase(creatorScopeName)
					|| (userScopeType.trim().toLowerCase().equalsIgnoreCase("local")
							&& userScopeName.toLowerCase().equalsIgnoreCase(refCountryScope.getLocalScope())));
		case "global":
			return ((userScopeType.trim().toLowerCase().equalsIgnoreCase("local")
					&& userScopeName.toLowerCase().equalsIgnoreCase(refCountryScope.getLocalScope()))
					|| (userScopeType.trim().toLowerCase().equalsIgnoreCase("regional")
							&& userScopeName.toLowerCase().equalsIgnoreCase(refCountryScope.getRegionalScope())));
		default:
			return false;
		}
	}

	private List<TableRoles.SecondaryRole> tableSecondaryRolesCalculator(RefExpectedTableEntity expectedTable,
			RefUserEntity user, TableRoles.PrimaryRole primaryRole) {
		List<TableRoles.SecondaryRole> secondaryRoles = new ArrayList<>();
		boolean isActive = expectedTable.getRetStatus().trim().equalsIgnoreCase("active");
		switch (primaryRole) {
		case PRODUCER:
			secondaryRoles
					.addAll(Arrays.asList(TableRoles.SecondaryRole.TABLE_WRITER, TableRoles.SecondaryRole.TABLE_READER,
							TableRoles.SecondaryRole.TABLE_DELETER, TableRoles.SecondaryRole.RUN_TABLE_READER));
			// if (isActive) {
			// secondaryRoles.add(TableRoles.SecondaryRole.TABLE_STATUS);
			// }
			secondaryRoles.add(TableRoles.SecondaryRole.TABLE_STATUS);
			return secondaryRoles;
		case CONSUMER:
			if (isActive) {
				secondaryRoles.addAll(Arrays.asList(TableRoles.SecondaryRole.TABLE_READER,
						TableRoles.SecondaryRole.RUN_TABLE_READER));
			}
			return secondaryRoles;
		case OTHER:
			boolean isConfidential = expectedTable.isRetIsConfidential();
			if (isActive && !isConfidential) {
				secondaryRoles.addAll(Arrays.asList(TableRoles.SecondaryRole.TABLE_READER,
						TableRoles.SecondaryRole.RUN_TABLE_READER));
			}
			return secondaryRoles;
		default:
			return secondaryRoles;
		}
	}
}
