package com.MooBoo.MooBoo_Spring.adapter.outbound.external.jwt;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.CreateRefreshToken;
import com.MooBoo.MooBoo_Spring.application.port.inbound.TokenService;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.common.RefreshTokenGenerator;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.jwt.JwtProvider;

import com.MooBoo.MooBoo_Spring.domain.TokenStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * AccessToken 생성하기
 * RefreshToken 생성하기
 * 서명 검증
 * 토큰에서 사용자 ID, 클레임 추출
 */
@Slf4j
@Setter
@Component
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider {

    private final RefreshTokenGenerator refreshTokenGenerator;
    private final TokenService tokenService;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token-validity}")
    private long accessTokenValidity;

    @Override
    public String createAccessToken(String userId, List<String> roles, String nickName, String refreshUUID) {
        String accessUUID = UUID.randomUUID().toString();
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles", roles);
        // FIXME 카카오톡 이름 그대로 노출되므로 변경 필요 (개인정보보호)
        claims.put("nickname", nickName);
        claims.put("refresh_uuid", refreshUUID);
        claims.put("access_uuid", accessUUID);

        tokenService.deleteAccessUUID(userId);
        tokenService.saveAccessUUID(userId, accessUUID);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(this.getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String createRefreshToken(String userId, String uuid) {
        // 오페이크 토큰 생성
        String refreshToken = refreshTokenGenerator.createOpaqueToken();
        tokenService.saveRefreshToken(CreateRefreshToken.create(userId, refreshToken, uuid));
        return refreshToken;
    }

    @Override
    public TokenStatus validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())     // 서명 검증에 사용할 키 설정
                    .build()
                    .parseClaimsJws(token);      // JWT 파싱 및 서명 검증 수행
            return TokenStatus.VALID;
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            return TokenStatus.INVALID;
        }
    }

    @Override
    public String getUserId(String token) {
        return getClaims(token)
                .getSubject();
    }

    @Override
    public String getRefreshUUID(String token) {
        return getClaims(token)
                .get("refresh_uuid", String.class);
    }

    @Override
    public String getAccessTokenUUID(String token) {
        return getClaims(token)
                .get("access_uuid", String.class);
    }

    @Override
    public List<String> getRoles(String token) {
        List<?> temp = getClaims(token)
                .get("roles", List.class);

        return temp.stream()
                .map(Object::toString)
                .toList();
    }

    @Override
    public String getNickName(String token) {
        return getClaims(token)
                .get("nickname", String.class);
    }

    @Override
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String userId = claims.getSubject();

        /**
         * JWT 파싱할 때 Java의 제네릭 타입 정보는 런타임에 보존되지 않음
         * claims.get() -> List<String>을 보장하지 않음
         */
        List<String> roles = ((List<?>) claims.get("roles", List.class)).stream()
                .map(Object::toString)
                .toList();

        List<? extends GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        /**
         * JWT 기반 인증에서는 Spring Security가 기대하는 인증 객체가
         * UsernamePasswordAuthenticationToken 이기 때문에
         * OAuth2AuthenticationToken을 반환하지 않는다.
         */
        return new UsernamePasswordAuthenticationToken(userId, null, authorities);
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("AccessToken 만료");
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalStateException("변형된 토큰입니다.");
        }
    }

    private Key getKey() {
        // 키를 String 타입으로 넘길 수 없어 Key 객체로 만들어 전달
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
