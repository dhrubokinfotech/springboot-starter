package com.disl.boilerplate.security;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.disl.boilerplate.constants.SecurityConstants;
import com.disl.boilerplate.exceptions.AuthenticationExceptionHandler;

@Configuration
@EnableWebMvc
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final PasswordEncoder passwordEncoder;

	@Autowired AuthenticationExceptionHandler authExceptionHandler;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	public WebSecurity(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		System.out.println("SECURITY LOADING");
		http.csrf().disable().cors().and().authorizeRequests()
				.antMatchers(SecurityConstants.JWTDisabledAntMatchers)
				.permitAll().anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.requiresChannel()
	      .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
	      .requiresSecure();
		
		http.exceptionHandling().authenticationEntryPoint(authExceptionHandler);
		
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        
        ArrayList<String> all = new ArrayList<>();
        all.add("*");
        
        ArrayList<String> methods = new ArrayList<>();
        methods.add("GET");
        methods.add("POST");
        methods.add("PUT");
        methods.add("DELETE");
        methods.add("PATCH");
        
        configuration.setAllowedOrigins(all); // www - obligatory
        configuration.setAllowedMethods(methods);
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(all);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
