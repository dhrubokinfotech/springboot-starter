package com.disl.starter.exceptions;

import com.disl.starter.models.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint, Serializable {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Response errorResponse = new Response(HttpStatus.FORBIDDEN, false, authException.getLocalizedMessage(), null);

		String responseMsg = mapper.writeValueAsString(errorResponse);
		response.getWriter().write(responseMsg);
		response.setStatus(403);
	}
}
