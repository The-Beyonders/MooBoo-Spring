package com.MooBoo.MooBoo_Spring.application.service;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.CreateRefreshToken;
import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.RefreshTokenDto;
import com.MooBoo.MooBoo_Spring.application.port.inbound.TokenService;
import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Override
    public Optional<String> findRefreshToken(String userId, String uuid) {

        Optional<String> findUUID = tokenRepository.findRefreshUUIDByUserId(userId);

        // UUID가 없거나 일치하지 않는 경우
        if (findUUID.isEmpty() || !uuid.equals(findUUID.get())) {
            log.info("refreshUUID가 없거나 refreshUUID가 일치하지 않습니다.");
            return Optional.empty();
        }

        return tokenRepository.findRefreshByUUID(findUUID.get());
    }

    @Override
    @Transactional
    public void saveRefreshToken(CreateRefreshToken createRefreshToken) {
        tokenRepository.saveRefreshToken(RefreshTokenDto.to(createRefreshToken));
    }

    @Override
    public void deleteRefreshTokenAndUUID(String userId) {
        Optional<String> findUUID = tokenRepository.findRefreshUUIDByUserId(userId);
        if (!findUUID.isEmpty()) {
            tokenRepository.deleteRefreshToken(findUUID.get());
        }
        tokenRepository.deleteRefreshUUID(userId);
    }

    @Override
    public Optional<String> findAccessUUID(String userId, String uuid) {

        Optional<String> findUUID = tokenRepository.findAccessUUIDByUserId(userId);
        if (findUUID.isEmpty() || !uuid.equals(findUUID.get())) {
            log.info("accessUUID가 없거나 accessUUID가 일치하지 않습니다.");
            return Optional.empty();
        }
        return findUUID;
    }

    @Override
    public void saveAccessUUID(String userId, String uuid) {
        tokenRepository.saveAccessUUID(userId, uuid);
    }

    @Override
    public void deleteAccessUUID(String userId) {
        tokenRepository.deleteAccessUUID(userId);
    }
}
