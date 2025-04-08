package com.MooBoo.MooBoo_Spring.application.service;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.CreateRefreshToken;
import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.RefreshTokenDto;
import com.MooBoo.MooBoo_Spring.application.port.inbound.RefreshTokenService;
import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Optional<String> find(String userId, String uuid) {

        Optional<String> findUUID = refreshTokenRepository.findUUIDByUserId(userId);

        // UUID가 없거나 일치하지 않는 경우
        if (findUUID.isEmpty() || !uuid.equals(findUUID.get())) {
            log.info("UUID가 없거나 UUID가 일치하지 않습니다.");
            if (!findUUID.isEmpty()) {
                refreshTokenRepository.deleteRefreshToken(findUUID.get());
            }
            refreshTokenRepository.deleteUUID(userId);
            return Optional.empty();
        }

        return refreshTokenRepository.findRefreshByUUID(findUUID.get());
    }

    @Override
    @Transactional
    public void save(CreateRefreshToken createRefreshToken) {
        refreshTokenRepository.saveRefreshToken(RefreshTokenDto.to(createRefreshToken));
    }
}
