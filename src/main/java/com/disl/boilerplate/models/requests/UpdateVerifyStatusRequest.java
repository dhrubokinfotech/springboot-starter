package com.disl.boilerplate.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateVerifyStatusRequest {

    @Schema(required = true)
    private long userId;

    @Schema(required = true)
    private boolean verify;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }
}
