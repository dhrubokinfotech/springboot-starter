package com.disl.starter.security;

import com.disl.starter.enums.SocialAuthType;
import com.disl.starter.exceptions.OAuth2AuthenticationProcessingException;
import com.disl.starter.models.FacebookOAuth2UserInfo;
import com.disl.starter.models.GoogleOAuth2UserInfo;
import com.disl.starter.models.OAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(SocialAuthType authProvider, Map<String, Object> attributes) {
        if(authProvider == SocialAuthType.GOOGLE) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (authProvider == SocialAuthType.FACEBOOK) {
            return new FacebookOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + authProvider + " is not supported yet.");
        }
    }
}
