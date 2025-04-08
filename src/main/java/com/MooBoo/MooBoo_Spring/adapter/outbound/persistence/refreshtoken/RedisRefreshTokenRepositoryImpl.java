package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.RefreshTokenDto;
import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RedisOperations<String, Object> redisOperations;

    private static final String PREFIX_USER = "refresh:user:";
    private static final String PREFIX_UUID = "refresh:uuid:";

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;

    @Override
    public Optional<String> findUUIDByUserId(String userId) {
        String findUUID = (String) redisOperations.opsForValue().get(PREFIX_USER + userId);
        return Optional.ofNullable(findUUID);
    }

    @Override
    public Optional<String> findRefreshByUUID(String uuid) {
        String opaqueToken = (String) redisOperations.opsForValue().get(PREFIX_UUID + uuid);
        return Optional.ofNullable(opaqueToken);
    }

    @Override
    public void saveRefreshToken(RefreshTokenDto refreshTokenDto) {
        redisOperations.opsForValue().set(
                PREFIX_USER + refreshTokenDto.getUserId(),
                refreshTokenDto.getUuid(),
                refreshTokenValidity, TimeUnit.DAYS
        );
        redisOperations.opsForValue().set(
                PREFIX_UUID + refreshTokenDto.getUuid(),
                refreshTokenDto.getOpaqueToken(),
                refreshTokenValidity, TimeUnit.DAYS
        );
    }

    @Override
    public void deleteRefreshToken(String uuid) {
        redisOperations.delete(PREFIX_UUID + uuid);
    }

    @Override
    public void deleteUUID(String userId) {
        redisOperations.delete(PREFIX_USER + userId);
    }
}
