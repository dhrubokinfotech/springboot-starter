package com.disl.boilerplate.exceptions;

import java.io.IOException;
import java.io.Serializable;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.disl.boilerplate.payloads.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint, Serializable {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String responseMsg = mapper.writeValueAsString(
				new Response(HttpStatus.FORBIDDEN, false, authException.getLocalizedMessage(), null));
		response.getWriter().write(responseMsg);
		response.setStatus(403);
	}
}
