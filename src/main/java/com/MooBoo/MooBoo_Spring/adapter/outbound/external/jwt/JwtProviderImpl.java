package com.MooBoo.MooBoo_Spring.adapter.outbound.external.jwt;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.jwt.dto.CreateAccessToken;
import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.CreateRefreshToken;
import com.MooBoo.MooBoo_Spring.application.port.inbound.bookapi.RefreshTokenService;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.common.RefreshTokenGenerator;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.jwt.JwtProvider;
import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.RefreshTokenRepository;
import com.MooBoo.MooBoo_Spring.domain.refreshtoken.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * AccessToken 생성하기
 * RefreshToken 생성하기
 * 서명 검증
 * 토큰에서 사용자 ID, 클레임 추출
 */
@Setter
@Component
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider {

    private final RefreshTokenGenerator refreshTokenGenerator;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token-validity}")
    private long accessTokenValidity;
    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;

    @Override
    public String createAccessToken(CreateAccessToken createAccessToken) {
        Claims claims = Jwts.claims().setSubject(createAccessToken.getUserId());
        claims.put("roles", createAccessToken.getRoles());
        // FIXME 카카오톡 이름 그대로 노출되므로 변경 필요 (개인정보보호)
        claims.put("nickname", createAccessToken.getNickName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(this.getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String createRefreshToken(String userId) {
        // 오페이크 토큰 생성
        String refreshToken = refreshTokenGenerator.createOpaqueToken();
        refreshTokenService.save(CreateRefreshToken.create(userId, refreshToken , System.currentTimeMillis() + refreshTokenValidity));
        return refreshToken;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())     // 서명 검증에 사용할 키 설정
                    .build()
                    .parseClaimsJws(token);      // JWT 파싱 및 서명 검증 수행
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    @Override
    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public List<String> getRoles(String token) {
        return Jwts.parserBuilder()
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", List.class);
    }

    private Key getKey() {
        // 키를 String 타입으로 넘길 수 없어 Key 객체로 만들어 전달
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
