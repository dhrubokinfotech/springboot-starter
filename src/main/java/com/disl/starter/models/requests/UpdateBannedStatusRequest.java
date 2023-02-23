package com.disl.starter.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateBannedStatusRequest {

    @Schema(required = true)
    private long userId;

    @Schema(required = true)
    private boolean banned;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
