package com.disl.boilerplate.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.disl.boilerplate.constants.SecurityConstants;

import io.jsonwebtoken.ExpiredJwtException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
//		System.out.println("host = " + request.getRemoteHost() + ", port = " + request.getRemotePort() + ", address = " + request.getRemoteAddr());
		try {
			String jwt = getJwtFromRequest(request);

			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String userId = tokenProvider.getUserIdFromJWT(jwt);

				UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (ExpiredJwtException ex) {
			logger.debug("Expired JWT Token.");
			String isRefreshToken = request.getHeader("isRefreshToken");
			String requestURL = request.getRequestURL().toString();
			// allow for Refresh Token creation if following conditions are true.
			if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
				allowForRefreshToken(ex, request);
			} else
				request.setAttribute("exception", ex);
		}  catch (Exception ex) {
			System.out.println(ex);
		}
		filterChain.doFilter(request, response);
	}
	
	private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
//		System.out.println()
		// create a UsernamePasswordAuthenticationToken with null values.
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null, null);
		// After setting the Authentication in the context, we specify
		// that the current user is authenticated. So it passes the
		// Spring Security Configurations successfully.
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		// Set the claims so that in controller we will be using it to create
		// new JWT
		request.setAttribute("claims", ex.getClaims());

	}
	
	
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(SecurityConstants.HEADER_STRING);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

}
