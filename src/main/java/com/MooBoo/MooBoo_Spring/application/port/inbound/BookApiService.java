package com.MooBoo.MooBoo_Spring.application.port.inbound;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.bookapi.dto.BookApiDto;
import com.MooBoo.MooBoo_Spring.adapter.inbound.api.bookapi.dto.SearchParam;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface BookApiService {
    Mono<List<BookApiDto>> searchBooks(SearchParam searchParam);

    Mono<BookApiDto> searchBook(String isbn13);
}
