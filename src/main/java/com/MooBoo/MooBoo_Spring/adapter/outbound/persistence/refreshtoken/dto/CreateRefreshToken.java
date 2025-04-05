package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateRefreshToken {
    private String userId;
    private String opaqueToken;

    public static CreateRefreshToken create(String userId, String opaqueToken) {
        return CreateRefreshToken.builder()
                .userId(userId)
                .opaqueToken(opaqueToken)
                .build();
    }
}
