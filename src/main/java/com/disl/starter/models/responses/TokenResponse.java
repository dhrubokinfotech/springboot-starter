package com.disl.starter.models.responses;

import com.disl.starter.entities.User;

public class TokenResponse {
	private String accessToken;
	private String refreshToken;
	private String tokenType = "Bearer";
	private User user;
	
	public TokenResponse(String accessToken, String refreshToken, User user) {
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

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
