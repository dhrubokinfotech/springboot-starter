package com.disl.boilerplate.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OneSignalInfoProvider {

	 @Value("${onesignal.url}")
	 private String url;

	 @Value("${onesignal.appId}")
	 private String appId;
	 
	 @Value("${onesignal.restApiKey}")
	 private String restApiKey;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getRestApiKey() {
		return restApiKey;
	}

	public void setRestApiKey(String restApiKey) {
		this.restApiKey = restApiKey;
	}
}
