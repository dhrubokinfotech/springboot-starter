package com.disl.boilerplate.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
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

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer{      
	
	@Autowired
	AppProperties appProperties;
	
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
        	.host(appProperties.getBackEndUrl1())
            .select()
            .apis(RequestHandlerSelectors.any())
            .build()
            .apiInfo(apiInfo())
            .securitySchemes(Arrays.asList(apiKey()));

    }    
    
    private ApiKey apiKey() {
        return new ApiKey("jwtToken", "Authorization", "header");
    }

	   private ApiInfo apiInfo() {
	      return new ApiInfoBuilder()
	            .title("BoilerPlate API")
	            .description("BoilerPlate API")
	            .version("Version 1.0.0")
	            .licenseUrl("https://boilerplate.com/")
	            .build();
	   }
}