package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRefreshToken {
    private String opaqueToken;
    private String userId;
    private Long expiresAt;
}
