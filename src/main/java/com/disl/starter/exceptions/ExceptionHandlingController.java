package com.disl.starter.exceptions;

import com.disl.starter.models.Response;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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

import java.sql.SQLException;
import java.util.Optional;

@Hidden
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

	@ResponseBody
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
		String className = e.getClassName().getSimpleName();
		return buildResponseEntity(HttpStatus.BAD_REQUEST, false, "No " + className.toLowerCase() + " found with this id", null);
	}

	@ResponseBody
	@ExceptionHandler(ResponseException.class)
	public ResponseEntity<Object> handleResponseException(ResponseException e) {
		Object payload = e.getPayload();
		HttpStatus httpStatus = e.getHttpStatus();
		return buildResponseEntity(httpStatus != null ? httpStatus :  HttpStatus.BAD_REQUEST, false, e.getMessage(), payload);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Optional<ObjectError> objectError = e.getBindingResult().getAllErrors().stream().findFirst();

		if(objectError.isPresent()) {
			ObjectError error = objectError.get();
			return buildResponseEntity(status, false, ((FieldError) error).getField() + " field " + error.getDefaultMessage(), null);
		}

		return buildResponseEntity(status, false, "Invalid request body.", null);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity(status, false, "Invalid input.", ex.getLocalizedMessage());
	}

	@Override
	protected ResponseEntity<Object> handleConversionNotSupported(
			ConversionNotSupportedException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity(status, false, "Invalid Input", ex.getLocalizedMessage());
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
			HttpMediaTypeNotAcceptableException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity(status, false, "Invalid File Type provided.", ex.getLocalizedMessage());
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(
			HttpMessageNotWritableException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity(status, false, "Server Error. Write Failed", ex.getLocalizedMessage());
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity(status, false, "Invalid type of request.", ex.getLocalizedMessage());
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity(status, false, "Server Error Occurred.", ex.getLocalizedMessage());
	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(
			MissingPathVariableException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity(status, false, "Request Failed. Invalid Request. Please Try Again.", ex.getLocalizedMessage());
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
			HttpMediaTypeNotSupportedException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request
	) {
		return buildResponseEntity(status, false, "Request Failed. Invalid Request. Please Try Again.", ex.getLocalizedMessage());
	}

	@ResponseBody
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleSQLException(SQLException ex) {
		return buildResponseEntity(HttpStatus.BAD_REQUEST, false, "Request Failed. Invalid Request. Please Try Again.", ex.getLocalizedMessage());
    }

	private ResponseEntity<Object> buildResponseEntity(HttpStatusCode status, boolean success, String message, Object payload) {
		HttpStatus httpStatus;

		try {
			httpStatus = HttpStatus.valueOf(status.value());
		} catch (Exception e) {
			httpStatus = HttpStatus.BAD_REQUEST;
		}

		Response errorResponse = new Response(httpStatus, success, message, payload);
		return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
	}
}
