package com.MooBoo.MooBoo_Spring.application.service;

import com.MooBoo.MooBoo_Spring.adapter.inbound.oauth.dto.CreateOAuth2User;
import com.MooBoo.MooBoo_Spring.application.port.inbound.UserService;
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
    public Optional<User> loadUserByProviderId(String providedId) {
        return userRepository.findUserByProviderId(providedId);
    }

    @Override
    @Transactional
    public void signUpUser(CreateOAuth2User createOAuth2User) {
        userRepository.saveUser(User.to(createOAuth2User));
    }
}
