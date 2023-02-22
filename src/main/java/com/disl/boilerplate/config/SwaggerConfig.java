package com.disl.boilerplate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    @Value("${app.name}")
    private String appName;

    @Autowired
    private AppProperties appProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(appProperties.getSwaggerBackendUrl())
                .select()
                .apis(RequestHandlerSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(List.of(apiKey()));

    }

    private ApiKey apiKey() {
        return new ApiKey("jwtToken", "Authorization", "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(appName + " API")
                .description(appName + " API Documentation")
                .version("Version 1.0.0")
                .licenseUrl(appProperties.getSwaggerBackendUrl())
                .build();
    }
}