package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto;
import lombok.*;

@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenDto {
    private String opaqueToken;
    private String userId;

    //== 변환 메서드==//
    public static RefreshTokenDto to(CreateRefreshToken createRefreshToken) {
        return RefreshTokenDto.builder()
                .opaqueToken(createRefreshToken.getOpaqueToken())
                .userId(createRefreshToken.getUserId())
                .build();
    }
}
