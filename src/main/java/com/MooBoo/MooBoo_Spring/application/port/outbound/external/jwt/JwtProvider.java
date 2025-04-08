package com.MooBoo.MooBoo_Spring.application.port.outbound.external.jwt;

import com.MooBoo.MooBoo_Spring.domain.TokenStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface JwtProvider {

    String createAccessToken(String userId, List<String> roles, String nickName, String uuid);

    String createRefreshToken(String userId, String uuid);

    TokenStatus validateToken(String token);

    String getUserId(String token);

    String getUUID(String token);

    List<String> getRoles(String token);

    String getNickName(String token);

    Authentication getAuthentication(String token);
}
