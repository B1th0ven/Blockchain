package com.scor.dataProcessing.common;

import java.util.Arrays;
import java.util.List;

public class StudyRoles {
	private PrimaryRole primaryRole;
	private List<SecondaryRole> secondaryRoles;

	public StudyRoles(PrimaryRole primaryRole, List<SecondaryRole> secondaryRoles) {
		super();
		this.primaryRole = primaryRole;
		this.secondaryRoles = secondaryRoles;
	}

	public PrimaryRole getPrimaryRole() {
		return primaryRole;
	}

	public void setPrimaryRole(PrimaryRole primaryRole) {
		this.primaryRole = primaryRole;
	}

	public List<SecondaryRole> getSecondaryRoles() {
		return secondaryRoles;
	}

	public void setSecondaryRoles(List<SecondaryRole> secondaryRoles) {
		this.secondaryRoles = secondaryRoles;
	}

	public enum PrimaryRole {
		PRODUCER(1, "PRODUCER"), REVIEWER(2, "REVIEWER"), PRIVATE_CLIENT(3, "PRIVATE CLIENT"), OTHER(4,"OTHER");
		private int code;
		private String role;

		private PrimaryRole(int code, String role) {
			this.code = code;
			this.role = role;
		}

	}

	public enum SecondaryRole {
		STUDY_READER(10, "STUDY_READER"), STUDY_WRITER(11, "STUDY_WRITER"), STUDY_CANCELLER(12, "STUDY_CANCELLER"),
		DATASET_CREATOR(20, "DATASET_CREATOR"), DATASET_UPLOADER(21, "DATASET_UPLOADER"),
		DATASET_CONTROLS_EXECUTER(22, "DATASET_CONTROLS_EXECUTER"), DATASET_EDITOR(23, "DATASET_EDITOR"),
		DATASET_READER(24, "DATASET_READER"), RUN_CREATOR(30, "RUN_CREATOR"), RUN_EDITOR(31, "RUN_EDITOR"),
		RUN_IBNR_UPLOADER(32, "RUN_IBNR_UPLOADER"), RUN_IBNR_READER(33, "RUN_IBNR_READER"),
		RUN_LAUNCHER(34, "RUN_LAUNCHER"), RUN_READER(35, "RUN_READER"), RUN_DELETER(36, "RUN_DELETER"),
		TABLE_INPUT_READER(40, "TABLE_INPUT_READER"), TABLE_RESULT_READER(41, "TABLE_RESULT_READER");

		private int code;
		private String role;

		private SecondaryRole(int code, String role) {
			this.code = code;
			this.role = role;
		}

		public static List<SecondaryRole> addAllToList() {
			List<SecondaryRole> secondaryRoles = Arrays.asList(values());
			return secondaryRoles;
		}
	}
}
