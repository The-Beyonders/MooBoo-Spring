package com.MooBoo.MooBoo_Spring.common.config;

import com.MooBoo.MooBoo_Spring.adapter.inbound.oauth.CustomOAuth2SuccessHandler;
import com.MooBoo.MooBoo_Spring.adapter.inbound.oauth.CustomOAuth2UserService;
import com.MooBoo.MooBoo_Spring.adapter.inbound.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // "ROLE_" 접두사 제거
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /**
         * 특정 경로에 대한 인가 작업
         */
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/oauth2/**", "/error", "/api/v1/books-search","/api/v1/books-search/**").permitAll()
                        .requestMatchers("/api/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(customOAuth2SuccessHandler)
                );

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())            // 현재 세션을 사용하지 않으므로 CSRF 위험 없음
                .formLogin(form -> form.disable())  // 폼 로그인 사용하지 않음
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP 기본 인증 방식 비활성화 (사용안함)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 현재 세션 사용 안 함

        return http.build();
    }
}
