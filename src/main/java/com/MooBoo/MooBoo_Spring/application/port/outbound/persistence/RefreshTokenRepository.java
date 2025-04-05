package com.MooBoo.MooBoo_Spring.application.port.outbound.persistence;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.RefreshTokenDto;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshTokenDto> findByOpaqueToken(String opaqueToken);

    void saveRefreshToken(RefreshTokenDto refreshTokenDto);

}
