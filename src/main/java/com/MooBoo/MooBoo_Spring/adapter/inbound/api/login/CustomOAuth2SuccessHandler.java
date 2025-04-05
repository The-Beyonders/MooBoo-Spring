package com.MooBoo.MooBoo_Spring.adapter.inbound.api.login;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.oauth.dto.OAuth2UserInfo;
import com.MooBoo.MooBoo_Spring.application.port.inbound.bookapi.RefreshTokenService;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.jwt.JwtProvider;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.oauth.OAuth2SuccessUserInfoFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final OAuth2SuccessUserInfoFactory oAuth2SuccessUserInfoFactory;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userId = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream()
                .map(authority -> authority.toString())
                .collect(Collectors.toList());

        String nickName = null;
        // OAuth2 인증인 경우
        if(authentication instanceof OAuth2AuthenticationToken oauth2Token){
            String registrationId = oauth2Token.getAuthorizedClientRegistrationId();

            Map<String, Object> attributes  = ((OAuth2User) authentication
                    .getPrincipal())
                    .getAttributes();

            String userNameAttributeName = getUserNameAttributeName(registrationId);
            OAuth2UserInfo oAuthUserInfo = oAuth2SuccessUserInfoFactory.getOAuthUserInfo(registrationId, userNameAttributeName, attributes);

            nickName = oAuthUserInfo.getUserName();
        }

        String accessToken = jwtProvider.createAccessToken(userId, roles, nickName);
        String refreshToken = jwtProvider.createRefreshToken(userId);

        log.info("로그인 AccessToken 발급: "+ accessToken);
        log.info("로그인 RefreshToken 발급: "+ refreshToken);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE,
                ResponseCookie
                        .from("refreshToken", refreshToken)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(Duration.ofDays(7))
                        .build()
                        .toString()
        );
    }

    private String getUserNameAttributeName(String registrationId) {
        // ClientRegistration 꺼내기
        ClientRegistration clientRegistration =
                ((InMemoryClientRegistrationRepository) clientRegistrationRepository)
                        .findByRegistrationId(registrationId);

        // 고유 식별자 키 꺼내기
        String userNameAttributeName = clientRegistration
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        return userNameAttributeName;
    }
}
