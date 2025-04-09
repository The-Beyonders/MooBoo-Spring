package com.MooBoo.MooBoo_Spring.application.port.outbound.persistence;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.RefreshTokenDto;

import java.util.Optional;

public interface TokenRepository {
    Optional<String> findRefreshUUIDByUserId(String userId);

    Optional<String> findRefreshByUUID(String uuid);

    void saveRefreshToken(RefreshTokenDto refreshTokenDto);

    void deleteRefreshToken(String uuid);

    void deleteRefreshUUID(String userId);

}
