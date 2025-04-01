package com.MooBoo.MooBoo_Spring.application.port.outbound.external.oauth;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.oauth.dto.OAuth2UserInfo;

import java.util.Map;


public interface OAuth2UserInfoFactory {
    OAuth2UserInfo getOAuthUserInfo(String registrationId, String userNameAttributeName, Map<String, Object> attributes);
}
