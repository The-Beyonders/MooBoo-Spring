package com.MooBoo.MooBoo_Spring.application.port.outbound.external.jwt;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.jwt.dto.CreateAccessToken;

import java.util.List;

public interface JwtProvider {

    String createAccessToken(CreateAccessToken createAccessTokengi);

    String createRefreshToken(String userId);

    boolean validateToken(String token);

    String getUserId(String token);

    List<String> getRoles(String token);
}
