package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.user;

import com.MooBoo.MooBoo_Spring.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 유저 영속성 엔티티
 * 폼 로그인 및 OAuth2 인증을 위해 여러 필드를 가지도록 설계
 * 필독 !!
 * Getter를 Public으로 열어두지만, 비즈니스 로직 작성 및 변환 로직 작성 외에 사용하지 말 것 !!
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserRoleEntity> userRoles = new ArrayList<>();

    public void addUserRoles(UserRoleEntity userRole) {
        this.getUserRoles().add(userRole);
    }

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
                .userRoles(new ArrayList<>())
                .build();
    }
}
