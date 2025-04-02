package com.MooBoo.MooBoo_Spring.application.port.outbound.external.jwt;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.jwt.dto.CreateAccessToken;
import com.MooBoo.MooBoo_Spring.domain.TokenStatus;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface JwtProvider {

    String createAccessToken(String userId, List<String> roles, String nickName);

    String createRefreshToken(String userId);

    TokenStatus validateToken(String token);

    String getUserId(String token);

    List<String> getRoles(String token);

    String getNickName(String token);

    Authentication getAuthentication(String token);
}
