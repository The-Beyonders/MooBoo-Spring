package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.RefreshTokenDto;
import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.TokenRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisTokenRepositoryImpl implements TokenRepository {

    private final RedisOperations<String, Object> redisOperations;

    private static final String PREFIX_REFRESH_USER = "refresh:user:";
    private static final String PREFIX_REFRESH_UUID = "refresh:uuid:";

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;

    @Override
    public Optional<String> findRefreshUUIDByUserId(String userId) {
        String findUUID = (String) redisOperations.opsForValue().get(PREFIX_REFRESH_USER + userId);
        return Optional.ofNullable(findUUID);
    }

    @Override
    public Optional<String> findRefreshByUUID(String uuid) {
        String opaqueToken = (String) redisOperations.opsForValue().get(PREFIX_REFRESH_UUID + uuid);
        return Optional.ofNullable(opaqueToken);
    }

    @Override
    public void saveRefreshToken(RefreshTokenDto refreshTokenDto) {
        redisOperations.opsForValue().set(
                PREFIX_REFRESH_USER + refreshTokenDto.getUserId(),
                refreshTokenDto.getUuid(),
                refreshTokenValidity, TimeUnit.DAYS
        );
        redisOperations.opsForValue().set(
                PREFIX_REFRESH_UUID + refreshTokenDto.getUuid(),
                refreshTokenDto.getOpaqueToken(),
                refreshTokenValidity, TimeUnit.DAYS
        );
    }

    @Override
    public void deleteRefreshToken(String uuid) {
        redisOperations.delete(PREFIX_REFRESH_UUID + uuid);
    }

    @Override
    public void deleteRefreshUUID(String userId) {
        redisOperations.delete(PREFIX_REFRESH_USER + userId);
    }
}
