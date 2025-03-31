package com.MooBoo.MooBoo_Spring.adapter.inbound.api;

import com.MooBoo.MooBoo_Spring.adapter.dto.Result;
import com.MooBoo.MooBoo_Spring.application.port.inbound.BookApiService;
import com.MooBoo.MooBoo_Spring.adapter.inbound.api.dto.BookSearchResponse;
import com.MooBoo.MooBoo_Spring.adapter.inbound.api.dto.SearchParam;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookApiController {

    private final BookApiService bookApiService;

    /**
     * 책 목록 조회 - 책 검색할 때 사용
     */
    @GetMapping("/api/v1/books-search")
    public Mono<Result<List<BookSearchResponse>>> searchBooksV1(SearchParam searchParam) {
        if (searchParam.getKeyword().trim().equals("")) {
            return Mono.just(Result.wrapper("책 목록 조회", HttpStatus.OK, new ArrayList<BookSearchResponse>()));
        }

        return bookApiService.searchBooks(searchParam)
                .map(bookApis -> bookApis.stream()
                        .map(bookApi -> bookApi.toBookSearchResponse())
                        .toList())
                .map(bookSearchResponses -> Result.wrapper("책 목록 조회", HttpStatus.OK, bookSearchResponses))
                .onErrorResume(e ->
                        Mono.just(Result.wrapper("요청 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null))
                );
    }

    /**
     * 책 한 권 자세하게 조회
     */
    @GetMapping("/api/v1/books-search/{isbn}")
    public Mono<Result<BookSearchResponse>> searchBookV1(@PathVariable("isbn") String isbn) {
        return bookApiService.searchBook(isbn)
                .map(bookApi -> Result.wrapper("책 자세히 검색", HttpStatus.OK, bookApi.toBookSearchResponse()))
                .onErrorResume(e ->
                        Mono.just(Result.wrapper("요청 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null))
                );
    }
}
