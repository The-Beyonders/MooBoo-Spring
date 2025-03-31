package com.MooBoo.MooBoo_Spring.application.port.outbound.external;

import com.MooBoo.MooBoo_Spring.domain.bookapi.BookApi;
import com.MooBoo.MooBoo_Spring.adapter.inbound.api.bookapi.dto.SearchParam;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookApiClient {

    Mono<List<BookApi>> getBooksBySearchParam(SearchParam searchParam);

    Mono<BookApi> getBookByIsbn(String isbn);
}
