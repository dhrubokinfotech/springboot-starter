package com.disl.starter.entities;

import com.disl.starter.enums.UserTokenPurpose;
import com.disl.starter.models.AuditModel;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "SECRETS")
public class Secret extends AuditModel<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "GOOGLE_ACCESS_TOKEN")
    private String googleAccessToken;

    @Column(name = "APPLE_ACCESS_TOKEN")
    private String appleAccessToken;

    @Column(name = "FB_ACCESS_TOKEN")
    private String fbAccessToken;

    private String userToken;

    private Instant userTokenExpiresAt;

    @Enumerated(EnumType.STRING)
    private UserTokenPurpose userTokenPurpose;

    private Long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoogleAccessToken() {
        return googleAccessToken;
    }

    public void setGoogleAccessToken(String googleAccessToken) {
        this.googleAccessToken = googleAccessToken;
    }

    public String getAppleAccessToken() {
        return appleAccessToken;
    }

    public void setAppleAccessToken(String appleAccessToken) {
        this.appleAccessToken = appleAccessToken;
    }

    public String getFbAccessToken() {
        return fbAccessToken;
    }

    public void setFbAccessToken(String fbAccessToken) {
        this.fbAccessToken = fbAccessToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Instant getUserTokenExpiresAt() {
        return userTokenExpiresAt;
    }

    public void setUserTokenExpiresAt(Instant userTokenExpiresAt) {
        this.userTokenExpiresAt = userTokenExpiresAt;
    }

    public UserTokenPurpose getUserTokenPurpose() {
        return userTokenPurpose;
    }

    public void setUserTokenPurpose(UserTokenPurpose userTokenPurpose) {
        this.userTokenPurpose = userTokenPurpose;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}