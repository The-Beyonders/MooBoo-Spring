package com.MooBoo.MooBoo_Spring.application.service;

import com.MooBoo.MooBoo_Spring.application.port.inbound.BookApiService;
import com.MooBoo.MooBoo_Spring.domain.bookapi.BookApi;
import com.MooBoo.MooBoo_Spring.adapter.inbound.api.bookapi.dto.SearchParam;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.bookapi.BookApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookApiServiceImpl implements BookApiService {

    private final BookApiClient bookApiClient;

    @Override
    public Mono<List<BookApi>> searchBooks(SearchParam searchParam) {
        return bookApiClient.getBooksBySearchParam(searchParam);
    }

    @Override
    public Mono<BookApi> searchBook(String isbn13) {
        return bookApiClient.getBookByIsbn(isbn13);
    }
}
