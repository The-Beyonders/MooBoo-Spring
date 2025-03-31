package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.user;

import com.MooBoo.MooBoo_Spring.adapter.inbound.api.login.dto.CreateOAuth2User;
import com.MooBoo.MooBoo_Spring.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저 영속성 엔티티
 * 폼 로그인 및 OAuth2 인증을 위해 여러 필드를 가지도록 설계
 * 필독 !!
 * Getter를 Public으로 열어두지만, 비즈니스 로직 작성 및 변환 로직 작성 외에 사용하지 말 것 !!
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    // 카카오, 구글 같은 서비스 서버 이름
    @Column(name = "provider")
    private String provider;

    // 사용자 식별자
    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "email")
    private String email;

    @Column(name = "image")
    private String image;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    //== 변환 메서드 ==//
    /**
     * User -> UserEntity
     */
    public static UserEntity to(User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .password(user.getPassword())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .email(user.getEmail())
                .image(user.getImage())
                .userRole(user.getUserRole())
                .build();
    }
}
