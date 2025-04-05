package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.RefreshTokenDto;
import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.RefreshTokenRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RedisOperations<String, Object> redisOperations;

    private static final String PREFIX = "refresh:";
    @Override
    public Optional<RefreshTokenDto> findByOpaqueToken(String opaqueToken) {
        ObjectMapper objectMapper = new ObjectMapper();
        Object raw = redisOperations.opsForValue().get(PREFIX + opaqueToken);
        return Optional.ofNullable(raw)
                .map(o -> objectMapper.convertValue(o, RefreshTokenDto.class));
    }

    @Override
    public void saveRefreshToken(RefreshTokenDto reFreshTokenDto) {
        redisOperations.opsForValue().set(
                PREFIX + reFreshTokenDto.getOpaqueToken(),
                reFreshTokenDto,
                7, TimeUnit.DAYS
        );
    }
}
