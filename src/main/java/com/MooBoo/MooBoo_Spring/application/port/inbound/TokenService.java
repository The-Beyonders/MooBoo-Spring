package com.MooBoo.MooBoo_Spring.application.port.inbound;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.CreateRefreshToken;

import java.util.Optional;

public interface TokenService {
    Optional<String> findRefreshToken(String userId, String uuid);

    void saveRefreshToken(CreateRefreshToken createRefreshToken);

    void deleteRefreshTokenAndUUID(String userId);

    Optional<String> findAccessUUID(String userId, String uuid);

    void saveAccessUUID(String userId, String uuid);

    void deleteAccessUUID(String userId);

}
