package com.MooBoo.MooBoo_Spring.adapter.inbound.api.login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class LoginController {

    @GetMapping("/api/v1/login")
    public ResponseEntity<Void> redirectToKakao() {
        URI uri = URI.create("http://localhost:8080/oauth2/authorization/kakao");
        return ResponseEntity.status(HttpStatus.FOUND)  // 302 리다이렉트
                .location(uri)
                .build();
    }
}
