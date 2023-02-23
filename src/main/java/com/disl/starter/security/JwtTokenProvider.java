package com.disl.starter.security;

import com.disl.starter.constants.SecurityConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

import static com.disl.starter.StarterApplication.logger;

@Component
public class JwtTokenProvider {
	private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

	public String generateToken(Authentication authentication) {
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

		return Jwts.builder().setSubject(customUserDetails.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.signWith(getSecretKey(), signatureAlgorithm).compact();
	}

	public String generateToken(UserDetails userDetails) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

		return Jwts.builder().setSubject(userDetails.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.signWith(getSecretKey(), signatureAlgorithm).compact();
	}

	public String getUserIdFromJWT(String token) {
		return getJwtParser().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String authToken) {
		try {
			getJwtParser().parseClaimsJws(authToken);
			return true;
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
				.signWith(getSecretKey(), signatureAlgorithm).compact();
	}

	private JwtParser getJwtParser() {
		return Jwts.parserBuilder().setSigningKey(getSecretKey()).build();
	}

	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SecurityConstants.SECRET));
	}
}