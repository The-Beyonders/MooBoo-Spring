package com.MooBoo.MooBoo_Spring.application.port.outbound.external.jwt;

import java.util.List;

public interface JwtProvider {

    String createAccessToken(String userId, List<String> roles);

    String createRefreshToken(String userId);

    boolean validateToken(String token);

    String getUserId(String token);

    List<String> getRoles(String token);
}
