package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.user;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_role")
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    public static UserRoleEntity create(UserEntity userEntity, RoleEntity roleEntity) {
        return UserRoleEntity.builder()
                .user(userEntity)
                .role(roleEntity)
                .build();
    }
}
