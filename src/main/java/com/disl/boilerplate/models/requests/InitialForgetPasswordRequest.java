package com.disl.boilerplate.models.requests;

public class InitialForgetPasswordRequest {
	private String userEmail;

	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
