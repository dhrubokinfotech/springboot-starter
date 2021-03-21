package com.disl.boilerplate.models.requests;

public class ForgetPassRequest {
	String pass1;
	String pass2;
	String token;
	public String getPass1() {
		return pass1;
	}
	public void setPass1(String pass1) {
		this.pass1 = pass1;
	}
	public String getPass2() {
		return pass2;
	}
	public void setPass2(String pass2) {
		this.pass2 = pass2;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
