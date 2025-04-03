package com.MooBoo.MooBoo_Spring.common.filter;

import com.MooBoo.MooBoo_Spring.application.port.inbound.bookapi.RefreshTokenService;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.jwt.JwtProvider;
import com.MooBoo.MooBoo_Spring.domain.TokenStatus;
import com.MooBoo.MooBoo_Spring.domain.refreshtoken.RefreshToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * 요청이 들어오면 해당 필터가 요청을 가로채 토큰 검증
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractAccessToken(request);
        TokenStatus tokenStatus = jwtProvider.validateToken(accessToken);

        if (accessToken != null && !accessToken.isBlank() && tokenStatus == TokenStatus.VALID) {
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else if (accessToken != null && !accessToken.isBlank() && tokenStatus == TokenStatus.EXPIRED) {
            String refreshToken = extractRefreshToken(request);

            Optional<RefreshToken> result = refreshTokenService.find(refreshToken);
            if (refreshToken != null && !result.isEmpty() && result.get().validate(refreshToken)) {
                String userId = jwtProvider.getUserId(accessToken);
                List<String> roles = jwtProvider.getRoles(accessToken);
                String nickName = jwtProvider.getNickName(accessToken);
                accessToken = jwtProvider.createAccessToken(userId, roles, nickName);

                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            } else {
                redirectToLogin(response);
                return;
            }

        } else {
            redirectToLogin(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private static void redirectToLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/kakao");
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return "";
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return "";

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
