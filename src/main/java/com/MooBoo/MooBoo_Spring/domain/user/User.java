package com.MooBoo.MooBoo_Spring.domain.user;

import com.MooBoo.MooBoo_Spring.adapter.inbound.api.login.dto.CreateOAuth2User;
import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.user.UserEntity;
import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.user.UserRole;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 유저 도메인
 */
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private Long userId;
    private String userName;
    private String password;
    private String provider;
    private String providerId;
    private String email;
    private List<UserRole> userRoles = new ArrayList<>();

    /**
     * 필독 !!
     * Getter를 public으로 열어두지만, 변환 목적 및 도메인 내에서 비즈니스 로직 작성 외에 사용하지 말 것 !!
     */
    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getEmail() {
        return email;
    }


    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    //== 변환 메서드 ==//
    public static User to(UserEntity userEntity) {
        User user = User.builder()
                .userId(userEntity.getUserId())
                .userName(userEntity.getUserName())
                .password(userEntity.getPassword())
                .provider(userEntity.getProvider())
                .providerId(userEntity.getProviderId())
                .email(userEntity.getEmail())
                .userRoles(new ArrayList<>())
                .build();

        userEntity.getUserRoles().forEach(userRoleEntity -> {
            user.getUserRoles().add(userRoleEntity.getRole().getUserRole());
            }
        );
        return user;
    }

    /**
     * CreateOAuth2User -> User
     */
    public static User to(CreateOAuth2User createOAuth2User) {
        User user = User.builder()
                .userName(createOAuth2User.getUserName())
                .provider(createOAuth2User.getProvider())
                .providerId(createOAuth2User.getProviderId())
                .userRoles(new ArrayList<>())
                .build();
        user.getUserRoles().add(UserRole.USER);
        return user;
    }
}



