package com.disl.boilerplate.constants;

public class AppTables {
	public static final String rolePrivilege =  "ROLE_PRIVILEGE";
	public static final String userRole = "USER_ROLE";

	public static final String user = "USER";
	public static final String role = "ROLE";
	public static final String privilege = "PRIVILEGE";

	public static final class userTable {
		public static final String id = "ID";
		public static final String email = "EMAIL";
		public static final String password = "PASSWORD";
		public static final String passwordResetToken = "PASSWORD_RESET_TOKEN";
	}

	public static final class roleTable {
		public static final String roleId = "ID";
		public static final String roleName = "ROLE_NAME";
	}

	public static final class privilegeTable {
		public static final String id = "ID";
		public static final String name = "NAME";
		public static final String descName = "DESC_NAME";
	}

}