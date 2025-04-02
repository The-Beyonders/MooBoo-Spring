package com.MooBoo.MooBoo_Spring.application.port.inbound.bookapi;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.CreateRefreshToken;
import com.MooBoo.MooBoo_Spring.domain.refreshtoken.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> find(String opaqueToken);

    void save(CreateRefreshToken createRefreshToken);

}
