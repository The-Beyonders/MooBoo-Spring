package com.MooBoo.MooBoo_Spring.application.port.outbound.persistence;

import com.MooBoo.MooBoo_Spring.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {

    Optional<User> findUserByProviderId(String providerId);
    void saveUser(User user);
}
