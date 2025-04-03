package com.MooBoo.MooBoo_Spring.application.port.outbound.persistence;

import com.MooBoo.MooBoo_Spring.domain.refreshtoken.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByOpaqueToken(String opaqueToken);

    void saveRefreshToken(RefreshToken refreshToken);

    void delete(String userId);
}
