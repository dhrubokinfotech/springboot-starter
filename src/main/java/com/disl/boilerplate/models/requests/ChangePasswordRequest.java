package com.disl.boilerplate.models.requests;

public class ChangePasswordRequest {
	
	private String email;
	private String previousPassword;
	private String newPassword;


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPreviousPassword() {
		return previousPassword;
	}
	public void setPreviousPassword(String previousPassword) {
		this.previousPassword = previousPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
