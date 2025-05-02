package com.MooBoo.MooBoo_Spring.application.port.inbound;

import com.MooBoo.MooBoo_Spring.adapter.inbound.oauth.dto.CreateOAuth2User;
import com.MooBoo.MooBoo_Spring.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    Optional<User> loadUserByProviderId(String providedId);

    void signUpUser(CreateOAuth2User createOAuth2User);
}
