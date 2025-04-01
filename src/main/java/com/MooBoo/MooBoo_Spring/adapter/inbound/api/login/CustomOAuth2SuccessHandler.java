package com.MooBoo.MooBoo_Spring.adapter.inbound.api.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        System.out.println("principal = " + principal.toString());

        Object credentials = authentication.getCredentials();
        System.out.println("credentials.toString() = " + credentials.toString());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        authorities.forEach(authorite -> System.out.println("authorite.toString() = " + authorite.toString()));

        Object details = authentication.getDetails();
        System.out.println("details.toString() = " + details.toString());

        String name = authentication.getName();
        System.out.println("name = " + name);
    }
}
