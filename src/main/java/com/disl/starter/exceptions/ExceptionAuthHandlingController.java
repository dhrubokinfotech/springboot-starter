package com.disl.starter.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.disl.starter.models.Response;

@ControllerAdvice
public class ExceptionAuthHandlingController {

	@ResponseBody
	@ExceptionHandler(value = AuthenticationException.class)
	public ResponseEntity<Object> handleAuthenticationExceptions(AuthenticationException ex, HttpServletResponse response) {
		Response errorResponse = new Response(HttpStatus.FORBIDDEN, false, "Invalid email/password provided.", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = NoHandlerFoundException.class)
	public String handleNoHandlerFound(NoHandlerFoundException ex, WebRequest request) {
		return "error-404";
	}
}
