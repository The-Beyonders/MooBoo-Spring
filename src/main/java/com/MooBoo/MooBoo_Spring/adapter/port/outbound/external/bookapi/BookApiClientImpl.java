package com.MooBoo.MooBoo_Spring.adapter.port.outbound.external;

import com.MooBoo.MooBoo_Spring.domain.bookapi.BookApi;
import com.MooBoo.MooBoo_Spring.adapter.port.outbound.external.dto.BookApiResponse;
import com.MooBoo.MooBoo_Spring.adapter.port.inbound.api.dto.SearchParam;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.BookApiClient;
import com.MooBoo.MooBoo_Spring.domain.bookapi.exception.BookApiBadRequestException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookApiClientImpl implements BookApiClient {

    private final WebClient bookApiClient;

    /**
     * 책 목록 조회를 위한 메서드
     */
    @Override
    public Mono<List<BookApi>> getBooksBySearchParam(SearchParam searchParam)  {
        /**
         * 구독 체인 정의
         */
        Mono<List<BookApi>> result = bookApiClient.get().uri(uriBuilder -> uriBuilder
                    .path("/ItemSearch.aspx")
                    .queryParam("Query", searchParam.getKeyword())
                    .queryParam("MaxResults", searchParam.getMaxResults())
                    .queryParam("OutPut", searchParam.getFormat())
                    .build())
                .retrieve()                                                                             // 요청 전송
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    System.out.println("4xx Client Error. response = " + response.statusCode());        // 적절한 예외를 만들어 Mono.error()로 반환
                    return response.createException();
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    System.out.println("5xx Server Error. response = " + response.statusCode());
                    return response.createException();
                })
                .bodyToMono(BookApiResponse.class)                                                      // JSON -> 객체로 변환(역직렬화)
                .map(bookApiResponse -> bookApiResponse.getItem())
                .map(items -> items.stream()                                              // BookItem(DTO) -> BookApi(도메인)
                        .map(item -> BookApi.fromBookItem(item))
                        .toList()
                ).subscribeOn(Schedulers.boundedElastic()); // 별도의 스레드가 처리하도록 지정

        return result;
    }

    /**
     * 특정 책 조회를 위한 메서드
     */
    @Override
    public Mono<BookApi> getBookByIsbn(String isbn) {
        Mono<BookApi> result = bookApiClient.get().uri(uriBuilder -> uriBuilder
                        .path("/ItemLookUp.aspx")
                        .queryParam("ItemIdType", "ISBN13")
                        .queryParam("ItemId", isbn)
                        .queryParam("OutPut", "JS")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    System.out.println("4xx Client Error. response = " + response.statusCode());        // 적절한 예외를 만들어 Mono.error()로 반환
                    return response.createException();
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    System.out.println("5xx Server Error. response = " + response.statusCode());
                    return response.createException();
                })
                .bodyToMono(BookApiResponse.class)
                .map(bookApiResponse -> bookApiResponse.getItem())
                .flatMap(items -> { // Mono/Flux를 변환한다 (값 → Mono/Flux)

                    if (items.isEmpty())
                        return Mono.error(new BookApiBadRequestException("도서 정보를 찾을 수 없습니다."));

                    BookApi mapped = items.stream()
                            .map(item -> BookApi.fromBookItem(item))
                            .toList().get(0);
                    return Mono.just(mapped);

                })
                .subscribeOn(Schedulers.boundedElastic());

        return result;
    }
}