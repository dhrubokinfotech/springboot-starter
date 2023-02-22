package com.disl.boilerplate.exceptions;

import com.disl.boilerplate.models.Response;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import springfox.documentation.annotations.ApiIgnore;

import java.sql.SQLException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@ApiIgnore
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity((new Response(status, false, "Invalid input.", ex.getLocalizedMessage())));
	}

	private ResponseEntity<Object> buildResponseEntity(Response apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleConversionNotSupported(
			ConversionNotSupportedException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity((new Response(status, false, "Invalid Input", ex.getLocalizedMessage())));
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
			HttpMediaTypeNotAcceptableException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity((new Response(status, false, "Invalid File Type provided.", ex.getLocalizedMessage())));
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(
			HttpMessageNotWritableException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity((new Response(status, false, "Server Error. Write Failed", ex.getLocalizedMessage())));
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity((new Response(status, false, "Invalid type of request.", ex.getLocalizedMessage())));
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity((new Response(status, false, "Server Error Occurred.", ex.getLocalizedMessage())));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity(
				(new Response(status, false, "Request Failed. Please Try again.", ex.getLocalizedMessage())));
	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(
			MissingPathVariableException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity((new Response(status, false, "Request Failed. Invalid Request. Please Try Again.",
				ex.getLocalizedMessage())));
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
			HttpMediaTypeNotSupportedException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity((new Response(status, false, "Request Failed. Invalid Request. Please Try Again.",
				ex.getLocalizedMessage())));
	}

	@ResponseBody
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleSQLException(SQLException ex) {
		return buildResponseEntity((new Response(HttpStatus.BAD_REQUEST, false, "Request Failed. Invalid Request. Please Try Again.",
				ex.getLocalizedMessage())));
    }
}
