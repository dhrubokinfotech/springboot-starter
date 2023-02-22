package com.disl.boilerplate.constants;

import java.util.HashMap;

public class AppConstants {

	public enum environment {
		development,
		staging,
		production
	}

	public static String DEFAULTDATEFORMAT = "dd-MMM-yy";

	public static String INITIAL_USERNAME = "super_admin@disl.com";
	public static String INITIAL_PASSWORD = "123456";
	
	public static String INITIAL_ROLE = "SUPER ADMIN";
	public static String userRole = "USER";

	public static String consumerPermission = "USER";
	public static String consumerPermissionDesc = "User Generalized Permission";

	public static String RESET_PASSWORD_SUBURL = "/resetpassword?passResetToken=";
	public static String VERIFICATION_SUBURL = "/verify?verificationToken=";
	
	public static String BANNED_MESSAGE = "You are banned by admin. If you think it is a mistake then please contact with us";

	public final static String invitationSubject = "BoilerPlate User Verification";
	public final static String invitationText = "Please follow this link to verify your email for BoilerPlate. \n \t";

	public final static String forgetPasswordSubject = "BoilerPlate Password Reset Link";
	public final static String forgetPasswordText = " To reset your password in BoilerPlate please click on the following url \n \t" ;

	public static final String PAGE_NO = "pageNo";
	public static final String SORT_BY = "sortBy";
	public static final String PAGE_SIZE = "pageSize";
	public static final String ASC_OR_DESC = "ascOrDesc";

	public static final String PAGE_NO_VALUE = "0";
	public static final String PAGE_SIZE_VALUE = "20";
	public static final String ASC_OR_DESC_VALUE = "asc";
	public static final String SORT_BY_VALUE = "creationDate";

	public static final String PARAMERTES = "parameters";
	
	public static HashMap<String, String> PERMISSIONS = new HashMap<>() {
		{
			put("GENERAL", "GENERAL CONSUMER");

			put("USER_CREATE", "USER CREATE");
			put("USER_READ", "USER READ");
			put("USER_UPDATE", "USER UPDATE");
			put("USER_DELETE", "USER DELETE");

			put("ROLE_CREATE", "ROLE CREATE");
			put("ROLE_READ", "ROLE READ");
			put("ROLE_UPDATE", "ROLE UPDATE");
			put("ROLE_DELETE", "ROLE DELETE");

			put("USER_UPDATE_FROM_ADMIN", "USER UPDATE FROM ADMIN");
		}
	};
}

