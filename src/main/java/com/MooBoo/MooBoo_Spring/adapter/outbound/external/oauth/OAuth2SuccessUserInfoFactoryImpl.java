package com.MooBoo.MooBoo_Spring.adapter.outbound.external.oauth;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.oauth.dto.OAuth2UserInfo;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.oauth.OAuth2SuccessUserInfoFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OAuth2SuccessUserInfoFactoryImpl implements OAuth2SuccessUserInfoFactory {

    @Override
    public OAuth2UserInfo getOAuthUserInfo(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        switch(registrationId) {
            case("kakao") -> {
                String provider_id = attributes.get(userNameAttributeName).toString();

                Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

                String nickName = (String) properties.get("nickname");
                String profileImage = (String) properties.get("profile_image");
                String thumbnailImage = (String) properties.get("thumbnail_image");

                return new OAuth2UserInfo(provider_id, nickName, profileImage, thumbnailImage);
            }
            default -> throw new IllegalStateException("잘못된 인증 서버입니다.");
        }
    }
}
