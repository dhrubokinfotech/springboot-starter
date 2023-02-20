package com.disl.boilerplate.exceptions;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class SecurityExceptionHandlingController implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(
			HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception
	) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
}
