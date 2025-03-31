package com.MooBoo.MooBoo_Spring.application.service;

import com.MooBoo.MooBoo_Spring.adapter.inbound.api.login.dto.CreateOAuth2User;
import com.MooBoo.MooBoo_Spring.adapter.outbound.persistence.user.UserEntity;
import com.MooBoo.MooBoo_Spring.application.port.inbound.bookapi.UserService;
import com.MooBoo.MooBoo_Spring.application.port.outbound.persistence.UserRepository;
import com.MooBoo.MooBoo_Spring.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByProviderId(String providedId) {
        Optional<User> userByProviderId = userRepository.findUserByProviderId(providedId);
        if (userByProviderId.isEmpty()) {
            return null;
        }
        return userByProviderId.get();
    }

    @Override
    @Transactional
    public void signUpUser(CreateOAuth2User createOAuth2User) {
        userRepository.saveUser(User.to(createOAuth2User));
    }
}
