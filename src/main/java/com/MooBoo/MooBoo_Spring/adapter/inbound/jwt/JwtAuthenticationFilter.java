package com.MooBoo.MooBoo_Spring.adapter.inbound.jwt;

import com.MooBoo.MooBoo_Spring.application.port.inbound.TokenService;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.jwt.JwtProvider;
import com.MooBoo.MooBoo_Spring.domain.TokenStatus;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractAccessToken(request);
        TokenStatus tokenStatus = jwtProvider.validateToken(accessToken);

        log.info("사용자가 헤더에 전송한 accessToken 토큰 :" + accessToken);
        if (accessToken != null && !accessToken.isBlank() && tokenStatus == TokenStatus.VALID) {
            log.info("토큰 유효");
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("유효 authentication-principal: "+ authentication.getPrincipal());
            log.info("유효 authentication-authorities: "+ authentication.getAuthorities());
            log.info(SecurityContextHolder.getContext().toString());

        } else if (accessToken != null && !accessToken.isBlank() && tokenStatus == TokenStatus.EXPIRED) {
            log.info("토큰 기간 만료");
            String refreshToken = extractRefreshToken(request);
            String userId = jwtProvider.getUserId(accessToken);
            String uuid = jwtProvider.getUUID(accessToken);

            Optional<String> result = tokenService.findRefreshToken(userId, uuid);

            log.info("사용자가 쿠키에 전송한 RefreshToken: " + refreshToken);
            if (refreshToken != null && !result.isEmpty()) {
                List<String> roles = jwtProvider.getRoles(accessToken);
                String nickName = jwtProvider.getNickName(accessToken);
                accessToken = jwtProvider.createAccessToken(userId, roles, nickName, uuid);

                log.info("AccessToken userId: " + userId);
                log.info("AccessToken nickName: " + nickName);
                log.info("AccessToken roles: " + roles);

                log.info("재발급한 AccessToken: " + accessToken);

                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("재발급 후, authentication-principal: "+ authentication.getPrincipal());
                log.info("재발급 후, authentication-authorities: "+ authentication.getAuthorities());
                log.info(SecurityContextHolder.getContext().toString());

                response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            } else {
                log.error("리프래시에 문제 발생 // 없음 or 유저 불일치");
                respondInvalidToken(response);
                return;
            }

        } else {
            log.error("유효하지 않은 AccessToken");
            respondInvalidToken(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private static void respondInvalidToken(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Unauthorized or Token Expired\"}");
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
