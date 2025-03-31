package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

/**
 * 유저 영속성 엔티티
 * 폼 로그인 및 OAuth2 인증을 위해 여러 필드를 가지도록 설계
 */

@Entity
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
}
