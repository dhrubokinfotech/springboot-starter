package com.disl.boilerplate.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {

	private HttpStatus status;
	private String message;
	private Object payload;
	private boolean success;

	public static ResponseEntity<Response> getResponseEntity(boolean success, String message, Object payload) {
		return new ResponseEntity<>(new Response(success ? HttpStatus.OK : HttpStatus.BAD_REQUEST, success, message, payload), success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	public static ResponseEntity<Response> getResponseEntity(boolean success, String message) {
		return new ResponseEntity<>(new Response(success ? HttpStatus.OK : HttpStatus.BAD_REQUEST, success, message, null), success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	public static ResponseEntity<Response> getResponseEntity(HttpStatus httpStatus, String message, Object payload) {
		return new ResponseEntity<>(new Response(httpStatus, httpStatus.equals(HttpStatus.OK), message, payload), httpStatus);
	}

	public static ResponseEntity<Response> getResponseEntity(HttpStatus httpStatus, String message) {
		return new ResponseEntity<>(new Response(httpStatus, httpStatus.equals(HttpStatus.OK), message, null), httpStatus);
	}

	public Response() {}

	public Response(HttpStatus status, boolean success, String message, Object payload) {
		super();
		this.status = status;
		this.success = success;
		this.message = message;
		this.payload = payload;
	}

	public Response(HttpStatus status, boolean success, String message) {
		super();
		this.status = status;
		this.success = success;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
}
