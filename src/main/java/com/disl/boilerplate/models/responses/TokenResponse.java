package com.disl.boilerplate.models.responses;

import com.disl.boilerplate.models.User;

public class TokenResponse {
	private String accessToken;
	private String tokenType = "Bearer";
	private User user;
	
	public TokenResponse(String accessToken, User user) {
		super();
		this.accessToken = accessToken;
		this.user = user;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
