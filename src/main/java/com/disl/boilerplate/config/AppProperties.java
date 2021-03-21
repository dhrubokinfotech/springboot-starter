package com.disl.boilerplate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

	private String backEndUrl;
	private String backEndUrl1;

	public String getBackEndUrl() {
		return backEndUrl;
	}
	public void setBackEndUrl(String backEndUrl) {
		this.backEndUrl = backEndUrl;
	}

	public String getBackEndUrl1() {
		return backEndUrl1;
	}
	public void setBackEndUrl1(String backEndUrl1) {
		this.backEndUrl1 = backEndUrl1;
	}

}
