package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.user;

import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.RoleRepository;
import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.UserRepository;
import com.MooBoo.MooBoo_Spring.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager em;
    private final RoleRepository roleRepository;

    @Override
    public Optional<User> findUserByProviderId(String providerId) {
        Optional<UserEntity> result = em.createQuery("select u from UserEntity u " +
                        " where providerId =: providerId", UserEntity.class)
                .setParameter("providerId", providerId)
                .getResultList()
                .stream()
                .findFirst();

        return result
                .map(userEntity -> User.to(userEntity));
    }

    @Override
    public void saveUser(User user) {
        UserEntity userEntity = UserEntity.to(user);
        List<RoleEntity> roleEntities = roleRepository.getAll();
        if (roleEntities == null) {
            throw new IllegalStateException("서버 권한 목록이 조회되지 않았습니다.");
        }

        // 가져온 권한 목록을 Map으로 변환
        Map<UserRole, RoleEntity> collect = roleEntities.stream()
                .collect(Collectors.toMap(
                        roleEntity -> roleEntity.getUserRole(),
                        roleEntity -> roleEntity
                ));

        // 사용자의 권한과 일치하는 권한을 Map에서 꺼내 등록
        user.getUserRoles().forEach(
                userRole -> {
                    UserRoleEntity userRoleEntity = UserRoleEntity.create(userEntity, collect.get(userRole));
                    userEntity.addUserRoles(userRoleEntity);
                });

        em.persist(userEntity);
    }
}
