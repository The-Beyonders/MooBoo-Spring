package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.refreshtoken;

import com.MooBoo.MooBoo_Spring.domain.refreshtoken.RefreshToken;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(name = "opaque_token")
    private String opaqueToken;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "expires_at")
    private Long expiresAt;

    //== 변환 메서드 ==//
    public static RefreshTokenEntity to(RefreshToken refreshToken) {
        return RefreshTokenEntity.builder()
                .opaqueToken(refreshToken.getOpaqueToken())
                .userId(refreshToken.getUserId())
                .expiresAt(refreshToken.getExpiresAt())
                .build();
    }
}
