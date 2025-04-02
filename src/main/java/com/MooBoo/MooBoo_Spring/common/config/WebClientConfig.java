package com.MooBoo.MooBoo_Spring.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpHeaders;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Configuration
public class WebClientConfig {

    @Value("${book.api.baseurl}")
    private String baseUrl;

    @Value("${book.api.key}")
    private String apikey;

    /**
     * 알라딘 독서 API 접근을 위한 WebClient 등록
     */
    @Bean
    public WebClient bookApiClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate")
                .filter((request, next) -> {         // 필수 파라미터 추가
                    URI originalUri = request.url();
                    URI newUri = UriComponentsBuilder.fromUri(originalUri)
                            .queryParam("ttbkey", apikey)
                            .queryParam("Version", "20131101")
                            .build(true)
                            .toUri();

                    ClientRequest newRequest = ClientRequest.from(request)
                            .url(newUri)
                            .build();

                    return next.exchange(newRequest);
                })
                .build();
    }
}
