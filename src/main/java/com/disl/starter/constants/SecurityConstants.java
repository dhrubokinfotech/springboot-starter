package com.disl.starter.constants;

public final class SecurityConstants {
	private SecurityConstants() {}

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final long EXPIRATION_TIME = 86400000; // 10 days
	public static final String HEADER_STRING = "Authorization";
	public static final long REFRESH_TOKEN_EXPIRATION_TIME =  864000000;
	public static final String SECRET = "2034f6e32958647fdff75d265b455ebf2034f6e32958647fdff75d265b455ebf2034f6e32958647fdff75d265b455ebf";

	public static final String[] JWTDisabledAntMatchers = {
			"/swagger-ui.html",
			"/resetpassword/**",
			"/pass-reset",
			"/api/signin",
			"/api/signup",
			"/api/forgetpassword",
			"/api-documentation.html",
			"/swagger-ui/**",
			"/api-docs/**",
			"/api/verify/**",
			"/v3/api-docs/**",
			"/configuration/ui",
			"/swagger-resources/**",
			"/configuration/security",
			"/webjars/**",
			"/verify*",
			"/api/oauth2/**",
			"/api/refreshtoken",
			"/api/db-file/id/**",
			"/api/page/tag/**",
			"/api/page/predefine-tags",
	};

	public static final String[] FormDisabledAntMatchers = {
			"/static/**",
			"/**",
			"/error/**",
			"/swagger-ui/**",
			"/api-docs/**",
			"/verify/**",
			"/v2/api-docs",
			"/configuration/ui",
			"/swagger-resources/**",
			"/configuration/security",
			"/swagger-ui.html",
			"/webjars/**",
			"/images/**",
			"/build/**",
			"/dist/**",
			"/docs/**",
			"/pages/**",
			"/plugins/**",
			"/login/**",
	};
}
