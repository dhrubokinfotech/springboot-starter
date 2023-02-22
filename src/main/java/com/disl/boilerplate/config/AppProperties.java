package com.disl.boilerplate.config;

import com.disl.boilerplate.constants.AppConstants.environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	private String backEndUrl;
	private String swaggerBackendUrl;

	public environment getActiveProfile() {
		if(environment.production.name().equals(activeProfile)) {
			return environment.production;
		} else if (environment.staging.name().equals(activeProfile)) {
			return environment.staging;
		}

		return environment.development;
	}

	public void setActiveProfile(String activeProfile) {
		this.activeProfile = activeProfile;
	}

	public String getBackEndUrl() {
		return backEndUrl;
	}

	public void setBackEndUrl(String backEndUrl) {
		this.backEndUrl = backEndUrl;
	}

	public String getSwaggerBackendUrl() {
		return swaggerBackendUrl;
	}

	public void setSwaggerBackendUrl(String swaggerBackendUrl) {
		this.swaggerBackendUrl = swaggerBackendUrl;
	}
}
