package com.MooBoo.MooBoo_Spring.application.service;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.CreateRefreshToken;
import com.MooBoo.MooBoo_Spring.application.port.inbound.bookapi.RefreshTokenService;
import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.RefreshTokenRepository;
import com.MooBoo.MooBoo_Spring.domain.refreshtoken.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Optional<RefreshToken> find(String opaqueToken) {
        return refreshTokenRepository.findByOpaqueToken(opaqueToken);
    }

    @Override
    @Transactional
    public void save(CreateRefreshToken createRefreshToken) {
        refreshTokenRepository.saveRefreshToken(RefreshToken.to(createRefreshToken));
    }
}
