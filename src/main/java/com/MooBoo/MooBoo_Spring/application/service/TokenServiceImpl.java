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
            log.info("UUID가 없거나 UUID가 일치하지 않습니다.");
            if (!findUUID.isEmpty()) {
                tokenRepository.deleteRefreshToken(findUUID.get());
            }
            tokenRepository.deleteRefreshUUID(userId);
            return Optional.empty();
        }

        return tokenRepository.findRefreshByUUID(findUUID.get());
    }

    @Override
    @Transactional
    public void saveRefreshToken(CreateRefreshToken createRefreshToken) {
        tokenRepository.saveRefreshToken(RefreshTokenDto.to(createRefreshToken));
    }
}
