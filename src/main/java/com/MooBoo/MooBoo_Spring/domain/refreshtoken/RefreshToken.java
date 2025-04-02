package com.MooBoo.MooBoo_Spring.domain.refreshtoken;

import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.RefreshTokenEntity;
import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto.CreateRefreshToken;
import lombok.*;


@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    private Long id;
    private String opaqueToken;
    private String userId;
    private Long expiresAt;

    //== 변환 메서드==//
    public static RefreshToken to(CreateRefreshToken createRefreshToken) {
        return RefreshToken.builder()
                .opaqueToken(createRefreshToken.getOpaqueToken())
                .userId(createRefreshToken.getUserId())
                .expiresAt(createRefreshToken.getExpiresAt())
                .build();
    }

    public static RefreshToken to(RefreshTokenEntity refreshTokenEntity) {
        return RefreshToken.builder()
                .id(refreshTokenEntity.getId())
                .opaqueToken(refreshTokenEntity.getOpaqueToken())
                .userId(refreshTokenEntity.getUserId())
                .expiresAt(refreshTokenEntity.getExpiresAt())
                .build();
    }

    //== 비즈니스 로직 ==//
    public boolean validate(String refreshToken) {
        if(this.getOpaqueToken().equals(refreshToken) && System.currentTimeMillis() <= this.getExpiresAt())
            return true;
        return false;
    }
}
