package com.scor.dataProcessing.common;

import java.util.Arrays;
import java.util.List;

public class TableRoles {
	private PrimaryRole primaryRole;
	private List<SecondaryRole> secondaryRoles;

	public TableRoles(PrimaryRole primaryRole, List<SecondaryRole> secondaryRoles) {
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
		PRODUCER(1, "PRODUCER"), CONSUMER(2, "CONSUMER"), OTHER(3, "OTHER");
		private int code;
		private String role;

		private PrimaryRole(int code, String role) {
			this.code = code;
			this.role = role;
		}

	}

	public enum SecondaryRole {
		TABLE_WRITER(10, "TABLE_WRITER"), TABLE_READER(11, "TABLE_READER"), TABLE_DELETER(12, "TABLE_DELETER"),
		TABLE_STATUS(13, "TABLE_STATUS"), RUN_TABLE_READER(14, "RUN_TABLE_READER");

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
