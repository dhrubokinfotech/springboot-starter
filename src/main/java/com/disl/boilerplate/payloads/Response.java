package com.disl.boilerplate.payloads;

import java.util.List;

import org.springframework.http.HttpStatus;

public class Response {

	private HttpStatus status;
	private boolean success;
	private String message;
	private Object payload;
	private List<ApiSubError> subErrors;
	private String debugMessage;
	
	public Response(HttpStatus status, boolean success, String message, Object payload) {
		super();
		this.status = status;
		this.success = success;
		this.message = message;
		this.payload = payload;
	}
	
	public Response(HttpStatus status, boolean success, String message, List<ApiSubError> subErrors,
			String debugMessage) {
		super();
		this.status = status;
		this.success = success;
		this.message = message;
		this.subErrors = subErrors;
		this.debugMessage = debugMessage;
	}
	
	public List<ApiSubError> getSubErrors() {
		return subErrors;
	}
	public void setSubErrors(List<ApiSubError> subErrors) {
		this.subErrors = subErrors;
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
	public String getDebugMessage() {
		return debugMessage;
	}
	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	

}
