package com.disl.boilerplate.models.requests;

import io.swagger.annotations.ApiModelProperty;

public class UpdateBannedStatusRequest {

    @ApiModelProperty(required = true)
    private long userId;

    @ApiModelProperty(required = true)
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
