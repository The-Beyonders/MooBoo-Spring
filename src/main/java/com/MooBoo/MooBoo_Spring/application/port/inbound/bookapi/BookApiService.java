package com.MooBoo.MooBoo_Spring.application.port.inbound;

import com.MooBoo.MooBoo_Spring.domain.bookapi.BookApi;
import com.MooBoo.MooBoo_Spring.adapter.inbound.api.dto.SearchParam;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface BookApiService {
    Mono<List<BookApi>> searchBooks(SearchParam searchParam);

    Mono<BookApi> searchBook(String isbn13);
}
