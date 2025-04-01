package com.MooBoo.MooBoo_Spring.adapter.outbound.external.oauth;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.oauth.dto.OAuth2UserInfo;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.oauth.OAuth2UserInfoFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OAuth2UserInfoFactoryImpl implements OAuth2UserInfoFactory {

    public OAuth2UserInfo getOAuthUserInfo(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        switch(registrationId) {
            case("kakao") -> {
                String provider_id = attributes.get(userNameAttributeName).toString();
                String connectedAt = (String) attributes.get("connected_at");

                Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");

                String nickname = (String) profile.get("nickname");

                return new OAuth2UserInfo(provider_id, nickname, connectedAt);
            }
            default -> throw new IllegalStateException("잘못된 인증 서버입니다.");
        }
    }


}
