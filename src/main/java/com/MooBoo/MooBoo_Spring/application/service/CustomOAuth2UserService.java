package com.MooBoo.MooBoo_Spring.application.service;

import com.MooBoo.MooBoo_Spring.adapter.inbound.api.login.dto.CreateOAuth2User;
import com.MooBoo.MooBoo_Spring.adapter.outbound.external.oauth.dto.OAuth2UserInfo;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.user.UserRole;
import com.MooBoo.MooBoo_Spring.application.port.inbound.bookapi.UserService;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.oauth.OAuth2ServiceUserInfoFactory;
import com.MooBoo.MooBoo_Spring.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;
    private final OAuth2ServiceUserInfoFactory oAuth2ServiceUserInfoFactory;

    /**
     * loadUser
     * OAuth2 공급자(Kakao, Google, Naver 등)에서 사용자 정보를 가져오는 핵심 메서드
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();

        // OAuth2 공급자의 UserInfo API 호출이 실제로 발생
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth2 공급자 ID - kakao, google
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 고유 식별자 필드 이름 가져오기
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 팩토리 클래스를 이용해 여러 기관에서 인증 가능하도록 구현함
        OAuth2UserInfo oAuth2UserInfo = oAuth2ServiceUserInfoFactory
                .getOAuthUserInfo(registrationId, userNameAttributeName, attributes);

        String providerId = oAuth2UserInfo.getProviderId();
        String connectedAt = oAuth2UserInfo.getConnectedAt();
        String nickname = oAuth2UserInfo.getUserName();

        Optional<User> user = userService.loadUserByProviderId(providerId);
        if (user.isEmpty()) {
            userService.signUpUser(new CreateOAuth2User(nickname, registrationId, providerId));
            user = userService.loadUserByProviderId(providerId);
        }

        List<UserRole> userRoles = user.get().getUserRoles();
        log.info("CustomOAuth2UserService 사용자 접근 권한 목록: "+ userRoles);

        /**
         * oAuth2User를 그대로 넘기면 인증은 되지만 권한이 없음
         * 즉, 접근 권한이 없는 사용자가 됨
         */
        return new DefaultOAuth2User(
                userRoles.stream()
                        .map(userRole -> new SimpleGrantedAuthority(userRole.toString()))
                        .collect(Collectors.toSet()),
                attributes,
                userNameAttributeName
        );
    }
}
