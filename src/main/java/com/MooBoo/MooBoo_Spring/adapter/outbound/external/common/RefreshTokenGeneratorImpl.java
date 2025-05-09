package com.MooBoo.MooBoo_Spring.adapter.outbound.external.common;

import com.MooBoo.MooBoo_Spring.application.port.outbound.external.common.RefreshTokenGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class RefreshTokenGeneratorImpl implements RefreshTokenGenerator {

    private final SecureRandom secureRandom = new SecureRandom();                   // 암호학적으로 안전한 무작위 바이트 생성
    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding(); // 안전하게 인코딩하는 인코더

    /**
     * 32바이트 크기의 암호학적으로 안전한 무작위 바이트 배열을 생성하고,
     * Base64 URL-safe 방식으로 인코딩하여 문자열을 반환한다.
     * 인코딩 시 +, /, = 같은 문자를 사용하지 않기 때문에
     * URL, 쿠키, 헤더 등에서 안전하게 사용할 수 있다.
     */
    @Override
    public String createOpaqueToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return encoder.encodeToString(bytes);
    }
}
