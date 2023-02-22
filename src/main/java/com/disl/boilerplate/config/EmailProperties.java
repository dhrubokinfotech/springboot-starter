package com.disl.boilerplate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "spring.mail.properties.mail.smtp")
public class EmailProperties {

    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}