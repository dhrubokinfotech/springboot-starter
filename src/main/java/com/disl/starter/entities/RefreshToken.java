package com.disl.starter.entities;

import com.disl.starter.models.AuditModel;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "REFRESH_TOKEN")
public class RefreshToken extends AuditModel<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "REFRESH_TOKEN")
    private long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "EXPIRY_TIME", nullable = false)
    private Instant expiryDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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