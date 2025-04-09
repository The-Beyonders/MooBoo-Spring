package com.MooBoo.MooBoo_Spring.application.port.outbound.external.bookapi;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.bookapi.dto.BookApiDto;
import com.MooBoo.MooBoo_Spring.adapter.inbound.api.bookapi.dto.SearchParam;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookApiClient {

    Mono<List<BookApiDto>> getBooksBySearchParam(SearchParam searchParam);

    Mono<BookApiDto> getBookByIsbn(String isbn);
}
