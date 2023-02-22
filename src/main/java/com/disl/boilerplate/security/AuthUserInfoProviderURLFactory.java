package com.disl.boilerplate.security;

import com.disl.boilerplate.enums.SocialAuthType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthUserInfoProviderURLFactory {

    @Value("${provider.facebook.userInfoUri}")
    private String fbUserInfoUri;

    @Value("${provider.google.userInfoUri}")
    private String googleUserInfoUri;

    @Value("${provider.apple.userInfoUri}")
    private String appleUserInfoUri;

    public String getUserInfoUri(SocialAuthType provider) {
        if (provider == SocialAuthType.FACEBOOK) {
            return fbUserInfoUri;
        } else if (provider == SocialAuthType.GOOGLE) {
            return googleUserInfoUri;
        } else {
            return appleUserInfoUri;
        }
    }
}
