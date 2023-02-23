package com.disl.starter.services;

import com.disl.starter.constants.SecurityConstants;
import com.disl.starter.entities.RefreshToken;
import com.disl.starter.exceptions.ResponseException;
import com.disl.starter.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(SecurityConstants.REFRESH_TOKEN_EXPIRATION_TIME));
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new ResponseException(token.getToken() + " Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    public Boolean deleteByCredentialId(Long userId) {
        List<RefreshToken> refreshTokens = this.refreshTokenRepository.findByUserIdAndExpiryDateIsBefore(userId,Instant.now());
        this.refreshTokenRepository.deleteAll(refreshTokens);
        return true;
    }

    public Boolean deleteRefreshToken(RefreshToken refreshToken) {
        try{
            refreshTokenRepository.delete(refreshToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
