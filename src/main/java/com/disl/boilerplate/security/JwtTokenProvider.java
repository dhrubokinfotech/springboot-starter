package com.disl.boilerplate.security;

import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.disl.boilerplate.constants.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	public String generateToken(Authentication authentication) {

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

		return Jwts.builder().setSubject(customUserDetails.getUsername()).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET).compact();
	}

	public String generateToken(UserDetails customUserDetails) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

		return Jwts.builder().setSubject(customUserDetails.getUsername()).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET).compact();
	}

	public String getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody();
		return claims.getSubject().toString();
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(authToken);

			return true;
		} catch (SignatureException ex) {
			logger.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			logger.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			logger.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty.");
		}
		return false;
	}
	
	public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.REFRESH_TOKEN_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET).compact();

	}
	

}