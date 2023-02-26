package com.disl.starter.entities;

import com.disl.starter.constants.AppTables;
import com.disl.starter.constants.AppTables.RefreshTokenTable;
import com.disl.starter.models.AuditModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = AppTables.REFRESH_TOKEN_NAME)
public class RefreshToken extends AuditModel<String> {

    @Column(name = RefreshTokenTable.USER_ID)
    private Long userId;

    @Column(name = RefreshTokenTable.TOKEN)
    private String token;

    @Column(name = RefreshTokenTable.EXPIRY_TIME, nullable = false)
    private Instant expiryDate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}