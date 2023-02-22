package com.disl.boilerplate.models.requests;

import io.swagger.annotations.ApiModelProperty;

public class UpdateVerifyStatusRequest {

    @ApiModelProperty(required = true)
    private long userId;

    @ApiModelProperty(required = true)
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
