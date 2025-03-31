package com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.user;

import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.UserRepository;
import com.MooBoo.MooBoo_Spring.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager em;

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
        em.persist(UserEntity.to(user));
    }
}
