package com.MooBoo.MooBoo_Spring.application.port.inbound;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.CreateRefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<String> find(String userId, String uuid);

    void save(CreateRefreshToken createRefreshToken);

}
