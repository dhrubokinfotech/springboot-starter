package com.disl.boilerplate.constants;
import java.util.HashMap;

public class AppConstants {
	
	public enum environment {
		development,
		staging,
		production,
		clienttest
	}
	 
	public static String logfilePath () {
		switch (activeProfile) {
			case development:
				return "/Users/hafiz/Desktop/SpringAuth/Logs";
			case staging:
				return "/home/ubuntu/logfiles/";
			case production:
				return "/home/ubuntu/logfiles/";
			case clienttest:
				return "/home/ubuntu/logfiles/";
		}
		
		return "";
	}

	public static final environment activeProfile = environment.staging;
	
	public static String DEFAULTDATEFORMAT = "dd-MMM-yy";

	public static String USERNAME = "super_admin@disl.com";
	public static String PASSWORD = "123456";
	
	public static String INITIALROLE = "SUPER ADMIN";

	public static String consumerPermission = "USER";
	public static String consumerPermissionDesc = "User Generalized Permission";
	public static String consumerRole = "USER";

	public static String RESET_PASSWORD_SUBURL = "/resetpassword?passResetToken=";
	public static String VERIFICATION_SUBURL = "/verify?verificationToken=";
	
	public static String BANNED_MESSAGE = "You are banned by admin. If you think it is a mistake then please contact with us";

	public final static String invitationSubject = "BoilerPlate User Verification";
	public final static String invitationText = "Please follow this link to verify your email for BoilerPlate. \n \t";

	public final static String forgetPasswordSubject = "BoilerPlate Password Reset Link";
	public final static String forgetPasswordText = " To reset your password in BoilerPlate please click on the following url \n \t" ;

	
	public static HashMap<String, String> PERMISSSIONS = new HashMap<String, String>() {
		private static final long serialVersionUID = -8925309825687257974L;
		{
			put("GENERAL","GENERAL CONSUMER");
			
			put("USER_CREATE", "USER CREATE");
			put("USER_READ", "USER READ");
			put("USER_UPDATE", "USER UPDATE");
			put("USER_DELETE", "USER DELETE");
			
			put("ROLE_CREATE", "ROLE CREATE");
			put("ROLE_READ", "ROLE READ");
			put("ROLE_UPDATE", "ROLE UPDATE");
			put("ROLE_DELETE", "ROLE DELETE");
		}
	};
	
}

